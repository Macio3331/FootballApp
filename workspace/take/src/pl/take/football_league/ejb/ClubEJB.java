package pl.take.football_league.ejb;

import java.util.ArrayList;
import java.util.Calendar;
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
public class ClubEJB {
	
	@PersistenceContext(name="komis")
	EntityManager em;
	
	Mapper mapper = new Mapper();
	
	public Pair<Integer, List<ReturnClubDto>> getClubs() {
		System.out.println("Getting all clubs!");
		@SuppressWarnings("unchecked")
		List<Club> clubList = em.createQuery("select c from Club c").getResultList();
		List<ReturnClubDto> clubDtoList = new ArrayList<ReturnClubDto>();
		for(int i = 0; i < clubList.size(); i++)
			clubDtoList.add(mapper.mapToReturnClubDto(clubList.get(i)));
		System.out.println("Returning all clubs!");
		return new Pair<Integer, List<ReturnClubDto>>(200, clubDtoList);
	}
	
	public Pair<Integer, ReturnClubDto> getClub(long idc) {
		System.out.println("Getting club with id = " + idc + "!");
		Club club = em.find(Club.class, idc);
		if(club == null)
		{
			System.out.println("Club with given id does not exist!");
			return new Pair<Integer, ReturnClubDto>(404, null);
		}
		ReturnClubDto clubDto = mapper.mapToReturnClubDto(club);
		System.out.println("Returning club with id = " + idc + "!");
		return new Pair<Integer, ReturnClubDto>(200, clubDto);
	}
	
	public Pair<Integer, List<ReturnPlayerDto>> getClubPlayers(long idc) {
		System.out.println("Getting players from club with id = " + idc + "!");
		Club club = em.find(Club.class, idc);
		if(club == null)
		{
			System.out.println("Club with given id does not exist!");
			return new Pair<Integer, List<ReturnPlayerDto>>(404, null);
		}
		Set<Player> players = club.getPlayers();
		List<ReturnPlayerDto> playerDtoList = new ArrayList<ReturnPlayerDto>();
		for(Player player : players)
			playerDtoList.add(mapper.mapToReturnPlayerDto(player));
		System.out.println("Returning players from club with id = " + idc + "!");
		return new Pair<Integer, List<ReturnPlayerDto>>(200, playerDtoList);
	}
	
	public Pair<Integer, List<ReturnGameDto>> getClubHomeMatches(long idc) {
		System.out.println("Getting home matches of club with id = " + idc + "!");
		Club club = em.find(Club.class, idc);
		if(club == null)
		{
			System.out.println("Club with given id does not exist!");
			return new Pair<Integer, List<ReturnGameDto>>(404, null);
		}
		Set<Game> matches = club.getHomeMatches();
		List<ReturnGameDto> matchDtoList = new ArrayList<ReturnGameDto>();
		for(Game match : matches)
			matchDtoList.add(mapper.mapToReturnGameDto(match));
		System.out.println("Returning home matches of club with id = " + idc + "!");
		return new Pair<Integer, List<ReturnGameDto>>(200, matchDtoList);
	}
	
	public Pair<Integer, List<ReturnGameDto>> getClubAwayMatches(long idc) {
		System.out.println("Getting away matches of club with id = " + idc + "!");
		Club club = em.find(Club.class, idc);
		if(club == null)
		{
			System.out.println("Club with given id does not exist!");
			return new Pair<Integer, List<ReturnGameDto>>(404, null);
		}
		Set<Game> matches = club.getAwayMatches();
		List<ReturnGameDto> matchDtoList = new ArrayList<ReturnGameDto>();
		for(Game match : matches)
			matchDtoList.add(mapper.mapToReturnGameDto(match));
		System.out.println("Returning away matches of club with id = " + idc + "!");
		return new Pair<Integer, List<ReturnGameDto>>(200, matchDtoList);
	}
	
	public Pair<Integer, String> createClub(CreateClubDto clubDto) {
		System.out.println("Creating club!");
		if(clubDto.getName() == null || clubDto.getLocation() == null || clubDto.getDateOfCreation() == null)
		{
			System.out.println("Dto contains null values!");
			return new Pair<Integer, String>(400, "Dto contains null values.");
		}
		if(clubDto.getDateOfCreation().compareTo(Calendar.getInstance().getTime()) > 0)
		{
			System.out.println("Date of creation cannot be after the current time!");
			return new Pair<Integer, String>(400, "Date of creation cannot be after the current time.");
		}
		Club club = mapper.mapToFlatClub(clubDto);
		em.persist(club);
		long id = club.getId();
		System.out.println("Club created!");
		return new Pair<Integer, String>(201, "/clubs/" + id);
	}

	public Pair<Integer, String> updateClub(long idc, UpdateClubDto updateClubDto) {
		System.out.println("Updating club!");
		Club club = em.find(Club.class, idc);
		if(club == null)
		{
			System.out.println("Club with given id does not exist!");
			return new Pair<Integer, String>(404, null);
		}
		if(updateClubDto.getName() != null && updateClubDto.getName() != club.getName()) club.setName(updateClubDto.getName());
		if(updateClubDto.getLocation() != null && updateClubDto.getLocation() != club.getLocation()) club.setLocation(updateClubDto.getLocation());
		if(updateClubDto.getDateOfCreation() != null && updateClubDto.getDateOfCreation() != club.getDateOfCreation())
		{
			if(updateClubDto.getDateOfCreation().compareTo(Calendar.getInstance().getTime()) > 0)
			{
				System.out.println("Date of creation cannot be after the current time!");
				return new Pair<Integer, String>(400, "Date of creation cannot be after the current time.");
			}
			club.setDateOfCreation(updateClubDto.getDateOfCreation());
		}
		club = em.merge(club);
		System.out.println("Club updated!");
		return new Pair<Integer, String>(200, "Club updated.");
	}
	
	public Pair<Integer, String> deleteClub(long idc) {
		System.out.println("Deleting club!");
		Club club = em.find(Club.class, idc);
		if(club == null)
		{
			System.out.println("Club with given id does not exist!");
			return new Pair<Integer, String>(404, "");
		}
		em.remove(club);
		System.out.println("Club deleted!");
		return new Pair<Integer, String>(200, "Club deleted.");
	}
}
