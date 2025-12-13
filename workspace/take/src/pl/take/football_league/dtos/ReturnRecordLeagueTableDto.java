package pl.take.football_league.dtos;

public class ReturnRecordLeagueTableDto {
	Long clubId;
	Integer place;
	Integer numberOfMatches;
	Integer numberOfWins;
	Integer numberOfTies;
	Integer numberOfLosses;
	Integer goalsScored;
	Integer goalsConceded;
	Integer goalDifference;
	Integer points;
	
	public Long getClubId() {
		return clubId;
	}
	
	public void setClubId(Long clubId) {
		this.clubId = clubId;
	}
	
	public Integer getPlace() {
		return place;
	}
	
	public void setPlace(Integer place) {
		this.place = place;
	}
	
	public Integer getNumberOfMatches() {
		return numberOfMatches;
	}
	
	public void setNumberOfMatches(Integer numberOfMatches) {
		this.numberOfMatches = numberOfMatches;
	}
	
	public Integer getNumberOfWins() {
		return numberOfWins;
	}
	
	public void setNumberOfWins(Integer numberOfWins) {
		this.numberOfWins = numberOfWins;
	}
	
	public Integer getNumberOfTies() {
		return numberOfTies;
	}
	
	public void setNumberOfTies(Integer numberOfTies) {
		this.numberOfTies = numberOfTies;
	}
	
	public Integer getNumberOfLosses() {
		return numberOfLosses;
	}
	
	public void setNumberOfLosses(Integer numberOfLosses) {
		this.numberOfLosses = numberOfLosses;
	}
	
	public Integer getGoalsScored() {
		return goalsScored;
	}
	
	public void setGoalsScored(Integer goalsScored) {
		this.goalsScored = goalsScored;
	}
	
	public Integer getGoalsConceded() {
		return goalsConceded;
	}
	
	public void setGoalsConceded(Integer goalsConceded) {
		this.goalsConceded = goalsConceded;
	}
	
	public Integer getGoalDifference() {
		return goalDifference;
	}
	
	public void setGoalDifference(Integer goalDifference) {
		this.goalDifference = goalDifference;
	}
	
	public Integer getPoints() {
		return points;
	}
	
	public void setPoints(Integer points) {
		this.points = points;
	}
}
