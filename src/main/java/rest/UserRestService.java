package rest;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import ejb.UserController;
import entities.IoTUser;
import entities.Users;

import java.util.List;

//To test rest operations use the url http://localhost:8080/InternetOfThings/rest/users

@Path("/users")
@Stateless
public class UserRestService {

	UserController controller = new UserController();

	@PersistenceContext(unitName = "InternetOfThings")
	private EntityManager em;

	@GET
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public List<IoTUser> getUsers() {
		List<IoTUser> users = controller.getUsers();
		return users;
	}

	@GET
	@Path("{id}")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public IoTUser getUser(@PathParam("id") String id) {
		int idInt = Integer.parseInt(id);
		IoTUser user = controller.getUser();
		if (user == null)
			throw new NotFoundException();
		return user;
	}
}
