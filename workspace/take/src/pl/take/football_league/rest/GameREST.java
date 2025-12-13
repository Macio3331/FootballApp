package pl.take.football_league.rest;

import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import pl.take.football_league.Pair;
import pl.take.football_league.dtos.*;
import pl.take.football_league.ejb.GameEJB;

@Path("/matches")
@Consumes({ "application/json" })
@Produces({ "application/json" })

//@Consumes({ "application/xml" })
//@Produces({ "application/xml" })
public class GameREST {

	@EJB
	GameEJB gameBean;

	
	@GET
	public Response getMatches() {
		Pair<Integer, List<ReturnGameDto>> result = gameBean.getMatches();
		return getResponse(result.getFirst(), result.getSecond());
	}
	
	@GET
	@Path("/{idc}")
	public Response getMatch(@PathParam("idc") long idc) {
		Pair<Integer, ReturnGameDto> result = gameBean.getMatch(idc);
		return getResponse(result.getFirst(), result.getSecond());
	}
	
	@GET
	@Path("/{idc}/homeClub")
	public Response getMatchHomeClub(@PathParam("idc") long idc) {
		Pair<Integer, ReturnClubDto> result = gameBean.getMatchHomeClub(idc);
		return getResponse(result.getFirst(), result.getSecond());
	}
	
	@GET
	@Path("/{idc}/awayClub")
	public Response getMatchAwayClub(@PathParam("idc") long idc) {
		Pair<Integer, ReturnClubDto> result = gameBean.getMatchAwayClub(idc);
		return getResponse(result.getFirst(), result.getSecond());
	}
	
	@GET
	@Path("/{idc}/players")
	public Response getMatchPlayers(@PathParam("idc") long idc) {
		Pair<Integer, List<ReturnPlayerDto>> result = gameBean.getMatchPlayers(idc);
		return getResponse(result.getFirst(), result.getSecond());
	}
	
	@GET
	@Path("/{idc}/goals")
	public Response getMatchGoals(@PathParam("idc") long idc) {
		Pair<Integer, List<ReturnGoalDto>> result = gameBean.getMatchGoals(idc);
		return getResponse(result.getFirst(), result.getSecond());
	}
	
	@POST
	public Response createMatch(CreateGameDto gameDto) {
		Pair<Integer, String> result = gameBean.createMatch(gameDto);
		return getResponse(result.getFirst(), result.getSecond());
	}

	@PUT
	@Path("/{idc}")
	public Response updateMatch(@PathParam("idc") long idc, UpdateGameDto gameDto) {
		Pair<Integer, String> result = gameBean.updateMatch(idc, gameDto);
		return getResponse(result.getFirst(), result.getSecond());
	}

	@DELETE
	@Path("/{idc}")
	public Response deleteMatch(@PathParam("idc") long idc) {
		Pair<Integer, String> result = gameBean.deleteMatch(idc);
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
				return Response.status(Response.Status.NOT_FOUND).entity("Game with given id does not exist.").build();
			default:
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Something went wrong.").build();
		}
	}
}
