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

@Path("/clubs")
@Consumes({ "application/json" })
@Produces({ "application/json" })

//@Consumes({ "application/xml" })
//@Produces({ "application/xml" })

public class ClubREST {

	@EJB
	ClubEJB clubBean;

	
	@GET
	public Response getClubs() {
		Pair<Integer, List<ReturnClubDto>> result = clubBean.getClubs();
		return getResponse(result.getFirst(), result.getSecond());
	}
	
	@GET
	@Path("/{idc}")
	public Response getClub(@PathParam("idc") long idc) {
		Pair<Integer, ReturnClubDto> result = clubBean.getClub(idc);
		return getResponse(result.getFirst(), result.getSecond());
	}
	
	@GET
	@Path("/{idc}/players")
	public Response getClubPlayers(@PathParam("idc") long idc) {
		Pair<Integer, List<ReturnPlayerDto>> result = clubBean.getClubPlayers(idc);
		return getResponse(result.getFirst(), result.getSecond());
	}
	
	@GET
	@Path("/{idc}/homeMatches")
	public Response getClubHomeMatches(@PathParam("idc") long idc) {
		Pair<Integer, List<ReturnGameDto>> result = clubBean.getClubHomeMatches(idc);
		return getResponse(result.getFirst(), result.getSecond());
	}
	
	@GET
	@Path("/{idc}/awayMatches")
	public Response getClubAwayMatches(@PathParam("idc") long idc) {
		Pair<Integer, List<ReturnGameDto>> result = clubBean.getClubAwayMatches(idc);
		return getResponse(result.getFirst(), result.getSecond());
	}
	
	@POST
	public Response createClub(CreateClubDto clubDto) {
		Pair<Integer, String> result = clubBean.createClub(clubDto);
		return getResponse(result.getFirst(), result.getSecond());
	}

	@PUT
	@Path("/{idc}")
	public Response updateClub(@PathParam("idc") long idc, UpdateClubDto clubDto) {
		Pair<Integer, String> result = clubBean.updateClub(idc, clubDto);
		return getResponse(result.getFirst(), result.getSecond());
	}

	@DELETE
	@Path("/{idc}")
	public Response deleteClub(@PathParam("idc") long idc) {
		Pair<Integer, String> result = clubBean.deleteClub(idc);
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
				return Response.status(Response.Status.NOT_FOUND).entity("Club with given id does not exist.").build();
			default:
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Something went wrong.").build();
		}
	}
}
