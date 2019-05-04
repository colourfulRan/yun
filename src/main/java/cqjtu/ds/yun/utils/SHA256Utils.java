package cqjtu.ds.yun.utils;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256Utils {

    /**
     *利用java原生的类实现SHA256
     */
    public static String getSHA256(byte[] buffer){
        String value=null;
        try{
            MessageDigest sha=MessageDigest.getInstance("SHA-256");
            sha.update(buffer,0,buffer.length);
            byte[] b=sha.digest();
            //signum 表示的是符号，1 表示正数；0 表示 0；-1 表示负数
            BigInteger bi=new BigInteger(1,b);
            value=bi.toString(16);
        }catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return value;
    }
}
