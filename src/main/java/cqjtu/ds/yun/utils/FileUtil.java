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


    /**
     * 拆分文件
     * @param file  待拆分的完整文件
     * @param count  按多少块拆分
     * @return   拆分后的文件名列表
     */
    public static List<String> splitBySize(File file,int count) throws IOException{
        List<String> partHashs= new ArrayList<>();
        int byteSize= (int) Math.ceil(file.length()/count);
     //   ThreadPoolExecutor threadPool=new ThreadPoolExecutor(count,count*3,1, TimeUnit.SECONDS,new ArrayBlockingQueue<Runnable>(count*2));
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


    /**
     *拆分文件
     * @param file     待拆分的文件
     * @param count    拆分为的块数
     * @param challenges  挑战块
     * @return         对应挑战块的hash值
     */
    public static List<String> splitByChallenges(File file,int count,List<Integer> challenges){
        List<String> partHashs= new ArrayList<>();
        int byteSize= (int) Math.ceil(file.length()/count);
        int k=1;
        try {
            RandomAccessFile rFile=new RandomAccessFile(file,"r");//以只读方式;
            for (Integer i:challenges){
                rFile.seek(i*byteSize);//移动指针到每“段”开头
                byte[] b=new byte[byteSize];
                rFile.readFully(b);
                String hash=SHA256Utils.getSHA256(b);
                partHashs.add(hash);
                k++;
                if (k==challenges.size()){
                    break;
                }
            }
        }catch (IOException e) {
        e.printStackTrace();
    }
        return partHashs;
    }

    //创建固定大小的文件
    public static void create(File file, long length) throws IOException{
     //   long start = System.currentTimeMillis();
        RandomAccessFile r = null;
        try {
            r = new RandomAccessFile(file, "rw");
            r.setLength(length);
        } finally{
            if (r != null) {
                try {
                    r.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
       // long end = System.currentTimeMillis();
       // System.out.println(end-start);
    }


    public static void main(String[] args){
        File file1=new File("G:\\毕业设计\\测试文件\\64KB.txt");
        File file2=new File("G:\\毕业设计\\测试文件\\256KB.txt");
        File file3=new File("G:\\毕业设计\\测试文件\\1M.txt");
        File file4=new File("G:\\毕业设计\\测试文件\\4M.txt");
        File file5=new File("G:\\毕业设计\\测试文件\\16M.txt");
        File file6=new File("G:\\毕业设计\\测试文件\\64M.txt");
        File file7=new File("G:\\毕业设计\\测试文件\\256M.txt");
        File file8=new File("G:\\毕业设计\\测试文件\\1G.txt");
        try {
            create(file1,1024*64L);
            create(file2,1024*256L);
            create(file3,1024*1024L);
            create(file4,1024*1024*4L);
            create(file5,1024*1024*16L);
            create(file6,1024*1024*64L);
            create(file7,1024*1024*256L);
            create(file8,1024*1024*1024L);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
