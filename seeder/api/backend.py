"""Klient do komunikacji z backendem aplikacji Take."""

import requests
from requests.adapters import HTTPAdapter
from urllib3.util.retry import Retry
from typing import Optional, Callable, List
from config import DEFAULT_BACKEND_URL


class BackendClient:
    """Klient do seedowania danych w backendzie."""

    def __init__(self, base_url: str = DEFAULT_BACKEND_URL, 
                 on_log: Optional[Callable[[str, str], None]] = None):
        """
        Inicjalizacja klienta.
        
        Args:
            base_url: URL bazowy backendu
            on_log: Callback do logowania (message, level)
        """
        self.base_url = base_url.rstrip("/")
        self.on_log = on_log or (lambda msg, level: None)
        self.headers = {"Content-Type": "application/json"}
        
        # Sesja z connection pooling dla lepszej wydajności
        self.session = requests.Session()
        self.session.headers.update(self.headers)
        
        # Retry adapter
        retry_strategy = Retry(total=2, backoff_factor=0.1)
        adapter = HTTPAdapter(pool_connections=10, pool_maxsize=10, max_retries=retry_strategy)
        self.session.mount("http://", adapter)
        self.session.mount("https://", adapter)
        
        # Licznik operacji (loguj co N operacji)
        self._op_count = 0
        self._log_every = 10

    def _log(self, message: str, level: str = "info"):
        """Logowanie wiadomości."""
        self.on_log(message, level)

    def _post(self, endpoint: str, data: dict):
        """
        Wykonuje POST request.
        
        Args:
            endpoint: Ścieżka endpointu
            data: Dane do wysłania
            
        Returns:
            Odpowiedź (dict, string, lub None w przypadku błędu)
        """
        url = f"{self.base_url}{endpoint}"
        try:
            response = self.session.post(url, json=data, timeout=5)
            response.raise_for_status()
            
            # Sprawdź czy odpowiedź ma zawartość
            if not response.text or not response.text.strip():
                return {}
            
            # Spróbuj sparsować JSON, jeśli się nie uda - zwróć jako string
            try:
                return response.json()
            except ValueError:
                # Backend zwraca zwykły string jak "/clubs/1"
                return response.text.strip()
                
        except requests.exceptions.RequestException as e:
            self._log(f"Błąd POST {endpoint}: {str(e)}", "error")
            return None

    def _put(self, endpoint: str, data: dict):
        """
        Wykonuje PUT request.
        
        Args:
            endpoint: Ścieżka endpointu
            data: Dane do wysłania
            
        Returns:
            Odpowiedź (dict, string, lub None w przypadku błędu)
        """
        url = f"{self.base_url}{endpoint}"
        try:
            response = self.session.put(url, json=data, timeout=5)
            response.raise_for_status()
            
            # Sprawdź czy odpowiedź ma zawartość
            if not response.text or not response.text.strip():
                return {}
            
            try:
                return response.json()
            except ValueError:
                return response.text.strip()
                
        except requests.exceptions.RequestException as e:
            self._log(f"Błąd PUT {endpoint}: {str(e)}", "error")
            return None

    def _delete(self, endpoint: str) -> bool:
        """
        Wykonuje DELETE request.
        
        Args:
            endpoint: Ścieżka endpointu
            
        Returns:
            True jeśli sukces
        """
        url = f"{self.base_url}{endpoint}"
        try:
            response = self.session.delete(url, timeout=5)
            response.raise_for_status()
            return True
        except requests.exceptions.RequestException as e:
            self._log(f"Błąd DELETE {endpoint}: {str(e)}", "error")
            return False

    def _get(self, endpoint: str) -> Optional[list]:
        """
        Wykonuje GET request.
        
        Args:
            endpoint: Ścieżka endpointu
            
        Returns:
            Odpowiedź JSON lub None
        """
        url = f"{self.base_url}{endpoint}"
        try:
            response = self.session.get(url, timeout=5)
            response.raise_for_status()
            return response.json()
        except requests.exceptions.RequestException as e:
            self._log(f"Błąd GET {endpoint}: {str(e)}", "error")
            return None

    # ===== CLUBS =====

    def create_club(self, name: str, location: str, date_of_creation: str) -> Optional[int]:
        """
        Tworzy nowy klub.
        
        Args:
            name: Nazwa klubu
            location: Lokalizacja
            date_of_creation: Data założenia (YYYY-MM-DD)
            
        Returns:
            ID utworzonego klubu lub None
        """
        data = {
            "name": name,
            "location": location,
            "dateOfCreation": date_of_creation
        }
        result = self._post("/clubs", data)
        if result is not None:
            # Backend zwraca string "/clubs/{id}"
            club_id = self._extract_id_from_response(result)
            if club_id:
                return club_id
        return None
    
    def _extract_id_from_response(self, result) -> Optional[int]:
        """Wyciąga ID z odpowiedzi backendu (string lub dict)."""
        try:
            if isinstance(result, dict):
                # Jeśli to dict z id
                if "id" in result:
                    return int(result["id"])
                # Pusty dict
                return None
            elif isinstance(result, str):
                # Format sukcesu: "/clubs/123" lub "/players/123"
                if result.startswith("/") and "/" in result[1:]:
                    parts = result.rstrip("/").split("/")
                    return int(parts[-1])
                else:
                    # To prawdopodobnie komunikat błędu
                    self._log(f"Backend: {result}", "warning")
                    return None
            elif isinstance(result, int):
                return result
        except (ValueError, IndexError):
            pass
        return None

    def get_clubs(self) -> List[dict]:
        """Pobiera listę wszystkich klubów."""
        result = self._get("/clubs")
        return result if result else []

    def delete_club(self, club_id: int) -> bool:
        """Usuwa klub o podanym ID."""
        return self._delete(f"/clubs/{club_id}")

    # ===== PLAYERS =====

    def create_player(self, name: str, surname: str, number: int, 
                      position: str, club_id: int) -> Optional[int]:
        """
        Tworzy nowego zawodnika.
        
        Args:
            name: Imię
            surname: Nazwisko
            number: Numer na koszulce
            position: Pozycja
            club_id: ID klubu
            
        Returns:
            ID utworzonego zawodnika lub None
        """
        data = {
            "name": name,
            "surname": surname,
            "number": number if number else 0,
            "position": position,
            "clubId": club_id
        }
        result = self._post("/players", data)
        if result is not None:
            player_id = self._extract_id_from_response(result)
            if player_id:
                return player_id
        return None

    def get_players(self) -> List[dict]:
        """Pobiera listę wszystkich zawodników."""
        result = self._get("/players")
        return result if result else []

    def delete_player(self, player_id: int) -> bool:
        """Usuwa zawodnika o podanym ID."""
        return self._delete(f"/players/{player_id}")

    # ===== MATCHES =====

    def create_match(self, location: str, date: str, home_club_id: int,
                     away_club_id: int, player_ids: List[int]) -> Optional[int]:
        """
        Tworzy nowy mecz.
        
        Args:
            location: Lokalizacja meczu
            date: Data meczu (YYYY-MM-DD)
            home_club_id: ID klubu gospodarzy
            away_club_id: ID klubu gości
            player_ids: Lista ID zawodników uczestniczących
            
        Returns:
            ID utworzonego meczu lub None
        """
        data = {
            "location": location,
            "date": date,
            "homeClubId": home_club_id,
            "awayClubId": away_club_id,
            "players": player_ids
        }
        result = self._post("/matches", data)
        if result is not None:
            match_id = self._extract_id_from_response(result)
            if match_id:
                return match_id
        return None

    def update_match_played(self, match_id: int, played: bool = True) -> bool:
        """
        Aktualizuje status meczu (rozegrany/nie rozegrany).
        
        Args:
            match_id: ID meczu
            played: Czy mecz został rozegrany
            
        Returns:
            True jeśli sukces
        """
        data = {"played": played}
        result = self._put(f"/matches/{match_id}", data)
        return result is not None

    def get_matches(self) -> List[dict]:
        """Pobiera listę wszystkich meczów."""
        result = self._get("/matches")
        return result if result else []

    def delete_match(self, match_id: int) -> bool:
        """Usuwa mecz o podanym ID."""
        return self._delete(f"/matches/{match_id}")

    # ===== GOALS =====

    def create_goal(self, minute: int, own_goal: bool, scorer_id: int,
                    assistant_id: Optional[int], match_id: int) -> Optional[int]:
        """
        Tworzy nowy gol.
        
        Args:
            minute: Minuta gola
            own_goal: Czy samobój
            scorer_id: ID strzelca
            assistant_id: ID asystującego (może być None)
            match_id: ID meczu
            
        Returns:
            ID utworzonego gola lub None
        """
        data = {
            "minute": minute,
            "ownGoal": own_goal,
            "scorerId": scorer_id,
            "matchId": match_id
        }
        if assistant_id:
            data["assistantId"] = assistant_id
            
        result = self._post("/goals", data)
        if result is not None:
            goal_id = self._extract_id_from_response(result)
            if goal_id:
                return goal_id
        return None

    def get_goals(self) -> List[dict]:
        """Pobiera listę wszystkich goli."""
        result = self._get("/goals")
        return result if result else []

    def delete_goal(self, goal_id: int) -> bool:
        """Usuwa gol o podanym ID."""
        return self._delete(f"/goals/{goal_id}")

    # ===== UTILITIES =====

    def clear_all_data(self) -> bool:
        """
        Usuwa wszystkie dane z bazy.
        
        Returns:
            True jeśli sukces
        """
        self._log("Usuwanie wszystkich danych z bazy...", "info")
        
        # Najpierw gole
        goals = self.get_goals()
        for goal in goals:
            self.delete_goal(goal.get("id"))
        
        # Potem mecze
        matches = self.get_matches()
        for match in matches:
            self.delete_match(match.get("id"))
        
        # Potem zawodnicy
        players = self.get_players()
        for player in players:
            self.delete_player(player.get("id"))
        
        # Na końcu kluby
        clubs = self.get_clubs()
        for club in clubs:
            self.delete_club(club.get("id"))
        
        self._log("Wyczyszczono bazę danych", "success")
        return True

    def test_connection(self) -> bool:
        """
        Testuje połączenie z backendem.
        
        Returns:
            True jeśli połączenie działa
        """
        try:
            result = self._get("/clubs")
            if result is not None:
                self._log("Połączenie z backendem działa poprawnie", "success")
                return True
            return False
        except Exception:
            return False

