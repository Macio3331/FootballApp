"""Mapowanie danych z API-Football na format backendu."""

from typing import Dict, List, Optional, Tuple
from config import POSITION_MAPPING, DETAILED_POSITION_MAPPING


class DataMapper:
    """Mapuje dane z API-Football na format wymagany przez backend."""

    def __init__(self):
        """Inicjalizacja mappera."""
        # Mapowanie ID z API na ID w backendzie
        self.club_id_mapping: Dict[int, int] = {}  # api_team_id -> backend_club_id
        self.player_id_mapping: Dict[int, int] = {}  # api_player_id -> backend_player_id
        self.match_id_mapping: Dict[int, int] = {}  # api_fixture_id -> backend_match_id
        
        # Mapowanie graczy do klubów (backend_club_id -> lista backend_player_id)
        self.club_players: Dict[int, List[int]] = {}
        
        # Śledzenie użytych numerów w klubach (backend_club_id -> set of used numbers)
        self.club_used_numbers: Dict[int, set] = {}

    def reset_mappings(self):
        """Resetuje wszystkie mapowania ID."""
        self.club_id_mapping.clear()
        self.player_id_mapping.clear()
        self.match_id_mapping.clear()
        self.club_players.clear()
        self.club_used_numbers.clear()
    
    def get_unique_number(self, backend_club_id: int, preferred_number: int) -> int:
        """
        Zwraca unikalny numer dla zawodnika w klubie.
        
        Args:
            backend_club_id: ID klubu w backendzie
            preferred_number: Preferowany numer (z API)
            
        Returns:
            Unikalny numer dla zawodnika
        """
        if backend_club_id not in self.club_used_numbers:
            self.club_used_numbers[backend_club_id] = set()
        
        used = self.club_used_numbers[backend_club_id]
        
        # Jeśli preferowany numer jest dostępny i > 0, użyj go
        if preferred_number and preferred_number > 0 and preferred_number not in used:
            used.add(preferred_number)
            return preferred_number
        
        # Znajdź pierwszy wolny numer (zaczynając od 1)
        next_num = 1
        while next_num in used:
            next_num += 1
        
        used.add(next_num)
        return next_num

    def map_position(self, api_position: Optional[str]) -> str:
        """
        Mapuje pozycję z API na format backendu.
        
        Args:
            api_position: Pozycja z API (np. "Goalkeeper", "Defender", etc.)
            
        Returns:
            Pozycja w formacie backendu
        """
        if not api_position:
            return "Central Midfield"
        
        # Sprawdź szczegółowe mapowanie
        if api_position in DETAILED_POSITION_MAPPING:
            return DETAILED_POSITION_MAPPING[api_position]
        
        # Sprawdź ogólne mapowanie
        if api_position in POSITION_MAPPING:
            return POSITION_MAPPING[api_position]
        
        # Domyślna pozycja
        return "Central Midfield"

    def map_club(self, api_team: dict) -> dict:
        """
        Mapuje dane klubu z API na format backendu.
        
        Args:
            api_team: Dane zespołu z API-Football
            
        Returns:
            Dane w formacie backendu
        """
        team_info = api_team.get("team", {})
        venue_info = api_team.get("venue", {})
        
        founded = team_info.get("founded")
        if founded:
            date_of_creation = f"{founded}-01-01"
        else:
            date_of_creation = "1900-01-01"
        
        return {
            "name": team_info.get("name", "Unknown"),
            "location": venue_info.get("city", "Unknown"),
            "dateOfCreation": date_of_creation,
            "_api_id": team_info.get("id")
        }

    def map_player(self, api_player: dict, backend_club_id: int) -> dict:
        """
        Mapuje dane zawodnika z API na format backendu.
        
        Args:
            api_player: Dane zawodnika z API-Football
            backend_club_id: ID klubu w backendzie
            
        Returns:
            Dane w formacie backendu
        """
        name = api_player.get("name", "Unknown")
        name_parts = name.split(" ", 1)
        
        first_name = name_parts[0] if len(name_parts) > 0 else "Unknown"
        surname = name_parts[1] if len(name_parts) > 1 else ""
        
        # Pobierz unikalny numer (unikaj duplikatów)
        preferred_number = api_player.get("number") or 0
        unique_number = self.get_unique_number(backend_club_id, preferred_number)
        
        position = self.map_position(api_player.get("position"))
        
        return {
            "name": first_name,
            "surname": surname,
            "number": unique_number,
            "position": position,
            "clubId": backend_club_id,
            "_api_id": api_player.get("id")
        }

    def map_fixture(self, api_fixture: dict) -> dict:
        """
        Mapuje dane meczu z API na format backendu.
        
        Args:
            api_fixture: Dane meczu z API-Football
            
        Returns:
            Dane w formacie backendu
        """
        fixture_info = api_fixture.get("fixture", {})
        teams_info = api_fixture.get("teams", {})
        venue_info = fixture_info.get("venue", {})
        
        # Data meczu
        date_str = fixture_info.get("date", "")
        if date_str:
            # Format API: "2024-01-01T20:00:00+00:00"
            date = date_str.split("T")[0]
        else:
            date = "2024-01-01"
        
        # Status meczu
        status = fixture_info.get("status", {}).get("short", "")
        played = status in ["FT", "AET", "PEN"]  # Finished, After Extra Time, Penalties
        
        # ID klubów z API
        home_api_id = teams_info.get("home", {}).get("id")
        away_api_id = teams_info.get("away", {}).get("id")
        
        # Mapowanie na ID backendu
        home_club_id = self.club_id_mapping.get(home_api_id)
        away_club_id = self.club_id_mapping.get(away_api_id)
        
        return {
            "location": venue_info.get("name", "Unknown Stadium"),
            "date": date,
            "homeClubId": home_club_id,
            "awayClubId": away_club_id,
            "players": [],  # Będzie uzupełnione przez lineups
            "played": played,
            "_api_id": fixture_info.get("id"),
            "_home_api_id": home_api_id,
            "_away_api_id": away_api_id
        }

    def map_goal(self, api_event: dict, backend_match_id: int) -> Optional[dict]:
        """
        Mapuje wydarzenie gola z API na format backendu.
        
        Args:
            api_event: Wydarzenie z API-Football
            backend_match_id: ID meczu w backendzie
            
        Returns:
            Dane gola w formacie backendu lub None jeśli to nie gol
        """
        event_type = api_event.get("type", "")
        if event_type != "Goal":
            return None
        
        time_info = api_event.get("time", {})
        minute = time_info.get("elapsed", 0)
        
        # Szczegóły gola
        detail = api_event.get("detail", "")
        own_goal = "Own Goal" in detail
        
        # Strzelec
        player_info = api_event.get("player", {})
        scorer_api_id = player_info.get("id")
        scorer_id = self.player_id_mapping.get(scorer_api_id)
        
        # Asystent
        assist_info = api_event.get("assist", {})
        assistant_api_id = assist_info.get("id")
        assistant_id = self.player_id_mapping.get(assistant_api_id) if assistant_api_id else None
        
        if not scorer_id:
            return None
        
        return {
            "minute": minute,
            "ownGoal": own_goal,
            "scorerId": scorer_id,
            "assistantId": assistant_id,
            "matchId": backend_match_id
        }

    def map_lineups_to_player_ids(self, lineups: list) -> List[int]:
        """
        Mapuje składy meczowe na listę ID zawodników w backendzie.
        
        Args:
            lineups: Składy z API-Football
            
        Returns:
            Lista ID zawodników w backendzie
        """
        player_ids = []
        
        for team_lineup in lineups:
            # Gracze w pierwszym składzie
            start_xi = team_lineup.get("startXI", [])
            for player_entry in start_xi:
                player_info = player_entry.get("player", {})
                api_id = player_info.get("id")
                backend_id = self.player_id_mapping.get(api_id)
                if backend_id:
                    player_ids.append(backend_id)
            
            # Rezerwowi
            substitutes = team_lineup.get("substitutes", [])
            for player_entry in substitutes:
                player_info = player_entry.get("player", {})
                api_id = player_info.get("id")
                backend_id = self.player_id_mapping.get(api_id)
                if backend_id:
                    player_ids.append(backend_id)
        
        return player_ids

    def register_club_mapping(self, api_id: int, backend_id: int):
        """Rejestruje mapowanie ID klubu."""
        self.club_id_mapping[api_id] = backend_id

    def register_player_mapping(self, api_id: int, backend_id: int, backend_club_id: int = None):
        """Rejestruje mapowanie ID zawodnika i przypisanie do klubu."""
        self.player_id_mapping[api_id] = backend_id
        
        # Dodaj gracza do listy graczy klubu
        if backend_club_id is not None:
            if backend_club_id not in self.club_players:
                self.club_players[backend_club_id] = []
            self.club_players[backend_club_id].append(backend_id)

    def register_match_mapping(self, api_id: int, backend_id: int):
        """Rejestruje mapowanie ID meczu."""
        self.match_id_mapping[api_id] = backend_id
    
    def get_players_for_match(self, home_club_id: int, away_club_id: int, 
                               min_players: int = 22, max_players: int = 32) -> List[int]:
        """
        Zwraca listę graczy z obu klubów dla meczu.
        
        Args:
            home_club_id: ID klubu gospodarzy w backendzie
            away_club_id: ID klubu gości w backendzie
            min_players: Minimalna liczba graczy (domyślnie 22)
            max_players: Maksymalna liczba graczy (domyślnie 32)
            
        Returns:
            Lista ID graczy w backendzie
        """
        home_players = self.club_players.get(home_club_id, [])
        away_players = self.club_players.get(away_club_id, [])
        
        # Weź po równo z każdego klubu
        half = min_players // 2
        
        selected_home = home_players[:min(len(home_players), half + 5)]
        selected_away = away_players[:min(len(away_players), half + 5)]
        
        all_players = selected_home + selected_away
        
        # Upewnij się, że mamy wymaganą liczbę graczy
        if len(all_players) < min_players:
            return []
        
        # Ogranicz do maksymalnej liczby
        return all_players[:max_players]

    def get_backend_club_id(self, api_id: int) -> Optional[int]:
        """Zwraca ID klubu w backendzie dla danego ID API."""
        return self.club_id_mapping.get(api_id)

    def get_backend_player_id(self, api_id: int) -> Optional[int]:
        """Zwraca ID zawodnika w backendzie dla danego ID API."""
        return self.player_id_mapping.get(api_id)

    def get_backend_match_id(self, api_id: int) -> Optional[int]:
        """Zwraca ID meczu w backendzie dla danego ID API."""
        return self.match_id_mapping.get(api_id)

