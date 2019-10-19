package jms;

import java.io.IOException;
import java.util.logging.Logger;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;


import com.google.gson.JsonObject;

import entities.Device;

@MessageDriven(mappedName = "jms/InternetOfThings/Topic", activationConfig = {
		@ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
		@ActivationConfigProperty(propertyName = "messageSelector", propertyValue = "topicUser = 'SubApproved'") })
public class SubscriptionIsApprovedListner implements MessageListener {

	@Override
	public void onMessage(Message message) {
		Logger logg = Logger.getLogger(getClass().getName());
		try {
			Device device = message.getBody(Device.class);
			JsonObject json = new JsonObject();
			json.addProperty("Device id", device.getId());
			json.addProperty("Device name", device.getName());
			json.addProperty("Device subscription is", "APPROVED");

			Logger logger = Logger.getLogger(getClass().getName());
			logger.info("DTWEET Subscription id: " + device.getId());
			//logger.info("DTWEET JSON: " + json);

			try {
				DweetConnection dc = new DweetConnection();
				dc.publish(json);
			} catch (IOException e) {
				logger.info("DTWEET IOExeption was thrown");
				e.printStackTrace();
			}
		} catch (JMSException e1) {
			logg.info("DTWEET JMSExeption was thrown");
			e1.printStackTrace();
		}


	}

}
