package com.imwj.msg.handler;

import lombok.*;

/**
 * 请求实体
 * @author langao_q
 * @since 2021-12-29 15:37
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveRequest {
    /**天数*/
    private int leaveDays;

    /**姓名*/
    private String name;
}

