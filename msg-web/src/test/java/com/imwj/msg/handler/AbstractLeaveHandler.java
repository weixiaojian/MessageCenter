package com.imwj.msg.handler;

/**
 * 责任链抽象处理类
 * @author langao_q
 * @since 2021-12-29 15:37
 */
public class AbstractLeaveHandler {

    /**三级领导处理*/
    protected int MIN = 10;
    /**二级领导处理*/
    protected int MIDDLE = 20;
    /**一级级领导处理*/
    protected int MAX = 30;

    /**领导名称*/
    protected String handlerName;

    /**下一个处理节点（即更高级别的领导）*/
    protected AbstractLeaveHandler nextHandler;

    /**设置下一节点*/
    protected void setNextHandler(AbstractLeaveHandler handler){
        this.nextHandler = handler;
    }

    /**处理请求，子类实现*/
    protected void handlerRequest(LeaveRequest request){
    }
}
