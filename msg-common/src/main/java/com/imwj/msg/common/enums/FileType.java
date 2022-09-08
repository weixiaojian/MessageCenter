package com.imwj.msg.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 文件类型
 * @author wj
 * @create 2022-09-02 16:12
 */
@Getter
@ToString
@AllArgsConstructor
public enum FileType {

    /**
     * 图片
     * 语音
     * 文件
     * 视频
     */
    IMAGE("10", "image"),
    VOICE("20", "voice"),
    COMMON_FILE("30", "file"),
    VIDEO("40", "video"),
            ;
    private String code;
    private String dingDingName;

    public static String dingDingNameByCode(String code) {
        for (FileType fileType : FileType.values()) {
            if (fileType.getCode().equals(code)) {
                return fileType.getDingDingName();
            }
        }
        return null;
    }
}
