package cqjtu.ds.yun.utils;

import java.math.BigInteger;
import java.util.Random;

/**
 * 密钥生成：
 * 1.随机选择两个大质数p和q满足gcd(pq,(p-1)(q-1))=1。这个属性是保证两个质数长度相等。
 * 2.计算n=pq和λ=lcm(p-1,q-1)。
 * 3.选择随机整数g使得gcd(L(g^lambda%n^2),n)=1,满足g属于n^2。
 * 4.公钥为(N,g)
 * 5.私钥为lambda。
 * 加密：
 * 选择随机数r
 * 计算密文
 * 其中m为加密信息
 * 解密：
 * m=D(c,lambda)=(L(c^lambda%n^2)/L(g^lambda%n^2))%n
 * 其中L(u)=(u-1)/n
 */
public class PaillierUtils {

    //选取两个较大的质数p和q，lambda是p-1和q-1的最小公倍数
    private BigInteger p,q,lambda;

    //n是p与q的乘积
    public BigInteger n;

    //n_square=n*n
    private BigInteger n_square;
    //随机选取一个整数g，g属于小于n的平方中的整数集，且g满足：g的lambda次方对n的平方求模后减一后再除以n,
    //最后再将其与n求最大公约数，且最大公约数等于一。
    private BigInteger g;
    //模量
    private int bitLength;

    /**
     *带参的构造方法
     * @param bitLengthVal   模量
     * @param certainty   新的大整数表示质数的概率将超过（1-2^（确定性））。此构造函数的执行时间与此参数的值成比例。
     */
    public PaillierUtils(int bitLengthVal,int certainty){
        KeyGeneration(bitLengthVal,certainty);
    }

    /**
     * 构造了一个模为128位、素数生成的确定度至少为1-2^（-64）的paillier密码系统实例。
     */
    public PaillierUtils(){
        KeyGeneration(32,64);
    }

    /**
     * 产生公钥[N,g]      私钥lamada
     * @param bitLengthVal
     * @param certainty
     */
    public void KeyGeneration(int bitLengthVal,int certainty){
        bitLength=bitLengthVal;
        //随机构造两个大素数，长度可能为bitLength/2,它可能是一个具有指定bitLength的素数
        p=new BigInteger(bitLength/2,certainty,new Random());
        q=new BigInteger(bitLength/2,certainty,new Random());

        //n=p*q
        n=p.multiply(q);

        //n_square=n*n
        n_square=n.multiply(n);
        //随机生成一个0-100的整数g
       // g=new BigInteger(String.valueOf((int)(Math.random()*100)));
        g=new BigInteger("2");

        //求p-1与q-1的乘积除以p-1与q-1的最大公约数
        //lambda=((p-1)*(q-1))/gcd(p-1,q-1)
        lambda=p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE))
                .divide(p.subtract(BigInteger.ONE).gcd(q.subtract(BigInteger.ONE)));
        //检测g是否满足要求,gcd(L(g^lambda mod n_square),n)=1
        if (g.modPow(lambda,n_square).subtract(BigInteger.ONE).divide(n).gcd(n).intValue()!=1){
            System.out.println("g的选取不合适！");
            System.exit(1);
        }
    }

    /**
     * 加密
     * @param m 明文
     * @param r 随机的一个整数r
     * @return  返回密文
     */
    public BigInteger Encryption(BigInteger m,BigInteger r){
        //c=(g^m)*(r^n)mod n_square
        return g.modPow(m,n_square).multiply(r.modPow(n,n_square)).mod(n_square);
    }

    /**加密**/
    public BigInteger Encryption(BigInteger m){
        //构造一个随机生成的大整数，它是在0到(2numBits-1)（包括）范围内均匀分布的值
        BigInteger r=new BigInteger(bitLength,new Random());
        return g.modPow(m,n_square).multiply(r.modPow(n,n_square)).mod(n_square);
    }

    /**
     * 利用私钥lambda对密文进行解密
     * m=(L((c^lambda)mod n_square)/L((g^lambda)mod n_square)) mod n
     * @param c
     * @return 返回明文
     */
    public BigInteger Decryption(BigInteger c){
        BigInteger u = g.modPow(lambda, n_square).subtract(BigInteger.ONE).divide(n).modInverse(n);
        return c.modPow(lambda, n_square).subtract(BigInteger.ONE).divide(n).multiply(u).mod(n);
    }

   /* public static void main(String []args){


        PaillierUtils paillierUtils=new PaillierUtils();
        //创建两个大整数m1,m2:
        BigInteger m1=new BigInteger("70a79ac248f60d34f63c02a4e9afa049",16);
        BigInteger m2=new BigInteger("-70a79ac248f60d34f63c02a4e9afa049",16);
        System.out.println("原文是："+m1+"和"+m2);

        //将m1,m2加密得到em1,em2
        BigInteger em1=paillierUtils.Encryption(m1);
        BigInteger em2=paillierUtils.Encryption(m2);

        //加密后的结果
        System.out.println("密文是："+em1+"  "+em2);

        //解密后的结果
        System.out.println("解密后："+paillierUtils.Decryption(em1)+" "+paillierUtils.Decryption(em2));

        //求明文数值异或
        System.out.println("***********************异或********************");
       // BigInteger xor=m1.xor(m2).mod(paillierUtils.n);
        BigInteger sub=m1.add(m2);
        System.out.println("明文异或："+sub);

        //求密文数值异或
      // BigInteger exor=em1.xor(em2).mod(paillierUtils.n_square);
        BigInteger esub=em1.multiply(em2).mod(paillierUtils.n_square);
        System.out.println("密文异或："+esub);
        System.out.println("密文异或解密："+paillierUtils.Decryption(esub));
    }*/
}
