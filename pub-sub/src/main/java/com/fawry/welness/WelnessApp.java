package com.fawry.welness;

import com.fawry.model.Employee;
import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.activemq.artemis.jms.client.ActiveMQJMSConnectionFactory;

public class WelnessApp {

  public static void main(String[] args) throws NamingException, JMSException {
    InitialContext initialContext = new InitialContext();

    Topic topic = (Topic) initialContext.lookup("topic/empTopic");
    try (ActiveMQJMSConnectionFactory factory = new ActiveMQJMSConnectionFactory();
        JMSContext jmsContext = factory.createContext()) {

      JMSConsumer consumer = jmsContext.createSharedConsumer(topic, "sharedConsumer");
      JMSConsumer consumer2 = jmsContext.createSharedConsumer(topic, "sharedConsumer");

      for (int i = 0; i < 10; i = i + 2) {

        Message message1 = consumer.receive();
        Employee employee = message1.getBody(Employee.class);

        System.out.println(
            "Message Received to consumer1: "
                + employee.getFirstname()
                + " "
                + employee.getLastname()
                + " "
                + employee.getEmail());

        Message message2 = consumer2.receive();
        Employee employee2 = message2.getBody(Employee.class);
        System.out.println(
            "Message Received to consumer2: "
                + employee2.getFirstname()
                + " "
                + employee2.getLastname()
                + " "
                + employee2.getEmail());
      }
    }
  }
}
