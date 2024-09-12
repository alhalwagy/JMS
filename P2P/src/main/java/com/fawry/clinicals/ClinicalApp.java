package com.fawry.clinicals;

import com.fawry.model.Patient;
import org.apache.activemq.artemis.jms.client.ActiveMQJMSConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ClinicalApp {

  public static void main(String[] args) throws NamingException, JMSException {
    InitialContext initialContext = new InitialContext();

    Queue dest = (Queue) initialContext.lookup("queue/requestQueue");
    Queue replyQueue = (Queue) initialContext.lookup("queue/replyQueue");

    try (ActiveMQJMSConnectionFactory cf = new ActiveMQJMSConnectionFactory();
         JMSContext context = cf.createContext(); ) {

      JMSProducer producer = context.createProducer();
      ObjectMessage message = context.createObjectMessage();
      Patient patient = new Patient();
      patient.setId(10);
      patient.setName("John Doe");
      patient.setInsuranceProvider("Aliens");
      patient.setCopay(100d);
      patient.setAmountToBePaid(500d);
      message.setObject(patient);

      for(int i = 0; i < 10; i++) {

        producer.send(dest, message);
      }

//      JMSConsumer consumer = context.createConsumer(replyQueue);
//      MapMessage replyMessage = (MapMessage) consumer.receive(3000);
//      System.out.println("Patient Eligibility is: " + replyMessage.getBoolean("eligible"));
    }
  }
}
