package com.company;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

//public class Receiver extends Thread{
public class MsgReceiver {
    private static final String EXCHANGE_NAME_MESSAGES = "Messages";

    JTextArea textArea=null;
    String id=null;

    MsgReceiver(JTextArea textArea, String id){
        this.textArea=textArea;
        this.id=id;
    }
    public void receiveMsg (HashMap<String, JLabel> map) {
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
            channel.exchangeDeclare(EXCHANGE_NAME_MESSAGES, "fanout");
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
            channel.queueBind(queueName, EXCHANGE_NAME_MESSAGES, "");
        } catch (IOException e) {
            e.printStackTrace();
        }

        DeliverCallback deliverCallback = (consumerTag , delivery) -> {
            byte[] bytearray= delivery.getBody();
            try {
                Obj o=(Obj) Utils.deseriablize(bytearray);
                if(!o.getId().equals(id)){
                    JLabel j=map.get(o.getId());

                    int startOffset = textArea.viewToModel(new Point(j.getX(), j.getY()));
                    System.out.println("Start Offset : " + startOffset);
                    System.out.println(" [x] received '" + String.valueOf(o.getMsg() + "'"));
                    textArea.insert(String.valueOf(o.getMsg()),startOffset);
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
