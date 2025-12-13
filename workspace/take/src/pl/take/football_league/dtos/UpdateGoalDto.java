package pl.take.football_league.dtos;

public class UpdateGoalDto {
	Integer minute;
    Boolean ownGoal;
    
	public Integer getMinute() {
		return minute;
	}
	
	public void setMinute(Integer minute) {
		this.minute = minute;
	}
	
	public Boolean isOwnGoal() {
		return ownGoal;
	}
	
	public void setOwnGoal(Boolean ownGoal) {
		this.ownGoal = ownGoal;
	}
}
