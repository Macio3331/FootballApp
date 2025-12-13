package pl.take.football_league;

import pl.take.football_league.dtos.ReturnRecordLeagueTableDto;

public class RecordLeagueTableComparator implements java.util.Comparator<ReturnRecordLeagueTableDto> {
	@Override
	public int compare(ReturnRecordLeagueTableDto a, ReturnRecordLeagueTableDto b) {
        if(a.getPoints().intValue() == b.getPoints().intValue())
        {
        	if(a.getGoalDifference().intValue() == b.getGoalDifference().intValue()) return 0;
        	else return b.getGoalDifference() - a.getGoalDifference();
        }
        else return b.getPoints() - a.getPoints();
    }
}
