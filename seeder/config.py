"""Konfiguracja aplikacji Football Data Seeder."""

# API-Football configuration
# Dwa tryby: "rapidapi" lub "direct" (api-football.com)
API_MODE = "direct"  # Zmień na "rapidapi" jeśli używasz RapidAPI

# RapidAPI configuration
RAPIDAPI_HOST = "api-football-v1.p.rapidapi.com"
RAPIDAPI_BASE_URL = f"https://{RAPIDAPI_HOST}/v3"

# Direct API configuration (api-football.com)
DIRECT_API_BASE_URL = "https://v3.football.api-sports.io"

# Backend configuration
DEFAULT_BACKEND_URL = "http://localhost:8080/take"

# Dostępne ligi (nazwa -> ID w API-Football)
LEAGUES = {
    "La Liga (Hiszpania)": 140,
    "Premier League (Anglia)": 39,
    "Serie A (Włochy)": 135,
    "Bundesliga (Niemcy)": 78,
    "Ligue 1 (Francja)": 61,
    "Ekstraklasa (Polska)": 106,
}

# Dostępne sezony (Free plan: 2021-2023)
SEASONS = [2023, 2022, 2021]

# Mapowanie pozycji z API-Football na format backendu
POSITION_MAPPING = {
    "Goalkeeper": "Goalkeeper",
    "Defender": "Centre-Back",
    "Midfielder": "Central Midfield",
    "Attacker": "Centre-Forward",
}

# Bardziej szczegółowe mapowanie pozycji (jeśli API zwraca szczegóły)
DETAILED_POSITION_MAPPING = {
    "Goalkeeper": "Goalkeeper",
    "Centre-Back": "Centre-Back",
    "Left-Back": "Left-Back",
    "Right-Back": "Right-Back",
    "Defensive Midfield": "Defensive Midfield",
    "Central Midfield": "Central Midfield",
    "Attacking Midfield": "Attacking Midfield",
    "Left Winger": "Left Winger",
    "Right Winger": "Right Winger",
    "Centre-Forward": "Centre-Forward",
    "Second Striker": "Centre-Forward",
}

