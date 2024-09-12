package com.fawry;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class FirstTopic {



    public static void main(String[] args) throws NamingException, JMSException {

        InitialContext initialContext = null;
        Connection connection = null;

        try {
            initialContext = new InitialContext();
            ConnectionFactory connectionFactory = (ConnectionFactory) initialContext.lookup("ConnectionFactory");
            connection = connectionFactory.createConnection();
            Session session = connection.createSession();
            Topic topic = (Topic) initialContext.lookup("topic/myTopic");

            MessageProducer producer = session.createProducer(topic);

            MessageConsumer consumer1=session.createConsumer(topic);
            MessageConsumer consumer2=session.createConsumer(topic);
            MessageConsumer consumer3=session.createConsumer(topic);
            TextMessage textMessage = session.createTextMessage("All Within me.");

            producer.send(textMessage);

            connection.start();

            TextMessage message1 = (TextMessage) consumer1.receive(5000);
            System.out.println("Consumer1 message: " + message1.getText());
            TextMessage message2 = (TextMessage) consumer2.receive(5000);
            System.out.println("Consumer2 message: " + message2.getText());
            TextMessage message3 = (TextMessage) consumer3.receive(5000);
            System.out.println("Consumer3 message: " + message3.getText());
        } catch (NamingException e) {
            throw new RuntimeException(e);
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }finally{
            if (initialContext != null) {
                initialContext.close();
            }
            if(connection != null){
                connection.close();
            }
        }
    }

}
