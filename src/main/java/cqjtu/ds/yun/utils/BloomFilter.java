package cqjtu.ds.yun.utils;


import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.util.BitSet;
import java.util.concurrent.atomic.AtomicInteger;

public class BloomFilter implements Serializable{

    //解决不同的版本之间的串行话问题
    //Java的序列化机制是通过在运行时判断类的serialVersionUID来验证版本一致性的。在进行反序列化时，JVM会把传来
    // 的字节流中的serialVersionUID与本地相应实体（类）的serialVersionUID进行比较，如果相同就认为是一致的，可以进行反序 列化，否则就会出现序列化版本不一致的异常。
    private static final long serialVersionUID=-5221305273707291280L;
    private final int[] seeds;
    private final int size;
    //BitSet中数组大小会随需要增加;存储散列后的结果
    private final BitSet notebook;
    @Getter
    private final MisjudgmentRate rate;
    //AtomicInteger:一个提供原子操作的Integer的类,通过线程安全的方式操作加减
    private final AtomicInteger useCount=new AtomicInteger(0);
    private final Double autoClearRate;

    /**
     * 默认中等程序的误判率：MisjudgmentRate.MIDDLE 以及不自动清空数据（性能会有少许提升）
     * @param dataCount  预期处理的数据规模，如预期用于处理1百万数据的查重，这里填写1000000
     */
    public BloomFilter(int dataCount){
        this(MisjudgmentRate.MIDDLE,dataCount,null);
    }

    /**
     *
     * @param rate        一个枚举类型的误判率
     * @param dataCount   预期处理的数据规模
     * @param autoClearRate
     *        自动清空过滤器内部信息的使用比率，传null表示不会自动清理，当过滤器使用率达到100%时，则无论传入什么数据，都认为数据已经存在了
     *        当希望过滤器使用率达到100%时自动清空重新使用，则传入0.8
     */
    public BloomFilter(MisjudgmentRate rate,int dataCount,Double autoClearRate){
        long bitSize=rate.seeds.length*dataCount;
        if(bitSize<0 || bitSize>Integer.MAX_VALUE){
            throw new RuntimeException("位数太大溢出了，请降低误判率或者降低数据大小");
        }
        this.rate=rate;
        seeds=rate.seeds;
        size= (int) bitSize;
        notebook=new BitSet(size);
        this.autoClearRate=autoClearRate;
    }

    private int hash(String data, int seed) {
        char[] value=data.toCharArray();
        int hash=0;
        if(value.length>0){
            for(int i=0;i<value.length;i++){
                hash=i*hash+value[i];
            }
        }
        hash=hash*seed%size;
        //防止溢出变成负数
        return Math.abs(hash);
    }

    public void setTrue(int index) {
        //自增
        useCount.incrementAndGet();
        //将指定索引处的位设置为指定的值
        notebook.set(index,true);
    }

    public void add(String data){
        checkNeedClear();

        for(int i=0;i<seeds.length;i++){
            int index=hash(data,seeds[i]);
            setTrue(index);
        }
    }

    public boolean check(String data){
        for(int i=0;i<seeds.length;i++){
            int index=hash(data,seeds[i]);
            //该位为false，表明不存在相同的
            if(!notebook.get(index)){
                return false;
            }
        }
        return true;
    }

    /**
     * 如果不存在就进行记录并返回false，如果存在就返回true
     */
    public boolean addIfNotExist(String data) {
        checkNeedClear();
        int[] indexs = new int[seeds.length];
        //先假定存在
        boolean exist = true;
        int index;

        for (int i = 0; i < seeds.length; i++) {
            indexs[i] = index = hash(data, seeds[i]);
          //  System.out.print(index+"   ");
            if (exist) {
                if (!notebook.get(index)) {
                    //只要有一个不存在，就可以认为整个字符串都是第一次出现的
                    exist = false;
                    //补充之前的信息
                    for (int j = 0; j <= i; j++) {
                        setTrue(indexs[j]);

                    }
                }
            } else {
                setTrue(index);
            }
        }
        return exist;
    }


    private void checkNeedClear() {
        if(autoClearRate!=null){
            if (getUseRate()>=autoClearRate){
                synchronized (this){
                    if (getUseRate()>=autoClearRate){
                        notebook.clear();
                        useCount.set(0);
                    }
                }
            }
        }
    }

    public double getUseRate() {
        return (double)useCount.intValue()/(double)size;
    }


    public void saveFilterToFile(String path){
        try(ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(path))){
            oos.writeObject(this);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static BloomFilter readFilterFromFile(String path){
        try(ObjectInputStream ois=new ObjectInputStream(new FileInputStream(path))){
            return (BloomFilter) ois.readObject();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * 清空过滤器中的记录信息
     */
    public void clear(){
        useCount.set(0);
        notebook.clear();;
    }


    /**
     * 分配的位数越多，误判率越低，但是越占内存
     * 4个位误判率大概是0.14689159766308
     * 8个位的误判率大概是0.02157714146322
     * 16个位的误判率大概是0.00046557303372
     * 32个位的误判率大概是0.00000021167340
     */
    private enum  MisjudgmentRate {
        //这里要选取质数，能很好的降低错误率

        //每个字符串分配4个位
        VERY_SMALL(new int[]{2,3,5,7}),

        //每个字符串分配8个位
        SMALL(new int[]{2,3,5,7,11,13,17,19}),

        //每个字符串分配16个位
        MIDDLE(new int[]{2,3,5,7,11,13,17,19,23,29,31,37,41,43,47,53}),

        //每个字符串分配32个位
        HIGH(new int[]{2,3,5,7,11,13,17,19,23,29,31,37,41,43,47,53,59,61,101,103,107,109,113,127,131});

        @Getter
        @Setter
        private int[] seeds;

        MisjudgmentRate(int[] seeds) {
            this.seeds=seeds;
        }
    }

    /*public static void main(String[] args){
        BloomFilter filter=new BloomFilter(7);
        System.out.println(filter.addIfNotExist("1111111111"));
        System.out.println(filter.addIfNotExist("2222222222"));
        System.out.println(filter.addIfNotExist("3333333333"));
        System.out.println(filter.addIfNotExist("44444444444444"));
        System.out.println(filter.addIfNotExist("55555555555"));
        System.out.println(filter.addIfNotExist("66666666666"));
        System.out.println(filter.addIfNotExist("1111111111"));
        filter.saveFilterToFile("G:\\11.obj");
        filter=readFilterFromFile("G:\\111.obj");
        System.out.println(filter.getUseRate());
        System.out.println(filter.addIfNotExist("1111111111"));
    }*/
}