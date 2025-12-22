"""Klient API-Football do pobierania danych piłkarskich."""

import requests
import time
from collections import deque
from requests.adapters import HTTPAdapter
from urllib3.util.retry import Retry
from typing import Optional, Callable
from config import (
    API_MODE, 
    RAPIDAPI_HOST, 
    RAPIDAPI_BASE_URL, 
    DIRECT_API_BASE_URL
)


class FootballAPIClient:
    """Klient do komunikacji z API-Football."""

    # Rate limit: 10 requestów na minutę
    RATE_LIMIT = 10
    RATE_WINDOW = 60  # sekund

    def __init__(self, api_key: str, on_log: Optional[Callable[[str, str], None]] = None,
                 api_mode: str = None):
        """
        Inicjalizacja klienta.
        
        Args:
            api_key: Klucz API
            on_log: Callback do logowania (message, level)
            api_mode: "direct" (api-football.com) lub "rapidapi" (RapidAPI)
        """
        self.api_key = api_key
        self.on_log = on_log or (lambda msg, level: None)
        self.api_mode = api_mode or API_MODE
        
        if self.api_mode == "rapidapi":
            self.base_url = RAPIDAPI_BASE_URL
            self.headers = {
                "X-RapidAPI-Key": api_key,
                "X-RapidAPI-Host": RAPIDAPI_HOST
            }
        else:  # direct
            self.base_url = DIRECT_API_BASE_URL
            self.headers = {
                "x-apisports-key": api_key
            }
        
        # Sesja z connection pooling
        self.session = requests.Session()
        self.session.headers.update(self.headers)
        
        retry_strategy = Retry(total=2, backoff_factor=0.5)
        adapter = HTTPAdapter(pool_connections=5, pool_maxsize=5, max_retries=retry_strategy)
        self.session.mount("https://", adapter)
        
        # Rate limiting - przechowuj timestampy ostatnich requestów
        self._request_times: deque = deque(maxlen=self.RATE_LIMIT)

    def _wait_for_rate_limit(self):
        """Czeka jeśli przekroczono rate limit."""
        now = time.time()
        
        # Usuń stare timestampy (starsze niż RATE_WINDOW)
        while self._request_times and (now - self._request_times[0]) > self.RATE_WINDOW:
            self._request_times.popleft()
        
        # Jeśli osiągnęliśmy limit, poczekaj
        if len(self._request_times) >= self.RATE_LIMIT:
            oldest = self._request_times[0]
            wait_time = self.RATE_WINDOW - (now - oldest) + 0.5  # +0.5s margines
            if wait_time > 0:
                self._log(f"Rate limit - czekam {wait_time:.1f}s...", "warning")
                time.sleep(wait_time)
        
        # Zarejestruj nowy request
        self._request_times.append(time.time())

    def _log(self, message: str, level: str = "info"):
        """Logowanie wiadomości."""
        self.on_log(message, level)

    def _make_request(self, endpoint: str, params: Optional[dict] = None) -> Optional[dict]:
        """
        Wykonanie zapytania do API.
        
        Args:
            endpoint: Ścieżka endpointu (np. '/leagues')
            params: Parametry query string
            
        Returns:
            Odpowiedź JSON lub None w przypadku błędu
        """
        # Respektuj rate limit
        self._wait_for_rate_limit()
        
        url = f"{self.base_url}{endpoint}"
        try:
            self._log(f"Pobieranie: {endpoint}", "info")
            response = self.session.get(url, params=params, timeout=15)
            response.raise_for_status()
            data = response.json()
            
            if data.get("errors"):
                errors = data["errors"]
                if isinstance(errors, dict):
                    error_msg = ", ".join(f"{k}: {v}" for k, v in errors.items())
                else:
                    error_msg = str(errors)
                self._log(f"Błąd API: {error_msg}", "error")
                return None
                
            return data
        except requests.exceptions.Timeout:
            self._log(f"Timeout podczas pobierania {endpoint}", "error")
            return None
        except requests.exceptions.RequestException as e:
            self._log(f"Błąd połączenia: {str(e)}", "error")
            return None
        except ValueError as e:
            self._log(f"Błąd parsowania JSON: {str(e)}", "error")
            return None

    def get_leagues(self) -> list:
        """
        Pobiera listę dostępnych lig.
        
        Returns:
            Lista lig z API
        """
        data = self._make_request("/leagues")
        if data and "response" in data:
            leagues = data["response"]
            self._log(f"Pobrano {len(leagues)} lig", "success")
            return leagues
        return []

    def get_teams(self, league_id: int, season: int) -> list:
        """
        Pobiera kluby z danej ligi i sezonu.
        
        Args:
            league_id: ID ligi w API-Football
            season: Rok sezonu (np. 2024)
            
        Returns:
            Lista klubów
        """
        data = self._make_request("/teams", {"league": league_id, "season": season})
        if data and "response" in data:
            teams = data["response"]
            self._log(f"Pobrano {len(teams)} klubów", "success")
            return teams
        return []

    def get_squad(self, team_id: int) -> list:
        """
        Pobiera skład drużyny.
        
        Args:
            team_id: ID klubu w API-Football
            
        Returns:
            Lista zawodników
        """
        data = self._make_request("/players/squads", {"team": team_id})
        if data and "response" in data and len(data["response"]) > 0:
            players = data["response"][0].get("players", [])
            self._log(f"Pobrano {len(players)} zawodników dla klubu {team_id}", "success")
            return players
        return []

    def get_fixtures(self, league_id: int, season: int) -> list:
        """
        Pobiera mecze z danej ligi i sezonu.
        
        Args:
            league_id: ID ligi w API-Football
            season: Rok sezonu
            
        Returns:
            Lista meczów
        """
        data = self._make_request("/fixtures", {"league": league_id, "season": season})
        if data and "response" in data:
            fixtures = data["response"]
            self._log(f"Pobrano {len(fixtures)} meczów", "success")
            return fixtures
        return []

    def get_fixture_events(self, fixture_id: int) -> list:
        """
        Pobiera wydarzenia z meczu (gole, kartki, itp.).
        
        Args:
            fixture_id: ID meczu w API-Football
            
        Returns:
            Lista wydarzeń
        """
        data = self._make_request("/fixtures/events", {"fixture": fixture_id})
        if data and "response" in data:
            events = data["response"]
            return events
        return []

    def get_fixture_lineups(self, fixture_id: int) -> list:
        """
        Pobiera składy meczowe.
        
        Args:
            fixture_id: ID meczu w API-Football
            
        Returns:
            Lista składów (home, away)
        """
        data = self._make_request("/fixtures/lineups", {"fixture": fixture_id})
        if data and "response" in data:
            lineups = data["response"]
            return lineups
        return []

    def test_connection(self) -> bool:
        """
        Testuje połączenie z API.
        
        Returns:
            True jeśli połączenie działa
        """
        try:
            data = self._make_request("/timezone")
            if data and "response" in data:
                self._log("Połączenie z API-Football działa poprawnie", "success")
                return True
            return False
        except Exception:
            return False

