package pl.take.football_league.ejb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import pl.take.football_league.GameScheduleComparator;
import pl.take.football_league.RecordLeagueTableComparator;
import pl.take.football_league.Mapper;
import pl.take.football_league.Pair;
import pl.take.football_league.dtos.*;
import pl.take.football_league.entities.*;

@Stateless
public class LeagueEJB {

	@PersistenceContext(name="komis")
	EntityManager em;
	
	Mapper mapper = new Mapper();
	
	public Pair<Integer, List<ReturnRecordLeagueTableDto>> getTable() {
		System.out.println("Getting league table!");
		@SuppressWarnings("unchecked")
		List<Club> clubList = em.createQuery("select c from Club c").getResultList();
		List<ReturnRecordLeagueTableDto> dtoList = new ArrayList<ReturnRecordLeagueTableDto>();
		for(Club club : clubList)
		{
			ReturnRecordLeagueTableDto dto = new ReturnRecordLeagueTableDto();
			dto.setClubId(club.getId());
			Set<Game> homeMatches = club.getHomeMatches();
			Set<Game> awayMatches = club.getAwayMatches();
			dto.setNumberOfMatches(homeMatches.size() + awayMatches.size());
			int goalsScored = 0, goalsConceded = 0, numberOfWins = 0, numberOfTies = 0, numberOfLosses = 0;
			for(Game match : homeMatches)
			{
				if(match.isPlayed())
				{
					if(match.getHomeResult().intValue() > match.getAwayResult().intValue()) numberOfWins++;
					else if(match.getHomeResult().intValue() == match.getAwayResult().intValue()) numberOfTies++;
					else numberOfLosses++;
					goalsScored += match.getHomeResult().intValue();
					goalsConceded += match.getAwayResult().intValue();
				}
			}
			for(Game match : awayMatches)
			{
				if(match.isPlayed())
				{
					if(match.getAwayResult().intValue() > match.getHomeResult().intValue()) numberOfWins++;
					else if(match.getAwayResult().intValue() == match.getHomeResult().intValue()) numberOfTies++;
					else numberOfLosses++;
					goalsScored += match.getAwayResult().intValue();
					goalsConceded += match.getHomeResult().intValue();
				}
			}
			dto.setNumberOfWins(numberOfWins);
			dto.setNumberOfTies(numberOfTies);
			dto.setNumberOfLosses(numberOfLosses);
			dto.setPoints(3 * numberOfWins + numberOfTies);
			dto.setGoalsScored(goalsScored);
			dto.setGoalsConceded(goalsConceded);
			dto.setGoalDifference(goalsScored - goalsConceded);
			dto.setPlace(null);
			dtoList.add(dto);
		}
		Collections.sort(dtoList, new RecordLeagueTableComparator());
		for(int i = 0; i < dtoList.size(); i++)
		{
			if(i == 0)
			{
				ReturnRecordLeagueTableDto dto = dtoList.get(i);
				dto.setPlace(i + 1);
				dtoList.set(0, dto);
			}
			else
			{
				ReturnRecordLeagueTableDto previousDto = dtoList.get(i - 1);
				ReturnRecordLeagueTableDto currentDto = dtoList.get(i);
				RecordLeagueTableComparator comparator = new RecordLeagueTableComparator();
				if(comparator.compare(previousDto, currentDto) == 0) currentDto.setPlace(previousDto.getPlace());
				else currentDto.setPlace(i + 1);
				dtoList.set(i, currentDto);
			}
		}
		System.out.println("Returning league table!");
		return new Pair<Integer, List<ReturnRecordLeagueTableDto>>(200, dtoList);
	}
	
	public Pair<Integer, List<ReturnGameDto>> getSchedule() {
		System.out.println("Getting league schedule!");
		@SuppressWarnings("unchecked")
		List<Game> matchList = em.createQuery("select g from Game g").getResultList();
		if(!matchList.isEmpty()) Collections.sort(matchList, new GameScheduleComparator());
		List<ReturnGameDto> matchListDto = new ArrayList<ReturnGameDto>();
		for(int i = 0; i < matchList.size(); i++)
			matchListDto.add(mapper.mapToReturnGameDto(matchList.get(i)));
		System.out.println("Returning league schedule!");
		return new Pair<Integer, List<ReturnGameDto>>(200, matchListDto);
	}
}
