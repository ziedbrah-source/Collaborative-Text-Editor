package com.company;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.concurrent.TimeoutException;

public class Sender {
    private static final String EXCHANGE_NAME = "Messages";
    static void send (Obj o)
    {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel();) {
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
            channel.basicPublish( EXCHANGE_NAME, "", null,getByteArray(o));
            System.out.println(" [x] Sent '" + o.getMsg() + "'");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static byte[] getByteArray(Object o) throws Exception{
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ObjectOutput objOut= new ObjectOutputStream(os);
        objOut.writeObject(o);
        return os.toByteArray();
    }
}
