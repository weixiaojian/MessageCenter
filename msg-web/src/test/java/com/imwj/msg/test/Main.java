package com.imwj.msg.test;

import cn.hutool.core.io.file.FileWriter;
import com.google.common.util.concurrent.RateLimiter;

/**
 * @author langao_q
 * @since 2022-01-11 15:08
 */
public class Main {

    public static void main(String[] args) {
        long millis = System.currentTimeMillis();
        StringBuilder builder = new StringBuilder();
        for (int i=0; i<10000000; i++){
            builder.append("有两种常见的方法来实现单例。两者都基于保持构造方法私有和导出公共静态成员以提供对唯一实例的访问。在第一种方法中，成员是 final 修饰的属性：");
        }

        long ms1 = System.currentTimeMillis() - millis;
        System.out.println("耗时1：" + ms1);

        FileWriter writer = new FileWriter("e://test.log");
        writer.write(builder.toString());

        long ms2 = System.currentTimeMillis() - millis;
        System.out.println("耗时2：" + ms2);
    }


    public static void fun1(RateLimiter rateLimiter){
        System.out.println("线程名：" + Thread.currentThread().getName());
        Double costTime = rateLimiter.acquire(2);
        if(costTime > 0){
            System.out.println(Thread.currentThread().getName() + "--限流中...");
        }
    }

    /**
     * 获取两数之和的数组下标
     * @param nums
     * @param target
     * @return
     */
    public static int[] twoSum(int[] nums, int target) {
        int[] res = {0,0};
        for(int i=0; i< nums.length; i++){
            int data1 = target - nums[i];
            for(int j=i+1; j< nums.length; j++){
                if(data1 == nums[j]){
                    res[0] = i;
                    res[1] = j;
                    return res;
                }
            }
        }
        return null;
    }
}
