package com.company;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

public class ParagraphInfoReceiver {
    private static final String EXCHANGE_NAME_PARAGRAPHS = "Paragraphs";
    private String uniqueID=null;
    HashMap<String, Paragraph> paragraphMap=null;

    ParagraphInfoReceiver(String uniqueID, HashMap<String, Paragraph> paragraphMap){
        this.uniqueID=uniqueID;
        this.paragraphMap=paragraphMap;

    }
    public void receiveParagraph () {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = null;
        try {
            connection = factory.newConnection();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
        Channel channel = null;
        try {
            channel = connection.createChannel();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            channel.exchangeDeclare(EXCHANGE_NAME_PARAGRAPHS, "fanout");
        } catch (IOException e) {
            e.printStackTrace();
        }
        String queueName = null;
        try {
            queueName = channel.queueDeclare().getQueue();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            channel.queueBind(queueName, EXCHANGE_NAME_PARAGRAPHS, "");
        } catch (IOException e) {
            e.printStackTrace();
        }

        DeliverCallback deliverCallback = (consumerTag , delivery) -> {
            byte[] bytearray= delivery.getBody();
            try {
                Paragraph p=(Paragraph) Utils.deseriablize(bytearray);
                if(!p.getOwnerId().equals(uniqueID)){
                    System.out.println(" [x] received '" + p.getOwnerId() + "'");
                    System.out.println(" [x] received '" + p.getFirstLimit() + "'");
                    System.out.println(" [x] received '" + p.getSecondLimit() + "'");

                    if(paragraphMap.containsKey(p.getOwnerId())){
                        p=paragraphMap.get(p.getOwnerId());
                        p.setFirstLimit(p.getFirstLimit());
                        p.setSecondLimit(p.getSecondLimit());
                    }else{
                        paragraphMap.put(uniqueID,p);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }



        };

        try {
            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
