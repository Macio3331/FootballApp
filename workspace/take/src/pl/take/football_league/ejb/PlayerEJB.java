package pl.take.football_league.ejb;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import pl.take.football_league.Mapper;
import pl.take.football_league.Pair;
import pl.take.football_league.dtos.*;
import pl.take.football_league.entities.*;

@Stateless
public class PlayerEJB {
	
	@PersistenceContext(name="komis")
	EntityManager em;
	
	Mapper mapper = new Mapper();
	
	public Pair<Integer, List<ReturnPlayerDto>> getPlayers() {
		System.out.println("Getting all players!");
		@SuppressWarnings("unchecked")
		List<Player> playerList = em.createQuery("select p from Player p").getResultList();
		List<ReturnPlayerDto> playerDtoList = new ArrayList<ReturnPlayerDto>();
		for(int i = 0; i < playerList.size(); i++)
			playerDtoList.add(mapper.mapToReturnPlayerDto(playerList.get(i)));
		System.out.println("Returning all players!");
		return new Pair<Integer, List<ReturnPlayerDto>>(200, playerDtoList);
	}
	
	public Pair<Integer, ReturnPlayerDto> getPlayer(long idc) {
		System.out.println("Getting player with id = " + idc + "!");
		Player player = em.find(Player.class, idc);
		if(player == null)
		{
			System.out.println("Player with given id does not exist!");
			return new Pair<Integer, ReturnPlayerDto>(404, null);
		}
		ReturnPlayerDto playerDto = mapper.mapToReturnPlayerDto(player);
		System.out.println("Returning player with id = " + idc + "!");
		return new Pair<Integer, ReturnPlayerDto>(200, playerDto);
	}
	
	public Pair<Integer, ReturnClubDto> getPlayerClub(long idc) {
		System.out.println("Getting club of player with id = " + idc + "!");
		Player player = em.find(Player.class, idc);
		if(player == null)
		{
			System.out.println("Player with given id does not exist!");
			return new Pair<Integer, ReturnClubDto>(404, null);
		}
		Club club = player.getClub();
		ReturnClubDto clubDto = mapper.mapToReturnClubDto(club);
		System.out.println("Returning club of player with id = " + idc + "!");
		return new Pair<Integer, ReturnClubDto>(200, clubDto);
	}
	
	public Pair<Integer, List<ReturnGameDto>> getPlayerMatches(long idc) {
		System.out.println("Getting matches of player with id = " + idc + "!");
		Player player = em.find(Player.class, idc);
		if(player == null)
		{
			System.out.println("Player with given id does not exist!");
			return new Pair<Integer, List<ReturnGameDto>>(404, null);
		}
		Set<Game> matches = player.getMatches();
		List<ReturnGameDto> matchDtoList = new ArrayList<ReturnGameDto>();
		for(Game match : matches)
			matchDtoList.add(mapper.mapToReturnGameDto(match));
		System.out.println("Returning matches of player with id = " + idc + "!");
		return new Pair<Integer, List<ReturnGameDto>>(200, matchDtoList);
	}
	
	public Pair<Integer, List<ReturnGoalDto>> getPlayerGoals(long idc) {
		System.out.println("Getting goals of player with id = " + idc + "!");
		Player player = em.find(Player.class, idc);
		if(player == null)
		{
			System.out.println("Player with given id does not exist!");
			return new Pair<Integer, List<ReturnGoalDto>>(404, null);
		}
		Set<Goal> goals = player.getGoals();
		List<ReturnGoalDto> goalDtoList = new ArrayList<ReturnGoalDto>();
		for(Goal goal : goals)
			goalDtoList.add(mapper.mapToReturnGoalDto(goal));
		System.out.println("Returning goals of player with id = " + idc + "!");
		return new Pair<Integer, List<ReturnGoalDto>>(200, goalDtoList);
	}
	
	public Pair<Integer, List<ReturnGoalDto>> getPlayerAssists(long idc) {
		System.out.println("Getting assists of player with id = " + idc + "!");
		Player player = em.find(Player.class, idc);
		if(player == null)
		{
			System.out.println("Player with given id does not exist!");
			return new Pair<Integer, List<ReturnGoalDto>>(404, null);
		}
		Set<Goal> assists = player.getAssists();
		List<ReturnGoalDto> assistDtoList = new ArrayList<ReturnGoalDto>();
		for(Goal assist : assists)
			assistDtoList.add(mapper.mapToReturnGoalDto(assist));
		System.out.println("Returning assists of player with id = " + idc + "!");
		return new Pair<Integer, List<ReturnGoalDto>>(200, assistDtoList);
	}
	
	public Pair<Integer, String> createPlayer(CreatePlayerDto playerDto) {
		System.out.println("Creating player!");
		if(playerDto.getName() == null || playerDto.getSurname() == null || playerDto.getNumber() == null ||
			playerDto.getPosition() == null || playerDto.getClubId() == null)
		{
			System.out.println("Dto contains null values!");
			return new Pair<Integer, String>(400, "Dto contains null values.");
		}
		if(playerDto.getNumber() < 1 || playerDto.getNumber() > 99)
		{
			System.out.println("Player's number must be between 1 and 99!");
			return new Pair<Integer, String>(400, "Player's number must be between 1 and 99.");
		}
		Club club = em.find(Club.class, playerDto.getClubId());
		if(club == null)
		{
			System.out.println("Club with given id does not exist!");
			return new Pair<Integer, String>(400, "Club with given id does not exist.");
		}
		Set<Integer> numbers = new HashSet<Integer>();
		for(Player player : club.getPlayers())
			numbers.add(player.getNumber());
		if(numbers.contains(playerDto.getNumber()))
		{
			System.out.println("Player with given number already exists!");
			return new Pair<Integer, String>(400, "Player with given number already exists.");
		}
		Player player = mapper.mapToFlatPlayer(playerDto);
		player.setClub(club);
		Set<Player> players = club.getPlayers();
		players.add(player);
		club.setPlayers(players);
		em.persist(player);
		long id = player.getId();
		System.out.println("Player created!");
		return new Pair<Integer, String>(201, "/players/" + id);
	}

	public Pair<Integer, String> updatePlayer(long idc, UpdatePlayerDto updatePlayerDto) {
		System.out.println("Updating player!");
		if(updatePlayerDto.getNumber() < 1 || updatePlayerDto.getNumber() > 99)
		{
			System.out.println("Player's number must be between 1 and 99!");
			return new Pair<Integer, String>(400, "Player's number must be between 1 and 99.");
		}
		Player player = em.find(Player.class, idc);
		if(player == null)
		{
			System.out.println("Player with given id does not exist!");
			return new Pair<Integer, String>(404, null);
		}
		if(updatePlayerDto.getName() != null && updatePlayerDto.getName() != player.getName()) player.setName(updatePlayerDto.getName());
		if(updatePlayerDto.getSurname() != null && updatePlayerDto.getSurname() != player.getSurname()) player.setSurname(updatePlayerDto.getSurname());
		if(updatePlayerDto.getNumber() != null && updatePlayerDto.getNumber() != player.getNumber())
		{
			Set<Integer> numbers = new HashSet<Integer>();
			for(Player clubPlayer : player.getClub().getPlayers())
				numbers.add(clubPlayer.getNumber());
			if(numbers.contains(updatePlayerDto.getNumber()))
			{
				System.out.println("Player with given number already exists!");
				return new Pair<Integer, String>(400, "Player with given number already exists.");
			}
			player.setNumber(updatePlayerDto.getNumber());
		}
		if(updatePlayerDto.getPosition() != null && updatePlayerDto.getPosition() != player.getPosition()) player.setPosition(updatePlayerDto.getPosition());
		player = em.merge(player);
		System.out.println("Player updated!");
		return new Pair<Integer, String>(200, "Player updated!");
	}
	
	public Pair<Integer, String> deletePlayer(long idc) {
		System.out.println("Deleting player!");
		Player player = em.find(Player.class, idc);
		if(player == null)
		{
			System.out.println("Player with given id does not exist!");
			return new Pair<Integer, String>(404, "");
		}
		Set<Player> players = player.getClub().getPlayers();
		players.remove(player);
		player.getClub().setPlayers(players);
		for(Game match : player.getMatches())
			em.remove(match);
		em.remove(player);
		System.out.println("Player deleted!");
		return new Pair<Integer, String>(200, "Player deleted.");
	}
}
