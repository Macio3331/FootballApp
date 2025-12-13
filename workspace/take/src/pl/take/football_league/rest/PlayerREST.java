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
import pl.take.football_league.ejb.*;

@Path("/players")
@Consumes({ "application/json" })
@Produces({ "application/json" })

//@Consumes({ "application/xml" })
//@Produces({ "application/xml" })
public class PlayerREST {
	
	@EJB
	PlayerEJB playerBean;

	
	@GET
	public Response getPlayers() {
		Pair<Integer, List<ReturnPlayerDto>> result = playerBean.getPlayers();
		return getResponse(result.getFirst(), result.getSecond());
	}
	
	@GET
	@Path("/{idc}")
	public Response getPlayer(@PathParam("idc") long idc) {
		Pair<Integer, ReturnPlayerDto> result = playerBean.getPlayer(idc);
		return getResponse(result.getFirst(), result.getSecond());
	}
	
	@GET
	@Path("/{idc}/club")
	public Response getPlayerClub(@PathParam("idc") long idc) {
		Pair<Integer, ReturnClubDto> result = playerBean.getPlayerClub(idc);
		return getResponse(result.getFirst(), result.getSecond());
	}
	
	@GET
	@Path("/{idc}/matches")
	public Response getPlayerMatches(@PathParam("idc") long idc) {
		Pair<Integer, List<ReturnGameDto>> result = playerBean.getPlayerMatches(idc);
		return getResponse(result.getFirst(), result.getSecond());
	}
	
	@GET
	@Path("/{idc}/goals")
	public Response getPlayerGoals(@PathParam("idc") long idc) {
		Pair<Integer, List<ReturnGoalDto>> result = playerBean.getPlayerGoals(idc);
		return getResponse(result.getFirst(), result.getSecond());
	}
	
	@GET
	@Path("/{idc}/assists")
	public Response getPlayerAssists(@PathParam("idc") long idc) {
		Pair<Integer, List<ReturnGoalDto>> result = playerBean.getPlayerAssists(idc);
		return getResponse(result.getFirst(), result.getSecond());
	}
	
	@POST
	public Response createPlayer(CreatePlayerDto playerDto) {
		Pair<Integer, String> result = playerBean.createPlayer(playerDto);
		return getResponse(result.getFirst(), result.getSecond());
	}

	@PUT
	@Path("/{idc}")
	public Response updatePlayer(@PathParam("idc") long idc, UpdatePlayerDto playerDto) {
		Pair<Integer, String> result = playerBean.updatePlayer(idc, playerDto);
		return getResponse(result.getFirst(), result.getSecond());
	}

	@DELETE
	@Path("/{idc}")
	public Response deletePlayer(@PathParam("idc") long idc) {
		Pair<Integer, String> result = playerBean.deletePlayer(idc);
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
