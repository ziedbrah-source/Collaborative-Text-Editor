package com.company;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ClearReceiver {
    private static final String EXCHANGE_NAME_CLEAR = "Clear";
    private String uniqueId=null;
    private Window window=null;
    ClearReceiver(Window notepad){
        this.uniqueId=notepad.getUniqueID();
        this.window=notepad;

    }
    public void receiveClear() {
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
            channel.exchangeDeclare(EXCHANGE_NAME_CLEAR, "fanout");
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
            channel.queueBind(queueName, EXCHANGE_NAME_CLEAR, "");
        } catch (IOException e) {
            e.printStackTrace();
        }

        DeliverCallback deliverCallback = (consumerTag , delivery) -> {
            byte[] bytearray= delivery.getBody();
            try {
                Clear pos=(Clear) Utils.deseriablize(bytearray);
                if(!pos.getId().equals(uniqueId)){

                    window.getJt().setEnabled(true);
                    window.getJt().getDocument().addDocumentListener(window);
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
