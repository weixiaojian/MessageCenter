package service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.imwj.msg.api.domain.MessageParam;
import com.imwj.msg.api.domain.SendRequest;
import com.imwj.msg.api.domain.SendResponse;
import com.imwj.msg.api.enums.BusinessCode;

/**
 * @author wj
 * @create 2022-09-28 17:27
 */
public class MsgService {

    private static final String SEND_PATH = "/send";
    private static final String RECALL_PATH = "/messageTemplate/recall";

    /**
     * 发送消息
     *
     * @param host        调用的接口host
     * @param sendRequest 发送消息入参
     * @return
     * @throws Exception
     */
    public static SendResponse send(String host, SendRequest sendRequest) throws Exception {
        String url = host + SEND_PATH;
        String result = HttpUtil.post(url, JSONUtil.toJsonStr(sendRequest));
        return JSONUtil.toBean(result, SendResponse.class);
    }

    /**
     * 根据模板ID撤回消息
     *
     * @param host 调用的接口host
     * @param id   撤回消息的模板ID
     * @return
     * @throws Exception
     */
    public static SendResponse recall(String host, String id) throws Exception {
        String url = host + RECALL_PATH + StrUtil.SLASH + id;
        String result = HttpUtil.post(url, id);
        return JSONUtil.toBean(result, SendResponse.class);
    }

    public static void main(String[] args) {
        SendRequest request = SendRequest.builder().code(BusinessCode.COMMON_SEND.getCode())
                .messageTemplateId(68L)
                .messageParam(MessageParam.builder().receiver("phone").build()).build();

        try {
            MsgService.send("url", request);
        } catch (Exception e) {
            System.out.println(e);

        }
    }
}
