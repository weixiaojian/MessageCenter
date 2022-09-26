package com.imwj.msg.test;

import com.google.common.util.concurrent.RateLimiter;

/**
 * @author langao_q
 * @since 2022-01-11 15:08
 */
public class Main {

    public static void main(String[] args) {
        int[] nums = {1,2,3};
        int[] ints = twoSum(nums, 5);
        for(int i=0; i< ints.length; i++){
            System.out.println(ints[i]);
        }
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
