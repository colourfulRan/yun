package cqjtu.ds.yun.utils;

import java.math.BigInteger;

/**
 * 盲签名
 */
public class BlindSignature {

    public static void main(String[] args){
        System.out.println("这里是盲签名测试");
        BigInteger e=new BigInteger("32663");
        BigInteger d=new BigInteger("23");
        BigInteger n=new BigInteger("42167");

        //签名的消息
        BigInteger m=new BigInteger("123");
        //盲因子
        BigInteger factor=new BigInteger("37");

        BigInteger blindMsg=blindHideMsg(m,factor,e,n);
        BigInteger blindSig=blindSignature(blindMsg,d,n);
        BigInteger sig=blindRetriveSig(blindSig,factor,n);
        System.out.println("盲签名="+sig);
        BigInteger realSig=m.modPow(d,n);
        System.out.println("原签名="+realSig);

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
