package com.company;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class PositionReceiver {
    private static final String EXCHANGE_NAME_POSITIONS = "Positions";
    private String uniqueId=null;
    private Notepad notepad=null;
    PositionReceiver(Notepad notepad){
        this.uniqueId=notepad.getUniqueID();
        this.notepad=notepad;

    }
    public void receivePosition() {
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
            channel.exchangeDeclare(EXCHANGE_NAME_POSITIONS, "fanout");
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
            channel.queueBind(queueName, EXCHANGE_NAME_POSITIONS, "");
        } catch (IOException e) {
            e.printStackTrace();
        }

        DeliverCallback deliverCallback = (consumerTag , delivery) -> {
            byte[] bytearray= delivery.getBody();
            try {
                Position pos=(Position) Utils.deseriablize(bytearray);
                if(!pos.getId().equals(uniqueId)){

                    System.out.println(" [x] received '" + pos.getId() + "'");
                    notepad.DrawRectangleInPosition(pos);
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
