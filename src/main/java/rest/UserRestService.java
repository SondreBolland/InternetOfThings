package rest;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import entities.IoTUser;
import entities.Users;

//To test rest operations use the url http://localhost:8080/Dat250Example0/rest/users



@Path("/users")
@Stateless
public class UserRestService {

	@PersistenceContext(unitName = "InternetOfThings")
	private EntityManager em;

	@GET
	@Produces(MediaType.APPLICATION_XML)

	public Response getUsers() {
		TypedQuery<IoTUser> query = em.createNamedQuery(IoTUser.FIND_ALL, IoTUser.class);
		Users users = new Users(query.getResultList());
		return Response.ok(users).build();
	}

	@GET
	@Path("{id}")
	public Response getUser(@PathParam("id") String id) {
		int idInt = Integer.parseInt(id);
		IoTUser tweet = em.find(IoTUser.class, idInt);
		if (tweet == null)
			throw new NotFoundException();
		return Response.ok(tweet).build();
	}
}
