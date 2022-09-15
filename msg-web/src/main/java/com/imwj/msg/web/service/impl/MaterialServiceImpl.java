package com.imwj.msg.web.service.impl;

import cn.hutool.core.util.IdUtil;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiMediaUploadRequest;
import com.dingtalk.api.response.OapiMediaUploadResponse;
import com.google.common.base.Throwables;
import com.imwj.msg.common.constant.SendAccountConstant;
import com.imwj.msg.common.enums.FileType;
import com.imwj.msg.common.enums.RespStatusEnum;
import com.imwj.msg.common.vo.BasicResultVO;
import com.imwj.msg.web.service.MaterialService;
import com.imwj.msg.web.vo.UploadResponseVo;
import com.taobao.api.FileItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * 素材service实现类
 * @author wj
 * @create 2022-09-08 17:11
 */
@Slf4j
@Service
public class MaterialServiceImpl implements MaterialService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String DING_DING_URL = "https://oapi.dingtalk.com/media/upload";

    @Override
    public BasicResultVO dingDingMaterialUpload(MultipartFile file, String sendAccount, String fileType) {
        OapiMediaUploadResponse rsp;
        try {
            // 获取钉钉token
            String accessToken = redisTemplate.opsForValue().get(SendAccountConstant.DING_DING_ACCESS_TOKEN_PREFIX + sendAccount);
            DingTalkClient client = new DefaultDingTalkClient(DING_DING_URL);
            // 初始化请求参数
            OapiMediaUploadRequest req = new OapiMediaUploadRequest();
            FileItem item = new FileItem(new StringBuilder().append(IdUtil.fastSimpleUUID()).append(file.getOriginalFilename()).toString(),
                    file.getInputStream());
            req.setMedia(item);
            req.setType(FileType.dingDingNameByCode(fileType));
            // 发起请求
            rsp = client.execute(req, accessToken);
            // 解析结果 并返回素材id
            if (rsp.getErrcode() == 0L) {
                return new BasicResultVO(RespStatusEnum.SUCCESS, UploadResponseVo.builder().id(rsp.getMediaId()).build());
            }
            log.error("MaterialService#dingDingMaterialUpload fail:{}", rsp.getErrmsg());
        } catch (Exception e) {
            log.error("MaterialService#dingDingMaterialUpload fail:{}", Throwables.getStackTraceAsString(e));
        }
        return BasicResultVO.fail("未知错误，联系管理员");
    }
}
