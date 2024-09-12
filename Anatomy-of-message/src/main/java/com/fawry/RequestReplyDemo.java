package com.fawry;

import org.apache.activemq.artemis.jms.client.ActiveMQJMSConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class RequestReplyDemo {

  public static void main(String[] args) throws NamingException, JMSException {
    InitialContext initialContext = new InitialContext();

    Queue Requestqueue = (Queue) initialContext.lookup("queue/requestQueue");
    Queue Replyqueue = (Queue) initialContext.lookup("queue/replyQueue");

    try (ActiveMQJMSConnectionFactory factory = new ActiveMQJMSConnectionFactory();
        JMSContext context = factory.createContext()) {

      JMSProducer producer = context.createProducer();
      TextMessage requestMessageHelloWorld =
          context.createTextMessage("Request Message Hello World");
      requestMessageHelloWorld.setJMSReplyTo(Replyqueue);
      producer.send(Requestqueue, requestMessageHelloWorld);
      System.out.println(requestMessageHelloWorld.getJMSMessageID());

      JMSConsumer consumer = context.createConsumer(Requestqueue);
      TextMessage message = (TextMessage) consumer.receive();
      System.out.println("Request message received: " + message.getText());
      System.out.println(message.getJMSMessageID());

      JMSProducer replyProducer = context.createProducer();
      TextMessage replyMessage = context.createTextMessage("you are asome boyyyyyyyy!");

      replyMessage.setJMSCorrelationID(message.getJMSMessageID());
      producer.send(requestMessageHelloWorld.getJMSReplyTo(), replyMessage);

      JMSConsumer replyConsumer = context.createConsumer(Replyqueue);

      TextMessage ReplyMessage = (TextMessage) replyConsumer.receive();
      System.out.println(replyMessage.getJMSMessageID());


      System.out.println(ReplyMessage.getText());
    }
  }
}
