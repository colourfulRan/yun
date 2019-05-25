package cqjtu.ds.yun.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.math.BigInteger;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

/**
 * 盲签名
 */
public class BlindSignature {

    public static void main(String[] args){
        /*Map<String,Object> map;
        map=RSAUtils.init();*/
        //读出文件中的公钥对象
        try {
            ObjectInputStream ois1=new ObjectInputStream(new FileInputStream("src/public.key"));
            ObjectInputStream ois2=new ObjectInputStream(new FileInputStream("src/private.key"));
            RSAPublicKey pubKey= (RSAPublicKey) ois1.readObject();
            RSAPrivateKey priKey= (RSAPrivateKey) ois2.readObject();
            ois1.close();
            ois2.close();
            BigInteger e=pubKey.getPublicExponent();
            BigInteger d=priKey.getPrivateExponent();
            BigInteger n=pubKey.getModulus();
            BigInteger n2=priKey.getModulus();
            System.out.println("公钥："+e);
            System.out.println("私钥："+d);
            System.out.println("n1："+n);
            System.out.println("n2："+n2);
            // BigInteger e=new BigInteger("32663");
            // BigInteger d=new BigInteger("23");
            // BigInteger n=new BigInteger("42167");

            //签名的消息
            BigInteger m=new BigInteger("275d6479559a7339bf933454874241f1",16);
            System.out.println("原文="+m);
            //盲因子
            BigInteger factor=new BigInteger("317127853611181057365928393691525761187227861954");

            BigInteger blindMsg=blindHideMsg(m,factor,e,n);
            BigInteger blindSig=blindSignature(blindMsg,d,n);
            BigInteger sig=blindRetriveSig(blindSig,factor,n);
            System.out.println("盲签名="+sig);
            BigInteger realSig=m.modPow(d,n);
            System.out.println("原签名="+realSig);
            BigInteger deInt=sig.modPow(e,n);
            System.out.println("原文="+deInt);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }



    /**盲化**/
    public static BigInteger blindHideMsg(BigInteger msg,BigInteger factor,BigInteger e,BigInteger n){
        BigInteger hideMsg=msg.multiply(factor.modPow(e,n)).mod(n);
        return hideMsg;
    }

    /**签名**/
    public static BigInteger blindSignature(BigInteger blindMsg,BigInteger d,BigInteger n){
        BigInteger blindSig=blindMsg.modPow(d,n);
        return blindSig;
    }

    /**解盲得到签名**/
    public static BigInteger blindRetriveSig(BigInteger blindSig,BigInteger factor,BigInteger n){
        BigInteger signature=blindSig.multiply(factor.modInverse(n)).mod(n);
        return signature;
    }
}
