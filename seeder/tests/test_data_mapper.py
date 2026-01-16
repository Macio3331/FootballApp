from seeder.mappers.data_mapper import DataMapper
import pytest



@pytest.fixture
def mapper():
    return DataMapper()


def test_reset_mappings_clears_all(mapper):
    mapper.club_id_mapping[1] = 10
    mapper.player_id_mapping[2] = 20
    mapper.match_id_mapping[3] = 30
    mapper.club_players[10] = [20]
    mapper.club_used_numbers[10] = {7}
    mapper.match_players[3] = [20]

    mapper.reset_mappings()

    assert mapper.club_id_mapping == {}
    assert mapper.player_id_mapping == {}
    assert mapper.match_id_mapping == {}
    assert mapper.club_players == {}
    assert mapper.club_used_numbers == {}
    assert mapper.match_players == {}

def test_get_unique_number_prefers_preferred(mapper):
    number = mapper.get_unique_number(backend_club_id=1, preferred_number=7)
    assert number == 7


def test_get_unique_number_finds_next_free(mapper):
    mapper.get_unique_number(1, 7)
    mapper.get_unique_number(1, 10)

    number = mapper.get_unique_number(1, 7)
    assert number == 1


def test_get_unique_number_increments(mapper):
    mapper.get_unique_number(1, 1)
    mapper.get_unique_number(1, 2)

    number = mapper.get_unique_number(1, 0)
    assert number == 3


def test_map_position_none_returns_default(mapper):
    assert mapper.map_position(None) == "Central Midfield"


def test_map_position_unknown_returns_default(mapper):
    assert mapper.map_position("UnknownPosition") == "Central Midfield"


def test_map_club_full_data(mapper):
    api_team = {
        "team": {"id": 1, "name": "FC Test", "founded": 2000},
        "venue": {"city": "Warsaw"}
    }

    result = mapper.map_club(api_team)

    assert result == {
        "name": "FC Test",
        "location": "Warsaw",
        "dateOfCreation": "2000-01-01",
        "_api_id": 1
    }


def test_map_club_missing_data(mapper):
    result = mapper.map_club({})

    assert result["name"] == "Unknown"
    assert result["location"] == "Unknown"
    assert result["dateOfCreation"] == "1900-01-01"


def test_map_player_basic(mapper):
    api_player = {
        "id": 100,
        "name": "John Doe",
        "number": 9,
        "position": "Defender"
    }

    result = mapper.map_player(api_player, backend_club_id=1)

    assert result["name"] == "John"
    assert result["surname"] == "Doe"
    assert result["number"] == 9
    assert result["clubId"] == 1
    assert result["_api_id"] == 100


def test_map_fixture_played_match(mapper):
    mapper.register_club_mapping(1, 10)
    mapper.register_club_mapping(2, 20)

    api_fixture = {
        "fixture": {
            "id": 50,
            "date": "2024-01-01T20:00:00+00:00",
            "status": {"short": "FT"},
            "venue": {"name": "Test Stadium"}
        },
        "teams": {
            "home": {"id": 1},
            "away": {"id": 2}
        }
    }

    result = mapper.map_fixture(api_fixture)

    assert result["played"] is True
    assert result["date"] == "2024-01-01"
    assert result["homeClubId"] == 10
    assert result["awayClubId"] == 20


def test_map_goal_valid(mapper):
    mapper.register_player_mapping(1, 101)
    mapper.register_player_mapping(2, 102)

    api_event = {
        "type": "Goal",
        "time": {"elapsed": 45},
        "detail": "Normal Goal",
        "player": {"id": 1},
        "assist": {"id": 2}
    }

    result = mapper.map_goal(
        api_event,
        backend_match_id=500,
        match_player_ids=[101, 102]
    )

    assert result["minute"] == 45
    assert result["scorerId"] == 101
    assert result["assistantId"] == 102
    assert result["ownGoal"] is False


def test_map_goal_not_goal_returns_none(mapper):
    api_event = {"type": "Card"}
    assert mapper.map_goal(api_event, 1) is None


def test_map_lineups_to_player_ids(mapper):
    mapper.register_player_mapping(1, 10)
    mapper.register_player_mapping(2, 20)

    lineups = [
        {
            "startXI": [{"player": {"id": 1}}],
            "substitutes": [{"player": {"id": 2}}]
        }
    ]

    result = mapper.map_lineups_to_player_ids(lineups)

    assert result == [10, 20]


def test_get_players_for_match_enough_players(mapper):
    mapper.club_players[1] = list(range(1, 20))
    mapper.club_players[2] = list(range(20, 40))

    players = mapper.get_players_for_match(1, 2)

    assert len(players) >= 22


def test_get_players_for_match_not_enough_players(mapper):
    mapper.club_players[1] = [1, 2]
    mapper.club_players[2] = [3, 4]

    players = mapper.get_players_for_match(1, 2)

    assert players == []
