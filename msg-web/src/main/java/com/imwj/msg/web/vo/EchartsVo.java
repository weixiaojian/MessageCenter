package com.imwj.msg.web.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 图标vo
 * @author wj
 * @create 2022-07-19 10:53
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EchartsVo {
    /**
     * title 标题
     */
    @JsonProperty
    private TitleVO title;
    /**
     * tooltip 提示
     */
    @JsonProperty
    private TooltipVO tooltip;
    /**
     * legend 图例
     */
    @JsonProperty
    private LegendVO legend;
    /**
     * xAxis x轴
     */
    @JsonProperty
    private XAxisVO xAxis;
    /**
     * yAxis y轴
     */
    @JsonProperty
    private YAxisVO yAxis;
    /**
     * series 系列列表
     * <p>
     * 每个系列通过 type 决定自己的图表类型
     */
    @JsonProperty
    private List<SeriesVO> series;

    /**
     * TitleVO
     */
    @Data
    @Builder
    public static class TitleVO {
        /**
         * text
         */
        private String text;
    }

    /**
     * TooltipVO
     */
    @Data
    @Builder
    public static class TooltipVO {
        private String color;
    }

    /**
     * LegendVO
     */
    @Data
    @Builder
    public static class LegendVO {
        /**
         * data
         */
        private List<String> data;
    }

    /**
     * XAxisVO
     */
    @Data
    @Builder

    public static class XAxisVO {
        /**
         * data
         */
        private List<String> data;
    }

    /**
     * YAxisVO
     */
    @Data
    @Builder
    public static class YAxisVO {
        private String type;
    }

    /**
     * SeriesVO
     */
    @Data
    @Builder
    public static class SeriesVO {
        /**
         * name
         */
        private String name;
        /**
         * type
         */
        private String type;
        /**
         * data
         */
        private List<Integer> data;
    }
}
