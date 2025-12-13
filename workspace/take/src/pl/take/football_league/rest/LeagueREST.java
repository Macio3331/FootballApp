package pl.take.football_league.rest;

import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import pl.take.football_league.Pair;
import pl.take.football_league.dtos.*;
import pl.take.football_league.ejb.LeagueEJB;

@Path("/league")
@Consumes({ "application/json" })
@Produces({ "application/json" })

//@Consumes({ "application/xml" })
//@Produces({ "application/xml" })
public class LeagueREST {

	@EJB
	LeagueEJB leagueBean;

	
	@GET
	@Path("/table")
	public Response getTable() {
		Pair<Integer, List<ReturnRecordLeagueTableDto>> result = leagueBean.getTable();
		return getResponse(result.getFirst(), result.getSecond());
	}
	
	@GET
	@Path("/schedule")
	public Response getSchedule() {
		Pair<Integer, List<ReturnGameDto>> result = leagueBean.getSchedule();
		return getResponse(result.getFirst(), result.getSecond());
	}
	
	
	private Response getResponse(int code, Object entity)
	{
		switch(code)
		{
			case 200:
				return Response.status(Response.Status.OK).entity(entity).build();
			case 201:
				return Response.status(Response.Status.CREATED).entity(entity).build();
			case 400:
				return Response.status(Response.Status.BAD_REQUEST).entity(entity).build();
			case 404:
				return Response.status(Response.Status.NOT_FOUND).entity("Player with given id does not exist.").build();
			default:
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Something went wrong.").build();
		}
	}
}
