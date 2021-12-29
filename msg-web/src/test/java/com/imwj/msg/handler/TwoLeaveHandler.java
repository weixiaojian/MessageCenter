package com.imwj.msg.handler;

/**
 * 二级领导
 * @author langao_q
 * @since 2021-12-29 15:38
 */
public class TwoLeaveHandler extends AbstractLeaveHandler {

    public TwoLeaveHandler(String name) {
        this.handlerName = name;
    }

    @Override
    protected void handlerRequest(LeaveRequest request) {
        if(request.getLeaveDays() >this.MIN && request.getLeaveDays() <= this.MIDDLE){
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
