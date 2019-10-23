package rest;

import ejb.DeviceController;
import entities.Device;
import entities.IoTUser;
import entities.Register;
import entities.Users;

import javax.ejb.Stateless;
import javax.jms.JMSException;
import javax.naming.NamingException;
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

	DeviceController controller = new DeviceController();

	@GET
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public List<Device> getDevices() {
		return controller.getDevices();
	}

	@GET
	@Path("{id}")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Device getDevice(@PathParam("id") String id) {
		int idInt = Integer.parseInt(id);
		Device device = controller.getDevice(idInt);
		if (device == null)
			throw new NotFoundException();
		return device;
	}

	@GET
	@Path("{id}/registrations")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public List<Integer> getRegistrationsForDevice(@PathParam("id") String id) {
		int idInt = Integer.parseInt(id);
		List<Integer> registrations = controller.getRegistrationIds(idInt);
		if (registrations == null)
			throw new NotFoundException();
		return registrations;
	}

	@GET
	@Path("{id}/registrations/{rid}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Register getRegistrationsForDevice(@PathParam("id") String id, @PathParam("rid") String rid) {
		int idInt = Integer.parseInt(id);
		int ridInt = Integer.parseInt(rid);
		Register registration = controller.getRegistration(idInt, ridInt);
		if (registration == null)
			throw new NotFoundException();
		return registration;
	}

	@POST
	@Path("{id}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Register registerForDevice(@PathParam("id") String deviceId, IoTUser user) throws NamingException, JMSException {
		int idInt = Integer.parseInt(deviceId);
		Register registration = controller.registerUser(idInt, user.getUser);
		if (registration == null)
			throw new NotFoundException();
		return registration;
	}

	@GET
	@Path("registrations")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public List<Register> getAllRegistrations() {
		List<Register> registrations = controller.getAllRegistrations();
		if (registrations == null)
			throw new NotFoundException();
		return registrations;
	}
}
