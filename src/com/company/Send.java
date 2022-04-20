package com.company;



import com.rabbitmq.client.*;

import java.util.HashMap;

public class Send {

    private static final String EXCHANGE_NAME = "logs";
    private static final String EXCHANGE_NAME_POSITIONS = "Positions";
    private static final String EXCHANGE_NAME_CLEAR = "Clear";
    public static void envoyer(String msg,String queueName,int startRange,int endRange,String type) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
            HashMap<String,Object> ranges = new HashMap<String,Object>();
            ranges.put("startRange",startRange);
            ranges.put("endRange",endRange);
            ranges.put("type",type);
            ranges.put("queue",queueName);
            System.out.println("Ranges: Position "+startRange+" to "+endRange+"\ntype: "+type);

            channel.basicPublish(EXCHANGE_NAME, "", new AMQP.BasicProperties.Builder().headers(ranges).build(), msg.getBytes("UTF-8"));
            System.out.println("Sent '" + msg + "' from "+queueName);
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
    static void sendClear (Clear cl)
    {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel();) {
            channel.exchangeDeclare(EXCHANGE_NAME_CLEAR, BuiltinExchangeType.FANOUT);
            channel.basicPublish( EXCHANGE_NAME_CLEAR, "", null,Utils.getByteArray(cl));
            System.out.println(" [x] Sent '" + cl.getId() + "'");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}