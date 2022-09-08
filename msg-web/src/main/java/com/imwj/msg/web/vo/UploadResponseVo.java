package com.imwj.msg.web.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 上传后成功返回素材的Id
 * @author wj
 * @create 2022-09-08 17:11
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadResponseVo {
    private String id;
}

