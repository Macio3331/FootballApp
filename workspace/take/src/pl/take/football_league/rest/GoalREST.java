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
import pl.take.football_league.ejb.GoalEJB;

@Path("/goals")
@Consumes({ "application/json" })
@Produces({ "application/json" })

//@Consumes({ "application/xml" })
//@Produces({ "application/xml" })
public class GoalREST {

	@EJB
	GoalEJB goalBean;

	
	@GET
	public Response getGoals() {
		Pair<Integer, List<ReturnGoalDto>> result = goalBean.getGoals();
		return getResponse(result.getFirst(), result.getSecond());
	}
	
	@GET
	@Path("/{idc}")
	public Response getGoal(@PathParam("idc") long idc) {
		Pair<Integer, ReturnGoalDto> result = goalBean.getGoal(idc);
		return getResponse(result.getFirst(), result.getSecond());
	}
	
	@GET
	@Path("/{idc}/match")
	public Response getGoalMatch(@PathParam("idc") long idc) {
		Pair<Integer, ReturnGameDto> result = goalBean.getGoalMatch(idc);
		return getResponse(result.getFirst(), result.getSecond());
	}
	
	@GET
	@Path("/{idc}/scorer")
	public Response getGoalScorer(@PathParam("idc") long idc) {
		Pair<Integer, ReturnPlayerDto> result = goalBean.getGoalScorer(idc);
		return getResponse(result.getFirst(), result.getSecond());
	}
	
	@GET
	@Path("/{idc}/assistant")
	public Response getGoalAssistant(@PathParam("idc") long idc) {
		Pair<Integer, ReturnPlayerDto> result = goalBean.getGoalAssistant(idc);
		return getResponse(result.getFirst(), result.getSecond());
	}
	
	@POST
	public Response createGoal(CreateGoalDto goalDto) {
		Pair<Integer, String> result = goalBean.createGoal(goalDto);
		return getResponse(result.getFirst(), result.getSecond());
	}

	@PUT
	@Path("/{idc}")
	public Response updateGoal(@PathParam("idc") long idc, UpdateGoalDto goalDto) {
		Pair<Integer, String> result = goalBean.updateGoal(idc, goalDto);
		return getResponse(result.getFirst(), result.getSecond());
	}

	@DELETE
	@Path("/{idc}")
	public Response deleteGoal(@PathParam("idc") long idc) {
		Pair<Integer, String> result = goalBean.deleteGoal(idc);
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
				return Response.status(Response.Status.NOT_FOUND).entity("Goal with given id does not exist.").build();
			default:
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Something went wrong.").build();
		}
	}
}
