package com.imwj.msg.web.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 时间线vo
 * @author wj
 * @create 2022-07-19 10:54
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TimeLineItemVo {

    /**
     * items
     */
    private List<ItemsVO> items;

    /**
     * ItemsVO
     */
    @Data
    @Builder
    public static class ItemsVO {
        /**
         * time
         */
        private String time;
        /**
         * title
         */
        private String title;
        /**
         * detail
         */
        private String detail;
        /**
         * color
         */
        private String color;
        /**
         * icon
         */
        private String icon;
    }
}