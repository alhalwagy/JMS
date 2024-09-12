package com.fawry;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Enumeration;

public class QueueBrowserDemo {

  public static void main(String[] args) {
    InitialContext initialContext = null;
    Connection connection = null;
    try {
      initialContext = new InitialContext();
      ConnectionFactory connectionFactory =
          (ConnectionFactory) initialContext.lookup("ConnectionFactory");
      connection = connectionFactory.createConnection();
      Session session = connection.createSession();
      Queue queue = (Queue) initialContext.lookup("queue/myQueue");
      MessageProducer producer = session.createProducer(queue);
      TextMessage textMessage1 =
          session.createTextMessage("I'm the First Creator of the message. My name is Alhalwagy.");
      TextMessage textMessage2 =
          session.createTextMessage("I'm the SecondCreator of the message. My name is Alhalwagy.");

      producer.send(textMessage1);
      producer.send(textMessage2);
      System.out.println("Messages sent successfully: ");

      QueueBrowser queueBrowser = session.createBrowser(queue);
      Enumeration messagesEnum = queueBrowser.getEnumeration();
      while (messagesEnum.hasMoreElements()) {
        TextMessage message = (TextMessage) messagesEnum.nextElement();
        System.out.println("Browsing" + message.getText());
      }
      MessageConsumer consumer = session.createConsumer(queue);
      connection.start();
      TextMessage messageReceived1 = (TextMessage) consumer.receive(5000);
      System.out.println("Message Received Successfully: ");
      TextMessage messageReceived2 = (TextMessage) consumer.receive(5000);
      System.out.println("Message Received Successfully: ");
    } catch (NamingException e) {
      throw new RuntimeException(e);
    } catch (JMSException e) {
      throw new RuntimeException(e);
    } finally {
      if (initialContext != null) {
        try {
          initialContext.close();
        } catch (NamingException e) {
          throw new RuntimeException(e);
        }
      }

      if (connection != null) {
        try {
          connection.close();
        } catch (JMSException e) {
          throw new RuntimeException(e);
        }
      }
    }
  }
}
