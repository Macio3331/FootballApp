package pl.take.football_league.dtos;

public class ReturnGoalDto {
	long id;
	int minute;
    boolean ownGoal;
    String scorer;
    String assistant;
    String match;
    
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public int getMinute() {
		return minute;
	}
	
	public void setMinute(int minute) {
		this.minute = minute;
	}
	
	public boolean isOwnGoal() {
		return ownGoal;
	}
	
	public void setOwnGoal(boolean ownGoal) {
		this.ownGoal = ownGoal;
	}
	
	public String getScorer() {
		return scorer;
	}
	
	public void setScorer(String scorer) {
		this.scorer = scorer;
	}
	
	public String getAssistant() {
		return assistant;
	}
	
	public void setAssistant(String assistant) {
		this.assistant = assistant;
	}
	
	public String getMatch() {
		return match;
	}
	
	public void setMatch(String game) {
		this.match = game;
	}
}
