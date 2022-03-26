package com.company;

import java.io.*;

public class Utils {

    public static Object deseriablize (byte[] byteArray) throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream(byteArray);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }
    public static byte[] getByteArray(Object o) throws Exception{
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ObjectOutput objOut= new ObjectOutputStream(os);
        objOut.writeObject(o);
        return os.toByteArray();
    }
}
