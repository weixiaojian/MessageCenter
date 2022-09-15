package com.imwj.msg.web.service;

import com.imwj.msg.common.vo.BasicResultVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * 素材service
 * @author wj
 * @create 2022-09-08 17:11
 */
public interface MaterialService {

    /**
     * 钉钉素材上传
     * @param file
     * @param sendAccount
     * @param fileType
     * @return
     */
    BasicResultVO dingDingMaterialUpload(MultipartFile file, String sendAccount, String fileType);
}
