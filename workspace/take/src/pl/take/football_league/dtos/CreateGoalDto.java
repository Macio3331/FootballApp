package pl.take.football_league.dtos;

public class CreateGoalDto {
	Integer minute;
    Boolean ownGoal;
    Long scorerId;
    Long assistantId;
    Long matchId;
    
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
	
	public Long getScorerId() {
		return scorerId;
	}
	
	public void setScorerId(Long scorerId) {
		this.scorerId = scorerId;
	}
	
	public Long getAssistantId() {
		return assistantId;
	}
	
	public void setAssistantId(Long assistantId) {
		this.assistantId = assistantId;
	}
	
	public Long getMatchId() {
		return matchId;
	}
	
	public void setMatchId(Long matchId) {
		this.matchId = matchId;
	}
}
