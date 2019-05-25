package cqjtu.ds.yun.utils;


import javax.crypto.Cipher;
import java.io.*;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class RSAUtils {

    //定义加密方式
    private static final String KEY_RSA="RSA";
    //定义公钥关键词
    private static final String KEY_RSA_PUBLICKEY="RSAPublicKey";
    //定义私钥关键词
    private static final String KEY_RSA_PRIVATEKEY="RSAPrivateKey";
    //定义签名算法
    private static final String KEY_RSA_SIGNATURE="MD5withRSA";

    //指定公钥存放文件和私钥存放文件
    private static String PUBLIC_KEY_FILE="src/public.key";
    private static String PRIVATE_KEY_FILE="src/private.key";

    /**
     * 生成公私密钥对
     */
    public static Map<String,Object> init(){
        Map<String,Object> map=null;
        try {
            KeyPairGenerator generator=KeyPairGenerator.getInstance(KEY_RSA);
            //设置密钥对的bit数，越大越安全，但速度减慢，一般使用512或1024
            generator.initialize(1024);
            KeyPair keyPair=generator.generateKeyPair();
            //获取公钥
            RSAPublicKey publicKey= (RSAPublicKey) keyPair.getPublic();
            //获取私钥
            RSAPrivateKey privateKey= (RSAPrivateKey) keyPair.getPrivate();
            //将生成的密钥写入文件
            ObjectOutputStream output1=new ObjectOutputStream(new FileOutputStream(PUBLIC_KEY_FILE));
            ObjectOutputStream output2=new ObjectOutputStream(new FileOutputStream(PRIVATE_KEY_FILE));
            output1.writeObject(publicKey);
            output2.writeObject(privateKey);
            output1.close();
            output2.close();
            //将密钥对封装为Map
            map=new HashMap<String,Object>();
            map.put(KEY_RSA_PUBLICKEY,publicKey);
            map.put(KEY_RSA_PRIVATEKEY,privateKey);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return map;
    }

    /**
     * 获取Base64编码的公钥字符串
     */
    public static String getPublicKey(Map<String,Object> map){
        String str="";
        Key key= (Key) map.get(KEY_RSA_PUBLICKEY);
        str=encryptBase64(key.getEncoded());
        return str;
    }

    /**
     *获取Base64编码的私钥字符串
     */
    public static String getPrivateKey(Map<String,Object> map){
        String str="";
        Key key= (Key) map.get(KEY_RSA_PRIVATEKEY);
        str=encryptBase64(key.getEncoded());
        return str;
    }

    /**
     * Base64 编码
     * @param encoded   需要Base64编码的字节数组
     * @return  字符串
     */
    private static String encryptBase64(byte[] encoded) {
        return new String(Base64.getEncoder().encode(encoded));
    }

    /**
     * Base64 解码
     * @param key   需要Base64解码的字符串
     * @return  字节数组
     */
    private static byte[] decryptBase64(String key){
        return Base64.getDecoder().decode(key);
    }

    /**
     * 公钥加密
     */
    public static String encryptByPublic(String encryptingStr,String publicKeyStr){
        //将公钥由字符串转换为UTF-8格式的字节数组
        byte[] publicKeyBytes=decryptBase64(publicKeyStr);
        //获得公钥
        X509EncodedKeySpec keySpec=new X509EncodedKeySpec(publicKeyBytes);
        //取得待加密数据
        try {
            byte[] data=encryptingStr.getBytes("UTF-8");
            KeyFactory factory=KeyFactory.getInstance(KEY_RSA);
            PublicKey publicKey=factory.generatePublic(keySpec);
            //对数据加密
            Cipher cipher=Cipher.getInstance(factory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE,publicKey);
            //返回加密后由Base64编码的加密信息
            return encryptBase64(cipher.doFinal(data));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 私钥解密
     */
    public static String decryptByPrivate(String encryptedStr,String privateKeyStr){
        //对私钥解密
        byte[] pricateKeyBytes=decryptBase64(privateKeyStr);
        //获得私钥
        PKCS8EncodedKeySpec keySpec=new PKCS8EncodedKeySpec(pricateKeyBytes);
        //获得待解密数据
        byte[] data=decryptBase64(encryptedStr);
        try {
            KeyFactory factory=KeyFactory.getInstance(KEY_RSA);
            PrivateKey privateKey=factory.generatePrivate(keySpec);
            //对数据解密
            Cipher cipher=Cipher.getInstance(factory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE,privateKey);
            //返回UTF-8编码的解密信息
            return new String(cipher.doFinal(data),"UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 用私钥对加密数据进行签名
     */
    public static String sign(String encryptedStr,String privateKey){
        String str="";
        //将私钥加密数据字符串转换为字节数组
        byte[] data=encryptedStr.getBytes();
        //解密由base64编码的私钥
        byte[] bytes=decryptBase64(privateKey);
        //构造PKCS8EncodedKeySpec对象
        PKCS8EncodedKeySpec pkcs=new PKCS8EncodedKeySpec(bytes);
        //指定加密算法
        try {
            KeyFactory factory=KeyFactory.getInstance(KEY_RSA);
            //取私钥对象
            PrivateKey key=factory.generatePrivate(pkcs);
            //用私钥对信息生成数字签名
            Signature signature=Signature.getInstance(KEY_RSA_SIGNATURE);
            signature.initSign(key);
            signature.update(data);
            str=encryptBase64(signature.sign());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 校验数字签名
     * @param encryptedStr
     * @param publicKey
     * @param sign
     * @return 校验成功返回true，失败返回false
     */
    public static boolean verify(String encryptedStr,String publicKey,String sign){
        boolean flag=false;
        //将私钥加密数据字符串转换为字节数组
        byte[] data=encryptedStr.getBytes();
        //解密由base64编码的公钥
        byte[] bytes=decryptBase64(publicKey);
        //构造X509EncodedKeySpec对象
        X509EncodedKeySpec keySpec=new X509EncodedKeySpec(bytes);
        //指定加密算法
        try {
            KeyFactory factory=KeyFactory.getInstance(KEY_RSA);
            //取公钥对象
            PublicKey key=factory.generatePublic(keySpec);
            //用公钥验证数字签名
            Signature signature=Signature.getInstance(KEY_RSA_SIGNATURE);
            signature.initVerify(key);
            signature.update(data);
            flag=signature.verify(decryptBase64(sign));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 私钥加密
     */
    public static String encryptByPrivate(String encryptingStr,String privateKeyStr){
        byte[] privateKeyBytes=decryptBase64(privateKeyStr);
        //获得私钥
        PKCS8EncodedKeySpec keySpec=new PKCS8EncodedKeySpec(privateKeyBytes);
        //取得待加密数据
        try {
            byte[] data=encryptingStr.getBytes("UTF-8");
            KeyFactory factory=KeyFactory.getInstance(KEY_RSA);
            PrivateKey privateKey=factory.generatePrivate(keySpec);
            //对数据加密
            Cipher cipher=Cipher.getInstance(factory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE,privateKey);
            //返回加密后由Base64编码的加密信息
            return encryptBase64(cipher.doFinal(data));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *公钥解密
     */
    public static String decryptByPublic(String encryptedStr,String publicKeyStr){
        //对公钥解密
        byte[] publicKeyBytes=decryptBase64(publicKeyStr);
        //取得公钥
        X509EncodedKeySpec keySpec=new X509EncodedKeySpec(publicKeyBytes);
        //取得待解密数据
        byte[] data=decryptBase64(encryptedStr);
        try {
            KeyFactory factory=KeyFactory.getInstance(KEY_RSA);
            PublicKey publicKey=factory.generatePublic(keySpec);
            //对数据解密
            Cipher cipher=Cipher.getInstance(factory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE,publicKey);
            //返回UTF-8编码的解密信息
            return new String(cipher.doFinal(data),"UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

     public static void main(String args[]){
        Map<String,Object> map;
        map=RSAUtils.init();
        System.out.println("公钥："+RSAUtils.getPublicKey(map));
        System.out.println("私钥："+RSAUtils.getPrivateKey(map));

        String publicKey=RSAUtils.getPublicKey(map);
        String privateKey=RSAUtils.getPrivateKey(map);
        String str="你好，RSA";
        //公钥加密，私钥解密
        String enStr=RSAUtils.encryptByPublic(str,publicKey);
        System.out.println("公钥加密后："+enStr);
        String deStr=RSAUtils.decryptByPrivate(enStr,privateKey);
        System.out.println("私钥解密后："+deStr);
        //私钥加密，公钥解密
         String enstr=RSAUtils.encryptByPrivate(str,privateKey);
         System.out.println("私钥加密后："+enstr);
         String destr=RSAUtils.decryptByPublic(enstr,publicKey);
         System.out.println("公钥解密后："+destr);
        /*//产生签名
        String sign=sign(str,privateKey);
        System.out.println("签名："+sign);
        //验证签名
        boolean status=verify(str,publicKey,sign);
        System.out.println("状态："+status);*/
    }
}
