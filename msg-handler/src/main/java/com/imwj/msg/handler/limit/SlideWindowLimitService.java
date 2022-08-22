package com.imwj.msg.handler.limit;

import com.imwj.msg.common.domain.TaskInfo;
import com.imwj.msg.handler.deduplication.DeduplicationParam;
import com.imwj.msg.handler.service.deduplication.service.AbstractDeduplicationService;
import com.imwj.msg.support.utils.RedisUtils;
import com.imwj.msg.support.utils.SnowFlakeIdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author wj
 * @create 2022-08-22 11:07
 */
@Service(value = "SlideWindowLimitService")
public class SlideWindowLimitService extends AbstractLimitService {

    private static final String LIMIT_TAG = "SW_";

    @Autowired
    private RedisUtils redisUtils;

    private SnowFlakeIdUtils snowFlakeIdUtils = new SnowFlakeIdUtils(1, 1);

    private DefaultRedisScript<Long> redisScript;


    @PostConstruct
    public void init() {
        redisScript = new DefaultRedisScript();
        redisScript.setResultType(Long.class);
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("limit.lua")));
    }


    /**
     * @param service  去重器对象
     * @param taskInfo
     * @param param    去重参数
     * @return 返回不符合条件的手机号码
     */
    @Override
    public Set<String> limitFilter(AbstractDeduplicationService service, TaskInfo taskInfo, DeduplicationParam param) {

        Set<String> filterReceiver = new HashSet<>(taskInfo.getReceiver().size());
        long nowTime = System.currentTimeMillis();
        for (String receiver : taskInfo.getReceiver()) {
            String key = LIMIT_TAG + deduplicationSingleKey(service, taskInfo, receiver);
            String scoreValue = String.valueOf(snowFlakeIdUtils.nextId());
            String score = String.valueOf(nowTime);
            if (redisUtils.execLimitLua(redisScript, Arrays.asList(key), String.valueOf(param.getDeduplicationTime() * 1000), score, String.valueOf(param.getCountNum()), scoreValue)) {
                filterReceiver.add(receiver);
            }

        }
        return filterReceiver;
    }


}
