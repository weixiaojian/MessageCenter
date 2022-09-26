package com.imwj.msg.handler.receiver.kafka;

import com.imwj.msg.handler.utils.GroupIdMappingUtils;
import com.imwj.msg.support.constans.MessageQueuePipeline;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.header.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListenerAnnotationBeanPostProcessor;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

/**
 * 自定义消费者信息（启动消费者的时候给消费者的topicGroupId赋值 同时会启动多个消费者 具体看groupIds的数量）
 *
 * @author langao_q
 * @since 2021-12-30 16:34
 */
@Slf4j
@Service
@ConditionalOnProperty(name = "msg.mq.pipeline", havingValue = MessageQueuePipeline.KAFKA)
public class ReceiverStart {

    @Autowired
    private ApplicationContext context;
    @Autowired
    private ConsumerFactory consumerFactory;

    /**
     * receiver的消费方法常量（即MsgReceiver类中的consumer方法名）
     */
    private static final String RECEIVER_METHOD_NAME = "MsgReceiver.consumer";

    /**
     * 获取得到所有的groupId
     */
    private static List<String> groupIds = GroupIdMappingUtils.getAllGroupIds();

    /**
     * 下标(用于迭代groupIds位置)
     */
    private static Integer index = 0;
    @Value("${msg.nacos.enabled}")
    private Boolean nacosEnabled;

    /**
     * 为每个渠道不同的消息类型 创建一个Receiver对象
     */
    @PostConstruct
    public void init() {
        int total = groupIds.size();
        if (nacosEnabled) {
            // 当nacos开启时 会导致Receiver提前加载 所以这里getBean次数-1
            // nacos issue: https://github.com/nacos-group/nacos-spring-project/issues/249
            total -= 1;
        }
        for (int i = 0; i < total; i++) {
            context.getBean(MsgReceiver.class);
        }
    }

    /**
     * 给每个Receiver对象的consumer方法 @KafkaListener赋值相应的groupId
     */
    @Bean
    public static KafkaListenerAnnotationBeanPostProcessor.AnnotationEnhancer groupIdEnhancer() {
        return (attrs, element) -> {
            if (element instanceof Method) {
                String name = ((Method) element).getDeclaringClass().getSimpleName() + "." + ((Method) element).getName();
                if (RECEIVER_METHOD_NAME.equals(name)) {
                    attrs.put("groupId", groupIds.get(index++));
                }
            }
            return attrs;
        };
    }

    /**
     * 针对tag消息过滤
     * producer 将tag写进header里
     * @return
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory filterContainerFactory(@Value("${msg.business.tagId.key}") String tagIdKey,
                                                                          @Value("${msg.business.tagId.value}") String tagIdValue) {
        ConcurrentKafkaListenerContainerFactory factory = new ConcurrentKafkaListenerContainerFactory();
        factory.setConsumerFactory(consumerFactory);
        factory.setAckDiscarded(true);

        factory.setRecordFilterStrategy(consumerRecord -> {
            if (Optional.ofNullable(consumerRecord.value()).isPresent()) {
                for (Header header : consumerRecord.headers()) {
                    if (header.key().equals(tagIdKey) && new String(header.value()).equals(new String(tagIdValue.getBytes(StandardCharsets.UTF_8)))) {
                        return false;
                    }
                }
            }
            //返回true将会被丢弃
            return true;
        });
        return factory;
    }

}
