"""
Football Data Seeder - Aplikacja GUI do pobierania danych piłkarskich
z API-Football i seedowania bazy danych backendu.
"""

from gui.app import FootballSeederApp


def main():
    """Uruchomienie aplikacji."""
    app = FootballSeederApp()
    app.run()


if __name__ == "__main__":
    main()

