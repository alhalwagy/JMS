package com.fawry;

import org.apache.activemq.artemis.jms.client.ActiveMQJMSConnectionFactory;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.List;

public class MessagePriority {

  public static void main(String[] args) throws NamingException {
    InitialContext ctx = new InitialContext();
    Queue queue = (Queue) ctx.lookup("queue/myQueue");

    try (ActiveMQJMSConnectionFactory factory = new ActiveMQJMSConnectionFactory();
        JMSContext context = factory.createContext()) {
      JMSProducer producer = context.createProducer();
      String[] messages = new String[5];
      messages[0] = "message1";
      messages[1] = "message2";
      messages[2] = "message3";
      messages[3] = "message4";
      messages[4] = "message5";

      producer.setPriority(3);
      producer.send(queue, messages[0]);

      producer.setPriority(1);
      producer.send(queue, messages[1]);

      producer.setPriority(9);
      producer.send(queue, messages[2]);

      JMSConsumer consumer = context.createConsumer(queue);
      for (int i = 0; i < 5; i++) {

        String s = consumer.receiveBody(String.class);
        System.out.println(s);
      }
    }
  }
}
