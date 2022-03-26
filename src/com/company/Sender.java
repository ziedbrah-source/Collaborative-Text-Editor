package com.company;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;


public class Sender {
    private static final String EXCHANGE_NAME_MESSAGES = "Messages";
    private static final String EXCHANGE_NAME_POSITIONS = "Positions";
    private static final String EXCHANGE_NAME_PARAGRAPHS = "Paragraphs";
    static void sendMsg (Obj o)
    {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel();) {
            channel.exchangeDeclare(EXCHANGE_NAME_MESSAGES, BuiltinExchangeType.FANOUT);
            channel.basicPublish( EXCHANGE_NAME_MESSAGES, "", null,Utils.getByteArray(o));
            System.out.println(" [x] Sent '" + o.getMsg() + "'");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    static void sendPosition (Position pos)
    {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel();) {
            channel.exchangeDeclare(EXCHANGE_NAME_POSITIONS, BuiltinExchangeType.FANOUT);
            channel.basicPublish( EXCHANGE_NAME_POSITIONS, "", null,Utils.getByteArray(pos));
            System.out.println(" [x] Sent '" + pos.getId() + "'");
            System.out.println(" [x] Sent '" + pos.getX() + "'");
            System.out.println(" [x] Sent '" + pos.getY() + "'");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    static void sendParagraphInfo(Paragraph p){
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel();) {
            channel.exchangeDeclare(EXCHANGE_NAME_PARAGRAPHS, BuiltinExchangeType.FANOUT);
            channel.basicPublish( EXCHANGE_NAME_PARAGRAPHS, "", null,Utils.getByteArray(p));
            System.out.println(" [x] Sent '" + p.getOwnerId() + "'");
            System.out.println(" [x] Sent '" + p.getFirstLimit() + "'");
            System.out.println(" [x] Sent '" + p.getSecondLimit() + "'");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
