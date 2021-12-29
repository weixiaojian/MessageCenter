package com.imwj.msg.handler;

/**
 *  一级领导
 * @author langao_q
 * @since 2021-12-29 15:39
 */
public class OneLeaveHandler extends AbstractLeaveHandler {

    public OneLeaveHandler(String name) {
        this.handlerName = name;
    }

    @Override
    protected void handlerRequest(LeaveRequest request) {
        if(request.getLeaveDays() > this.MIDDLE && request.getLeaveDays() <= this.MAX){
            System.out.println(handlerName + ",已经处理;流程结束。");
            return;
        }

        if(null != this.nextHandler){
            this.nextHandler.handlerRequest(request);
        }else{
            System.out.println("审批拒绝！");
        }
    }
}
