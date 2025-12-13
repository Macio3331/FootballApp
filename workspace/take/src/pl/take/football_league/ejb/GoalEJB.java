package pl.take.football_league.ejb;

import java.util.ArrayList;
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
public class GoalEJB {

	@PersistenceContext(name="komis")
	EntityManager em;
	
	Mapper mapper = new Mapper();
	
	public Pair<Integer, List<ReturnGoalDto>> getGoals() {
		System.out.println("Getting all goals!");
		@SuppressWarnings("unchecked")
		List<Goal> goalList = em.createQuery("select g from Goal g").getResultList();
		List<ReturnGoalDto> goalDtoList = new ArrayList<ReturnGoalDto>();
		for(int i = 0; i < goalList.size(); i++)
			goalDtoList.add(mapper.mapToReturnGoalDto(goalList.get(i)));
		System.out.println("Returning all goals!");
		return new Pair<Integer, List<ReturnGoalDto>>(200, goalDtoList);
	}
	
	public Pair<Integer, ReturnGoalDto> getGoal(long idc) {
		System.out.println("Getting goal with id = " + idc + "!");
		Goal goal = em.find(Goal.class, idc);
		if(goal == null)
		{
			System.out.println("Goal with given id does not exist!");
			return new Pair<Integer, ReturnGoalDto>(404, null);
		}
		ReturnGoalDto goalDto = mapper.mapToReturnGoalDto(goal);
		System.out.println("Returning goal with id = " + idc + "!");
		return new Pair<Integer, ReturnGoalDto>(200, goalDto);
	}
	
	public Pair<Integer, ReturnPlayerDto> getGoalScorer(long idc) {
		System.out.println("Getting scorer of goal with id = " + idc + "!");
		Goal goal = em.find(Goal.class, idc);
		if(goal == null)
		{
			System.out.println("Goal with given id does not exist!");
			return new Pair<Integer, ReturnPlayerDto>(404, null);
		}
		Player player = goal.getScorer();
		ReturnPlayerDto playerDto = mapper.mapToReturnPlayerDto(player);
		System.out.println("Returning scorer of goal with id = " + idc + "!");
		return new Pair<Integer, ReturnPlayerDto>(200, playerDto);
	}
	
	public Pair<Integer, ReturnPlayerDto> getGoalAssistant(long idc) {
		System.out.println("Getting assistant of goal with id = " + idc + "!");
		Goal goal = em.find(Goal.class, idc);
		if(goal == null)
		{
			System.out.println("Goal with given id does not exist!");
			return new Pair<Integer, ReturnPlayerDto>(404, null);
		}
		Player player = goal.getAssistant();
		ReturnPlayerDto playerDto = mapper.mapToReturnPlayerDto(player);
		System.out.println("Returning assistant of goal with id = " + idc + "!");
		return new Pair<Integer, ReturnPlayerDto>(200, playerDto);
	}
	
	public Pair<Integer, ReturnGameDto> getGoalMatch(long idc) {
		System.out.println("Getting match of goal with id = " + idc + "!");
		Goal goal = em.find(Goal.class, idc);
		if(goal == null)
		{
			System.out.println("Goal with given id does not exist!");
			return new Pair<Integer, ReturnGameDto>(404, null);
		}
		Game match = goal.getMatch();
		ReturnGameDto matchDto = mapper.mapToReturnGameDto(match);
		System.out.println("Returning match of goal with id = " + idc + "!");
		return new Pair<Integer, ReturnGameDto>(200, matchDto);
	}
	
	public Pair<Integer, String> createGoal(CreateGoalDto goalDto) {
		System.out.println("Creating goal!");
		if(goalDto.getMinute() == null || goalDto.isOwnGoal() == null || goalDto.getScorerId() == null ||
			goalDto.getAssistantId() == null || goalDto.getMatchId() == null)
		{
			System.out.println("Dto contains null values!");
			return new Pair<Integer, String>(400, "Dto contains null values.");
		}
		if(goalDto.getMinute() < 1 || goalDto.getMinute() > 120)
		{
			System.out.println("Goal must be scored between 1st and 120th minute!");
			return new Pair<Integer, String>(400, "Goal must be scored between 1st and 120th minute.");
		}
		if(goalDto.getScorerId() == goalDto.getAssistantId())
		{
			System.out.println("Assistant cannot be the same as scorer!");
			return new Pair<Integer, String>(400, "Assistant cannot be the same as scorer.");
		}
		Game match = em.find(Game.class, goalDto.getMatchId());
		if(match == null)
		{
			System.out.println("Match with given id does not exist!");
			return new Pair<Integer, String>(400, "Match with given id does not exist.");
		}
		if(match.isPlayed())
		{
			System.out.println("Goal cannot be created for a played match!");
			return new Pair<Integer, String>(400, "Goal cannot be created for a played match.");
		}
		Player scorer = em.find(Player.class, goalDto.getScorerId());
		if(scorer == null)
		{
			System.out.println("Scorer with given id does not exist!");
			return new Pair<Integer, String>(400, "Scorer with given id does not exist.");
		}
		Player assistant = em.find(Player.class, goalDto.getAssistantId());
		if(assistant == null)
		{
			System.out.println("Assistant with given id does not exist!");
			return new Pair<Integer, String>(400, "Assistant with given id does not exist.");
		}
		if(!match.getPlayers().contains(scorer))
		{
			System.out.println("Scorer with given id did not play in given match!");
			return new Pair<Integer, String>(400, "Scorer with given id did not play in given match.");
		}
		if(!match.getPlayers().contains(assistant))
		{
			System.out.println("Assistant with given id did not play in given match!");
			return new Pair<Integer, String>(400, "Assistant with given id did not play in given match.");
		}
		Goal goal = mapper.mapToFlatGoal(goalDto);
		goal.setMatch(match);
		Set<Goal> matchGoals = match.getGoals();
		matchGoals.add(goal);
		match.setGoals(matchGoals);
		goal.setScorer(scorer);
		Set<Goal> scorerGoals = scorer.getGoals();
		scorerGoals.add(goal);
		scorer.setGoals(scorerGoals);
		goal.setAssistant(assistant);
		Set<Goal> assistantGoals = assistant.getGoals();
		assistantGoals.add(goal);
		assistant.setGoals(assistantGoals);
		em.persist(goal);
		long id = goal.getId();
		System.out.println("Goal created!");
		return new Pair<Integer, String>(201, "/goals/" + id);
	}

	public Pair<Integer, String> updateGoal(long idc, UpdateGoalDto updateGoalDto) {
		System.out.println("Updating goal!");
		Goal goal = em.find(Goal.class, idc);
		if(goal == null)
		{
			System.out.println("Goal with given id does not exist!");
			return new Pair<Integer, String>(404, null);
		}
		if(updateGoalDto.getMinute() != null && updateGoalDto.getMinute() != goal.getMinute())
		{
			if(updateGoalDto.getMinute() < 1 || updateGoalDto.getMinute() > 120)
			{
				System.out.println("Goal must be scored between 1st and 120th minute!");
				return new Pair<Integer, String>(400, "Goal must be scored between 1st and 120th minute!");
			}
			goal.setMinute(updateGoalDto.getMinute());
		}
		if(updateGoalDto.isOwnGoal() != null && updateGoalDto.isOwnGoal() != goal.isOwnGoal()) goal.setOwnGoal(updateGoalDto.isOwnGoal());
		goal = em.merge(goal);
		System.out.println("Goal updated!");
		return new Pair<Integer, String>(200, "Goal updated.");
	}
	
	public Pair<Integer, String> deleteGoal(long idc) {
		System.out.println("Deleting goal!");
		Goal goal = em.find(Goal.class, idc);
		if(goal == null)
		{
			System.out.println("Goal with given id does not exist!");
			return new Pair<Integer, String>(404, "");
		}
		em.remove(goal);
		System.out.println("Goal deleted!");
		return new Pair<Integer, String>(200, "Goal deleted.");
	}
}
