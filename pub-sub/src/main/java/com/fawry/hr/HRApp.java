package com.fawry.hr;

import com.fawry.model.Employee;
import org.apache.activemq.artemis.jms.client.ActiveMQJMSConnectionFactory;

import javax.jms.JMSContext;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class HRApp {

  public static void main(String[] args) throws NamingException {
    InitialContext initialContext = new InitialContext();

    Topic topic = (Topic) initialContext.lookup("topic/empTopic");

    try (ActiveMQJMSConnectionFactory factory = new ActiveMQJMSConnectionFactory();
        JMSContext jmsContext = factory.createContext()) {

      Employee employee = new Employee();
      employee.setId(10);
      employee.setFirstname("ahmed");
      employee.setLastname("halwagy");
      employee.setEmail("ahmed@gmail.com");
      employee.setPhone("01061175705");
      employee.setDesignation("Software Engineer");

      for (int i = 0; i < 10; i++) {
        jmsContext.createProducer().send(topic, employee);
      }
      System.out.println("Message sent successfully!");
    }
  }
}
