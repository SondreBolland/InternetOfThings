package rest;

import entities.Device;
import entities.IoTUser;
import entities.Users;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

//To test rest operations use the url http://localhost:8080/Dat250Example0/rest/devices


@Path("/devices")
@Stateless
public class DeviceRestService {

	@PersistenceContext(unitName = "InternetOfThings")
	private EntityManager em;

	@GET
	@Produces(MediaType.APPLICATION_XML)
	public Response getDevices() {
		TypedQuery<Device> query = em.createNamedQuery(Device.FIND_ALL, Device.class);
		List<Device> devices = query.getResultList();
		return Response.ok(devices).build();
	}

	@GET
	@Path("{id}")
	public Response getDevice(@PathParam("id") String id) {
		int idInt = Integer.parseInt(id);
		Device device = em.find(Device.class, idInt);
		if (device == null)
			throw new NotFoundException();
		return Response.ok(device).build();
	}
}
