package cqjtu.ds.yun.utils;

import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 文件辅助类
 */
public class FileUtil {

    //当前目录路径
    public static String currentWorkDir=System.getProperty("user.dir"+"\\");
    //public static List<String> partHashs;

    /**
     * 删除文件
     */
    public static boolean delete(String fileName){
        boolean result=false;
        File f=new File(fileName);
        if(f.exists()){
            result=f.delete();
        }else {
            result=true;
        }
        return result;
    }

    /**
     * 读取文件内容
     */
    public static String read(String fileName) throws IOException {
        File f=new File(fileName);
        FileInputStream fs=new FileInputStream(f);
        String result=null;
        byte[] b=new byte[fs.available()];
        fs.read(b);
        fs.close();
        result=new String(b);
        return result;
    }

    /**
     * 写文件
     */
    public static boolean write(String fileName,String fileContent)throws IOException{
        boolean result=false;
        File f=new File(fileName);
        FileOutputStream fs=new FileOutputStream(f);
        byte[] b=fileContent.getBytes();
        fs.write(b);
        fs.flush();
        fs.close();
        result=true;
        return result;
    }

    /**
     * 拆分文件
     * @param file  待拆分的完整文件
     * @param count  按多少块拆分
     * @return   拆分后的文件名列表
     */
    public static List<String> splitBySize(File file,int count) throws IOException{
        List<String> partHashs= new ArrayList<>();
        int byteSize= (int) Math.ceil(file.length()/count);
        ThreadPoolExecutor threadPool=new ThreadPoolExecutor(count,count*3,1, TimeUnit.SECONDS,new ArrayBlockingQueue<Runnable>(count*2));
        RandomAccessFile rFile=new RandomAccessFile(file,"r");//以只读方式;
        for (int i=0;i<count;i++){
            try {
                byte[] b=new byte[byteSize];
                rFile.seek(i*byteSize);//移动指针到每“段”开头
                rFile.readFully(b);
                String hash=SHA256Utils.getSHA256(b);
                partHashs.add(hash);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return partHashs;
    }



}
