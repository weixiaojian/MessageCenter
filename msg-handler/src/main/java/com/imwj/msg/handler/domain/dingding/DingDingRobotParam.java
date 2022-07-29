package com.imwj.msg.handler.domain.dingding;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 钉钉自定义机器人 入参
 * https://open.dingtalk.com/document/group/custom-robot-access
 * @author wj
 * @create 2022-07-29 11:09
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DingDingRobotParam {
    /**
     * at
     */
    private AtVO at;
    /**
     * text
     */
    private TextVO text;
    /**
     * link
     */
    private LinkVO link;
    /**
     * markdown
     */
    private MarkdownVO markdown;
    /**
     * actionCard
     */
    private ActionCardVO actionCard;
    /**
     * feedCard
     */
    private FeedCardVO feedCard;
    /**
     * msgtype
     */
    private String msgtype;

    /**
     * AtVO 接收者相关
     */
    @NoArgsConstructor
    @Data
    @Builder
    @AllArgsConstructor
    public static class AtVO {
        /**
         * atMobiles
         */
        private List<String> atMobiles;
        /**
         * atUserIds
         */
        private List<String> atUserIds;
        /**
         * isAtAll
         */
        private Boolean isAtAll;
    }

    /**
     * TextVO 文本消息
     */
    @NoArgsConstructor
    @Data
    @Builder
    @AllArgsConstructor
    public static class TextVO {
        /**
         * content
         */
        private String content;
    }

    /**
     * LinkVO
     */
    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    public static class LinkVO {
        /**
         * text
         */
        private String text;
        /**
         * title
         */
        private String title;
        /**
         * picUrl
         */
        private String picUrl;
        /**
         * messageUrl
         */
        private String messageUrl;
    }

    /**
     * MarkdownVO
     */
    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    public static class MarkdownVO {
        /**
         * title
         */
        private String title;
        /**
         * text
         */
        private String text;
    }

    /**
     * ActionCardVO
     */
    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    public static class ActionCardVO {
        /**
         * title
         */
        private String title;
        /**
         * text
         */
        private String text;
        /**
         * btnOrientation
         */
        private String btnOrientation;
        /**
         * btns
         */
        private List<BtnsVO> btns;

        /**
         * BtnsVO
         */
        @NoArgsConstructor
        @Data
        @AllArgsConstructor
        public static class BtnsVO {
            /**
             * title
             */
            private String title;
            /**
             * actionURL
             */
            private String actionURL;
        }
    }

    /**
     * FeedCardVO
     */
    @NoArgsConstructor
    @Data
    @AllArgsConstructor
    public static class FeedCardVO {
        /**
         * links
         */
        private List<LinksVO> links;

        /**
         * LinksVO
         */
        @NoArgsConstructor
        @Data
        @AllArgsConstructor
        public static class LinksVO {
            /**
             * title
             */
            private String title;
            /**
             * messageURL
             */
            private String messageURL;
            /**
             * picURL
             */
            private String picURL;
        }
    }
}
