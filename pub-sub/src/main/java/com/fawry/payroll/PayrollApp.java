package com.fawry.payroll;

import com.fawry.model.Employee;
import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.activemq.artemis.jms.client.ActiveMQJMSConnectionFactory;

public class PayrollApp {

  public static void main(String[] args) throws NamingException, JMSException {
    InitialContext initialContext = new InitialContext();

    Topic topic = (Topic) initialContext.lookup("topic/empTopic");

    try (ActiveMQJMSConnectionFactory factory = new ActiveMQJMSConnectionFactory();
        JMSContext jmsContext = factory.createContext()) {

      JMSConsumer consumer = jmsContext.createConsumer(topic);
      Message message = consumer.receive();
      Employee employee = message.getBody(Employee.class);

      System.out.println(
          "Message Received: "
              + employee.getFirstname()
              + " "
              + employee.getLastname()
              + " "
              + employee.getEmail());
    }
  }
}
