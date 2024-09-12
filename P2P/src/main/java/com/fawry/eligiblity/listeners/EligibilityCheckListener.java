package com.fawry.eligiblity.listeners;

import com.fawry.model.Patient;
import org.apache.activemq.artemis.jms.client.ActiveMQJMSConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class EligibilityCheckListener implements MessageListener {
  @Override
  public void onMessage(Message message) {

    ObjectMessage objectMessage = (ObjectMessage) message;
    try (ActiveMQJMSConnectionFactory cf = new ActiveMQJMSConnectionFactory();
        JMSContext jmsContext = cf.createContext()) {
      InitialContext initialContext = new InitialContext();
      Queue replyQueue = (Queue) initialContext.lookup("queue/replyQueue");
      MapMessage replyMessage = jmsContext.createMapMessage();

      Patient patient = (Patient) objectMessage.getObject();

      String insuranceProvider = patient.getInsuranceProvider();
      System.out.println("Insurance Provider: " + insuranceProvider);
      if (insuranceProvider.equals("Aliens") || insuranceProvider.equals("UN")) {

        System.out.println("Patient Copay: "+patient.getCopay());
        System.out.println("Patient Copay: "+patient.getCopay());
        if (patient.getCopay() < 40 && patient.getAmountToBePaid() < 1000) {
          replyMessage.setBoolean("eligible", true);
        } else {
          replyMessage.setBoolean("eligible", false);
        }
      }
      JMSProducer producer = jmsContext.createProducer();
      producer.send(replyQueue, replyMessage);

    } catch (JMSException e) {
      throw new RuntimeException(e);
    } catch (NamingException e) {
      throw new RuntimeException(e);
    }
  }
}
