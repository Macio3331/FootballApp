"""Główne okno GUI aplikacji Football Data Seeder."""

import tkinter as tk
from tkinter import ttk, scrolledtext, messagebox
import threading
from typing import Optional
import sys
import os

# Dodaj katalog nadrzędny do ścieżki, aby móc importować moduły
sys.path.insert(0, os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

from config import LEAGUES, SEASONS, DEFAULT_BACKEND_URL, API_MODE
from api.football_api import FootballAPIClient
from api.backend import BackendClient
from mappers.data_mapper import DataMapper


class FootballSeederApp:
    """Główna aplikacja GUI do seedowania danych piłkarskich."""

    def __init__(self):
        """Inicjalizacja aplikacji."""
        self.root = tk.Tk()
        self.root.title("Football Data Seeder")
        self.root.geometry("800x700")
        self.root.minsize(700, 600)
        
        # Zmienne
        self.api_key_var = tk.StringVar()
        self.api_mode_var = tk.StringVar(value=API_MODE)  # "direct" lub "rapidapi"
        self.league_var = tk.StringVar(value=list(LEAGUES.keys())[0])
        self.season_var = tk.StringVar(value=str(SEASONS[0]))
        self.backend_url_var = tk.StringVar(value=DEFAULT_BACKEND_URL)
        
        # Checkboxy dla typów danych
        self.seed_clubs_var = tk.BooleanVar(value=True)
        self.seed_players_var = tk.BooleanVar(value=True)
        self.seed_matches_var = tk.BooleanVar(value=True)
        self.seed_goals_var = tk.BooleanVar(value=True)
        
        # Limit meczów (aby nie przekroczyć limitów API)
        self.match_limit_var = tk.StringVar(value="10")
        
        # Status
        self.is_running = False
        self.should_stop = False
        
        # Klienci
        self.football_client: Optional[FootballAPIClient] = None
        self.backend_client: Optional[BackendClient] = None
        self.data_mapper: Optional[DataMapper] = None
        
        # Statystyki
        self.stats = {
            "clubs": 0,
            "players": 0,
            "matches": 0,
            "goals": 0
        }
        
        self._create_widgets()
        self._apply_styles()

    def _apply_styles(self):
        """Aplikuje style do widgetów."""
        style = ttk.Style()
        style.configure("Title.TLabel", font=("Segoe UI", 16, "bold"))
        style.configure("Section.TLabel", font=("Segoe UI", 11, "bold"))
        style.configure("Success.TLabel", foreground="green")
        style.configure("Error.TLabel", foreground="red")
        style.configure("Info.TLabel", foreground="blue")

    def _create_widgets(self):
        """Tworzy wszystkie widgety interfejsu."""
        # Główny kontener
        main_frame = ttk.Frame(self.root, padding="10")
        main_frame.pack(fill=tk.BOTH, expand=True)
        
        # Tytuł
        title_label = ttk.Label(
            main_frame, 
            text="⚽ Football Data Seeder", 
            style="Title.TLabel"
        )
        title_label.pack(pady=(0, 15))
        
        # === Sekcja konfiguracji API ===
        self._create_api_config_section(main_frame)
        
        # === Sekcja wyboru danych ===
        self._create_data_selection_section(main_frame)
        
        # === Sekcja konfiguracji backendu ===
        self._create_backend_config_section(main_frame)
        
        # === Przyciski akcji ===
        self._create_action_buttons(main_frame)
        
        # === Pasek postępu ===
        self._create_progress_section(main_frame)
        
        # === Logi ===
        self._create_log_section(main_frame)

    def _create_api_config_section(self, parent):
        """Tworzy sekcję konfiguracji API."""
        frame = ttk.LabelFrame(parent, text="Konfiguracja API-Football", padding="10")
        frame.pack(fill=tk.X, pady=(0, 10))
        
        # Tryb API
        mode_frame = ttk.Frame(frame)
        mode_frame.pack(fill=tk.X, pady=(0, 5))
        
        ttk.Label(mode_frame, text="Źródło API:").pack(side=tk.LEFT)
        ttk.Radiobutton(
            mode_frame, text="api-football.com (Direct)", 
            variable=self.api_mode_var, value="direct"
        ).pack(side=tk.LEFT, padx=(10, 10))
        ttk.Radiobutton(
            mode_frame, text="RapidAPI", 
            variable=self.api_mode_var, value="rapidapi"
        ).pack(side=tk.LEFT)
        
        # Klucz API
        api_frame = ttk.Frame(frame)
        api_frame.pack(fill=tk.X, pady=(0, 5))
        
        ttk.Label(api_frame, text="Klucz API:").pack(side=tk.LEFT)
        self.api_key_entry = ttk.Entry(api_frame, textvariable=self.api_key_var, width=50, show="*")
        self.api_key_entry.pack(side=tk.LEFT, padx=(10, 5), fill=tk.X, expand=True)
        
        self.show_key_btn = ttk.Button(api_frame, text="👁", width=3, command=self._toggle_api_key_visibility)
        self.show_key_btn.pack(side=tk.LEFT)
        
        # Liga i sezon
        selection_frame = ttk.Frame(frame)
        selection_frame.pack(fill=tk.X, pady=(5, 0))
        
        ttk.Label(selection_frame, text="Liga:").pack(side=tk.LEFT)
        league_combo = ttk.Combobox(
            selection_frame, 
            textvariable=self.league_var, 
            values=list(LEAGUES.keys()),
            state="readonly",
            width=30
        )
        league_combo.pack(side=tk.LEFT, padx=(10, 20))
        
        ttk.Label(selection_frame, text="Sezon:").pack(side=tk.LEFT)
        season_combo = ttk.Combobox(
            selection_frame, 
            textvariable=self.season_var, 
            values=[str(s) for s in SEASONS],
            state="readonly",
            width=10
        )
        season_combo.pack(side=tk.LEFT, padx=(10, 0))

    def _create_data_selection_section(self, parent):
        """Tworzy sekcję wyboru typów danych."""
        frame = ttk.LabelFrame(parent, text="Dane do pobrania", padding="10")
        frame.pack(fill=tk.X, pady=(0, 10))
        
        check_frame = ttk.Frame(frame)
        check_frame.pack(fill=tk.X)
        
        ttk.Checkbutton(check_frame, text="Kluby", variable=self.seed_clubs_var).pack(side=tk.LEFT, padx=(0, 15))
        ttk.Checkbutton(check_frame, text="Zawodnicy", variable=self.seed_players_var).pack(side=tk.LEFT, padx=(0, 15))
        ttk.Checkbutton(check_frame, text="Mecze", variable=self.seed_matches_var).pack(side=tk.LEFT, padx=(0, 15))
        ttk.Checkbutton(check_frame, text="Gole", variable=self.seed_goals_var).pack(side=tk.LEFT, padx=(0, 15))
        
        # Limit meczów
        limit_frame = ttk.Frame(frame)
        limit_frame.pack(fill=tk.X, pady=(10, 0))
        
        ttk.Label(limit_frame, text="Limit meczów (0 = bez limitu):").pack(side=tk.LEFT)
        limit_spinbox = ttk.Spinbox(
            limit_frame, 
            textvariable=self.match_limit_var, 
            from_=0, to=100, 
            width=5
        )
        limit_spinbox.pack(side=tk.LEFT, padx=(10, 0))
        
        ttk.Label(
            limit_frame, 
            text="⚠️ Każdy mecz wymaga dodatkowych zapytań API",
            foreground="orange"
        ).pack(side=tk.LEFT, padx=(15, 0))

    def _create_backend_config_section(self, parent):
        """Tworzy sekcję konfiguracji backendu."""
        frame = ttk.LabelFrame(parent, text="Konfiguracja Backendu", padding="10")
        frame.pack(fill=tk.X, pady=(0, 10))
        
        url_frame = ttk.Frame(frame)
        url_frame.pack(fill=tk.X)
        
        ttk.Label(url_frame, text="URL Backendu:").pack(side=tk.LEFT)
        ttk.Entry(url_frame, textvariable=self.backend_url_var, width=50).pack(side=tk.LEFT, padx=(10, 10), fill=tk.X, expand=True)
        
        ttk.Button(url_frame, text="Test", command=self._test_backend_connection).pack(side=tk.LEFT)

    def _create_action_buttons(self, parent):
        """Tworzy przyciski akcji."""
        frame = ttk.Frame(parent)
        frame.pack(fill=tk.X, pady=(0, 10))
        
        self.start_btn = ttk.Button(
            frame, 
            text="▶ Pobierz i Seeduj Dane", 
            command=self._start_seeding
        )
        self.start_btn.pack(side=tk.LEFT, padx=(0, 10))
        
        self.stop_btn = ttk.Button(
            frame, 
            text="⏹ Zatrzymaj", 
            command=self._stop_seeding,
            state=tk.DISABLED
        )
        self.stop_btn.pack(side=tk.LEFT, padx=(0, 10))
        
        self.clear_btn = ttk.Button(
            frame, 
            text="🗑 Wyczyść Bazę", 
            command=self._clear_database
        )
        self.clear_btn.pack(side=tk.LEFT, padx=(0, 10))
        
        self.test_api_btn = ttk.Button(
            frame, 
            text="🔗 Test API", 
            command=self._test_api_connection
        )
        self.test_api_btn.pack(side=tk.LEFT)

    def _create_progress_section(self, parent):
        """Tworzy sekcję paska postępu."""
        frame = ttk.LabelFrame(parent, text="Postęp", padding="10")
        frame.pack(fill=tk.X, pady=(0, 10))
        
        # Główny pasek postępu
        self.progress_var = tk.DoubleVar()
        self.progress_bar = ttk.Progressbar(
            frame, 
            variable=self.progress_var,
            maximum=100,
            mode="determinate"
        )
        self.progress_bar.pack(fill=tk.X, pady=(0, 5))
        
        # Status
        self.status_label = ttk.Label(frame, text="Gotowy do pracy")
        self.status_label.pack(fill=tk.X)
        
        # Statystyki
        stats_frame = ttk.Frame(frame)
        stats_frame.pack(fill=tk.X, pady=(5, 0))
        
        self.clubs_label = ttk.Label(stats_frame, text="Kluby: 0")
        self.clubs_label.pack(side=tk.LEFT, padx=(0, 20))
        
        self.players_label = ttk.Label(stats_frame, text="Zawodnicy: 0")
        self.players_label.pack(side=tk.LEFT, padx=(0, 20))
        
        self.matches_label = ttk.Label(stats_frame, text="Mecze: 0")
        self.matches_label.pack(side=tk.LEFT, padx=(0, 20))
        
        self.goals_label = ttk.Label(stats_frame, text="Gole: 0")
        self.goals_label.pack(side=tk.LEFT)

    def _create_log_section(self, parent):
        """Tworzy sekcję logów."""
        frame = ttk.LabelFrame(parent, text="Logi", padding="10")
        frame.pack(fill=tk.BOTH, expand=True)
        
        self.log_text = scrolledtext.ScrolledText(
            frame, 
            height=12,
            font=("Consolas", 9),
            wrap=tk.WORD
        )
        self.log_text.pack(fill=tk.BOTH, expand=True)
        
        # Tagi kolorów dla logów
        self.log_text.tag_configure("info", foreground="black")
        self.log_text.tag_configure("success", foreground="green")
        self.log_text.tag_configure("error", foreground="red")
        self.log_text.tag_configure("warning", foreground="orange")
        
        # Przycisk czyszczenia logów
        ttk.Button(frame, text="Wyczyść logi", command=self._clear_logs).pack(pady=(5, 0))

    def _toggle_api_key_visibility(self):
        """Przełącza widoczność klucza API."""
        if self.api_key_entry.cget("show") == "*":
            self.api_key_entry.config(show="")
            self.show_key_btn.config(text="🙈")
        else:
            self.api_key_entry.config(show="*")
            self.show_key_btn.config(text="👁")

    def _log(self, message: str, level: str = "info"):
        """
        Dodaje wpis do logów.
        
        Args:
            message: Treść wiadomości
            level: Poziom logu (info, success, error, warning)
        """
        def _do_log():
            timestamp = self._get_timestamp()
            prefix = {
                "info": "ℹ️",
                "success": "✅",
                "error": "❌",
                "warning": "⚠️"
            }.get(level, "ℹ️")
            
            self.log_text.insert(tk.END, f"[{timestamp}] {prefix} {message}\n", level)
            self.log_text.see(tk.END)
        
        # Upewnij się, że logowanie jest w głównym wątku
        if threading.current_thread() is threading.main_thread():
            _do_log()
        else:
            self.root.after(0, _do_log)

    def _get_timestamp(self) -> str:
        """Zwraca aktualny znacznik czasu."""
        from datetime import datetime
        return datetime.now().strftime("%H:%M:%S")

    def _clear_logs(self):
        """Czyści logi."""
        self.log_text.delete(1.0, tk.END)

    def _update_status(self, message: str):
        """Aktualizuje label statusu."""
        def _do_update():
            self.status_label.config(text=message)
        
        if threading.current_thread() is threading.main_thread():
            _do_update()
        else:
            self.root.after(0, _do_update)

    def _update_progress(self, value: float):
        """Aktualizuje pasek postępu."""
        def _do_update():
            self.progress_var.set(value)
        
        if threading.current_thread() is threading.main_thread():
            _do_update()
        else:
            self.root.after(0, _do_update)

    def _update_stats(self):
        """Aktualizuje wyświetlane statystyki."""
        def _do_update():
            self.clubs_label.config(text=f"Kluby: {self.stats['clubs']}")
            self.players_label.config(text=f"Zawodnicy: {self.stats['players']}")
            self.matches_label.config(text=f"Mecze: {self.stats['matches']}")
            self.goals_label.config(text=f"Gole: {self.stats['goals']}")
        
        if threading.current_thread() is threading.main_thread():
            _do_update()
        else:
            self.root.after(0, _do_update)

    def _reset_stats(self):
        """Resetuje statystyki."""
        self.stats = {"clubs": 0, "players": 0, "matches": 0, "goals": 0}
        self._update_stats()

    def _set_buttons_state(self, running: bool):
        """Ustawia stan przycisków."""
        def _do_update():
            if running:
                self.start_btn.config(state=tk.DISABLED)
                self.stop_btn.config(state=tk.NORMAL)
                self.clear_btn.config(state=tk.DISABLED)
                self.test_api_btn.config(state=tk.DISABLED)
            else:
                self.start_btn.config(state=tk.NORMAL)
                self.stop_btn.config(state=tk.DISABLED)
                self.clear_btn.config(state=tk.NORMAL)
                self.test_api_btn.config(state=tk.NORMAL)
        
        if threading.current_thread() is threading.main_thread():
            _do_update()
        else:
            self.root.after(0, _do_update)

    def _validate_inputs(self) -> bool:
        """Waliduje dane wejściowe."""
        if not self.api_key_var.get().strip():
            messagebox.showerror("Błąd", "Podaj klucz API!")
            return False
        
        if not self.backend_url_var.get().strip():
            messagebox.showerror("Błąd", "Podaj URL backendu!")
            return False
        
        if not any([
            self.seed_clubs_var.get(),
            self.seed_players_var.get(),
            self.seed_matches_var.get(),
            self.seed_goals_var.get()
        ]):
            messagebox.showerror("Błąd", "Wybierz przynajmniej jeden typ danych do pobrania!")
            return False
        
        return True

    def _test_api_connection(self):
        """Testuje połączenie z API-Football."""
        if not self.api_key_var.get().strip():
            messagebox.showerror("Błąd", "Podaj klucz API!")
            return
        
        self._log("Testowanie połączenia z API-Football...", "info")
        
        def _test():
            client = FootballAPIClient(
                self.api_key_var.get(), 
                self._log,
                api_mode=self.api_mode_var.get()
            )
            if client.test_connection():
                self._log("Połączenie z API-Football działa!", "success")
            else:
                self._log("Nie udało się połączyć z API-Football", "error")
        
        threading.Thread(target=_test, daemon=True).start()

    def _test_backend_connection(self):
        """Testuje połączenie z backendem."""
        self._log("Testowanie połączenia z backendem...", "info")
        
        def _test():
            client = BackendClient(self.backend_url_var.get(), self._log)
            if client.test_connection():
                self._log("Połączenie z backendem działa!", "success")
            else:
                self._log("Nie udało się połączyć z backendem", "error")
        
        threading.Thread(target=_test, daemon=True).start()

    def _clear_database(self):
        """Czyści bazę danych."""
        if not messagebox.askyesno("Potwierdzenie", "Czy na pewno chcesz usunąć wszystkie dane z bazy?"):
            return
        
        self._log("Czyszczenie bazy danych...", "info")
        
        def _clear():
            client = BackendClient(self.backend_url_var.get(), self._log)
            if client.clear_all_data():
                self._log("Baza danych wyczyszczona!", "success")
            else:
                self._log("Wystąpił błąd podczas czyszczenia bazy", "error")
        
        threading.Thread(target=_clear, daemon=True).start()

    def _start_seeding(self):
        """Rozpoczyna proces seedowania."""
        if not self._validate_inputs():
            return
        
        self.is_running = True
        self.should_stop = False
        self._set_buttons_state(True)
        self._reset_stats()
        self._update_progress(0)
        
        # Uruchom seedowanie w osobnym wątku
        threading.Thread(target=self._run_seeding, daemon=True).start()

    def _stop_seeding(self):
        """Zatrzymuje proces seedowania."""
        self.should_stop = True
        self._log("Zatrzymywanie...", "warning")
        self._update_status("Zatrzymywanie...")

    def _run_seeding(self):
        """Główna logika seedowania (uruchamiana w osobnym wątku)."""
        try:
            self._log("Rozpoczynanie seedowania danych...", "info")
            
            # Inicjalizacja klientów
            self.football_client = FootballAPIClient(
                self.api_key_var.get(), 
                self._log,
                api_mode=self.api_mode_var.get()
            )
            self.backend_client = BackendClient(self.backend_url_var.get(), self._log)
            self.data_mapper = DataMapper()
            
            # Pobierz parametry
            league_name = self.league_var.get()
            league_id = LEAGUES[league_name]
            season = int(self.season_var.get())
            match_limit = int(self.match_limit_var.get())
            
            self._log(f"Liga: {league_name}, Sezon: {season}", "info")
            
            total_steps = 0
            if self.seed_clubs_var.get():
                total_steps += 1
            if self.seed_players_var.get():
                total_steps += 1
            if self.seed_matches_var.get():
                total_steps += 1
            if self.seed_goals_var.get():
                total_steps += 1
            
            current_step = 0
            
            # 1. Pobierz i seeduj kluby
            if self.seed_clubs_var.get() and not self.should_stop:
                self._update_status("Pobieranie klubów...")
                current_step += 1
                self._update_progress((current_step / total_steps) * 25)
                
                teams = self.football_client.get_teams(league_id, season)
                for team in teams:
                    if self.should_stop:
                        break
                    
                    club_data = self.data_mapper.map_club(team)
                    api_id = club_data.pop("_api_id")
                    
                    backend_id = self.backend_client.create_club(
                        club_data["name"],
                        club_data["location"],
                        club_data["dateOfCreation"]
                    )
                    
                    if backend_id:
                        self.data_mapper.register_club_mapping(api_id, backend_id)
                        self.stats["clubs"] += 1
                        self._update_stats()
            
            # 2. Pobierz i seeduj zawodników
            if self.seed_players_var.get() and not self.should_stop:
                self._update_status("Pobieranie zawodników...")
                current_step += 1
                
                for api_team_id, backend_club_id in self.data_mapper.club_id_mapping.items():
                    if self.should_stop:
                        break
                    
                    players = self.football_client.get_squad(api_team_id)
                    for player in players:
                        if self.should_stop:
                            break
                        
                        player_data = self.data_mapper.map_player(player, backend_club_id)
                        api_id = player_data.pop("_api_id")
                        
                        backend_id = self.backend_client.create_player(
                            player_data["name"],
                            player_data["surname"],
                            player_data["number"],
                            player_data["position"],
                            player_data["clubId"]
                        )
                        
                        if backend_id and api_id:
                            self.data_mapper.register_player_mapping(api_id, backend_id, backend_club_id)
                            self.stats["players"] += 1
                            self._update_stats()
                
                self._update_progress((current_step / total_steps) * 50)
            
            # 3. Pobierz i seeduj mecze
            if self.seed_matches_var.get() and not self.should_stop:
                self._update_status("Pobieranie meczów...")
                current_step += 1
                
                fixtures = self.football_client.get_fixtures(league_id, season)
                
                # Filtruj tylko rozegrane mecze i zastosuj limit
                played_fixtures = [
                    f for f in fixtures 
                    if f.get("fixture", {}).get("status", {}).get("short") in ["FT", "AET", "PEN"]
                ]
                
                if match_limit > 0:
                    played_fixtures = played_fixtures[:match_limit]
                
                self._log(f"Znaleziono {len(played_fixtures)} rozegranych meczów do zaimportowania", "info")
                
                for fixture in played_fixtures:
                    if self.should_stop:
                        break
                    
                    match_data = self.data_mapper.map_fixture(fixture)
                    api_id = match_data.pop("_api_id")
                    match_data.pop("_home_api_id")
                    match_data.pop("_away_api_id")
                    was_played = match_data.pop("played")
                    
                    # Sprawdź czy mamy oba kluby
                    home_club_id = match_data["homeClubId"]
                    away_club_id = match_data["awayClubId"]
                    
                    if not home_club_id or not away_club_id:
                        self._log(f"Pominięto mecz - brak klubu w mapowaniu", "warning")
                        continue
                    
                    # Pobierz graczy z obu klubów (22-32 graczy)
                    player_ids = self.data_mapper.get_players_for_match(home_club_id, away_club_id)
                    
                    if len(player_ids) < 22:
                        self._log(f"Pominięto mecz - za mało graczy ({len(player_ids)}/22)", "warning")
                        continue
                    
                    match_data["players"] = player_ids
                    
                    backend_id = self.backend_client.create_match(
                        match_data["location"],
                        match_data["date"],
                        match_data["homeClubId"],
                        match_data["awayClubId"],
                        match_data["players"]
                    )
                    
                    if backend_id:
                        self.data_mapper.register_match_mapping(api_id, backend_id)
                        self.stats["matches"] += 1
                        self._update_stats()
                
                self._update_progress((current_step / total_steps) * 75)
            
            # 4. Pobierz i seeduj gole
            if self.seed_goals_var.get() and not self.should_stop:
                self._update_status("Pobieranie goli...")
                current_step += 1
                
                for api_fixture_id, backend_match_id in self.data_mapper.match_id_mapping.items():
                    if self.should_stop:
                        break
                    
                    events = self.football_client.get_fixture_events(api_fixture_id)
                    
                    for event in events:
                        if self.should_stop:
                            break
                        
                        goal_data = self.data_mapper.map_goal(event, backend_match_id)
                        if goal_data and goal_data["scorerId"]:
                            backend_id = self.backend_client.create_goal(
                                goal_data["minute"],
                                goal_data["ownGoal"],
                                goal_data["scorerId"],
                                goal_data["assistantId"],
                                goal_data["matchId"]
                            )
                            
                            if backend_id:
                                self.stats["goals"] += 1
                                self._update_stats()
                
                self._update_progress(90)
            
            # 5. Oznacz mecze jako rozegrane (po dodaniu goli)
            if self.seed_matches_var.get() and not self.should_stop:
                self._update_status("Finalizowanie meczów...")
                
                for api_fixture_id, backend_match_id in self.data_mapper.match_id_mapping.items():
                    if self.should_stop:
                        break
                    self.backend_client.update_match_played(backend_match_id, True)
                
                self._update_progress(100)
            
            if self.should_stop:
                self._log("Seedowanie przerwane przez użytkownika", "warning")
                self._update_status("Przerwano")
            else:
                self._log("Seedowanie zakończone pomyślnie!", "success")
                self._update_status("Zakończono")
                self._update_progress(100)
            
        except Exception as e:
            self._log(f"Wystąpił błąd: {str(e)}", "error")
            self._update_status("Błąd")
        finally:
            self.is_running = False
            self._set_buttons_state(False)

    def run(self):
        """Uruchamia główną pętlę aplikacji."""
        self.root.mainloop()


if __name__ == "__main__":
    app = FootballSeederApp()
    app.run()

