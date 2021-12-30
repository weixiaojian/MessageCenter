package com.imwj.msg.receiver;

import com.imwj.msg.utils.GroupIdMappingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListenerAnnotationBeanPostProcessor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 自定义消费者信息（启动消费者的时候给消费者的topicGroupId赋值 同时会启动多个消费者 具体看groupIds的数量）
 *
 * @author langao_q
 * @since 2021-12-30 16:34
 */
@Service
public class ReceiverStart {

    @Autowired
    private ApplicationContext context;

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

    /**
     * 为每个渠道不同的消息类型 创建一个Receiver对象
     */
    @PostConstruct
    public void init() {
        for (int i = 0; i < groupIds.size(); i++) {
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
                    attrs.put("groupId", groupIds.get(index));
                    index++;
                }
            }
            return attrs;
        };
    }

}
