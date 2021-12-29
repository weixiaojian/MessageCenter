package com.imwj.msg.handler;

/**
 * 测试类
 * @author langao_q
 * @since 2021-12-29 15:39
 */
public class MainTest {

    public static void main(String[] args) {
        //根据leaveDays的值来决定是哪一级别的领导处理
        LeaveRequest request = LeaveRequest.builder().leaveDays(50).name("测试").build();
        /**
         * 三级(10) < 二级(20) < 一级(30)；三级领导能处理就不往上走了  三级处理不了再抛给二级领导
         */
        AbstractLeaveHandler directLeaderLeaveHandler = new ThreeLeaveHandler("三级领导");
        TwoLeaveHandler deptManagerLeaveHandler = new TwoLeaveHandler("二级领导");
        OneLeaveHandler gManagerLeaveHandler = new OneLeaveHandler("一级领导");
        //将各个处理类串联起来
        directLeaderLeaveHandler.setNextHandler(deptManagerLeaveHandler);
        deptManagerLeaveHandler.setNextHandler(gManagerLeaveHandler);
        //处理方法
        directLeaderLeaveHandler.handlerRequest(request);
    }
}
