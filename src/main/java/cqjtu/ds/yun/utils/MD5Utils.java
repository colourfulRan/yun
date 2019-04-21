package cqjtu.ds.yun.utils;


import org.springframework.stereotype.Component;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5生成文件校验值
 */
@Component
public class MD5Utils {

    public static String getMD5ByFile(File file){
        String value=null;
        FileInputStream in=null;
        try{
            in=new FileInputStream(file);
            //将节点中从position开始的size个字节映射到返回的MappedByteBuffer中,得到的镜像只能读不能写
            byte[] buffer=new byte[8192];
            int length=0;
            MessageDigest md5=MessageDigest.getInstance("MD5");
            //分多次将一个文件读入，对大型文件而言，占用内存较少
            while ((length=in.read(buffer))!=-1){
                md5.update(buffer,0,length);
            }
            byte[] b=md5.digest();
            //signum 表示的是符号，1 表示正数；0 表示 0；-1 表示负数
            BigInteger bi=new BigInteger(1,b);
            value=bi.toString(16);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }finally {
            if (null!=in){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return value;
    }

}
