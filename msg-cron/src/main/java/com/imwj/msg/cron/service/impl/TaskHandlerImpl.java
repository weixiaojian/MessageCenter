package com.imwj.msg.cron.service.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.csv.CsvRow;
import cn.hutool.core.util.StrUtil;
import com.imwj.msg.cron.csv.CountFileRowHandler;
import com.imwj.msg.cron.domain.CrowdInfoVo;
import com.imwj.msg.cron.pending.CrowdBatchTaskPending;
import com.imwj.msg.cron.service.TaskHandler;
import com.imwj.msg.cron.utils.ReadFileUtils;
import com.imwj.msg.support.dao.MessageTemplateDao;
import com.imwj.msg.support.domain.MessageTemplate;
import com.imwj.msg.support.pending.AbstractLazyPending;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * @author wj
 * @create 2022-05-26 16:52
 */
@Slf4j
@Service
public class TaskHandlerImpl implements TaskHandler {

    @Resource
    private MessageTemplateDao messageTemplateDao;
    @Autowired
    private ApplicationContext context;

    @Override
    public void handle(Long messageTemplateId) {
        log.info("TaskHandler handle:{}", Thread.currentThread().getName());
        //查询模板数据中的文件相关信息
        MessageTemplate messageTemplate = messageTemplateDao.selectById(messageTemplateId);
        if(messageTemplate == null || StrUtil.isBlank(messageTemplate.getCronCrowdPath())){
            log.error("TaskHandler#handle crowdPath empty!");
            return;
        }
        // 1.获取文件行数大小
        long countCsvRow = ReadFileUtils.countCsvRow(messageTemplate.getCronCrowdPath(), new CountFileRowHandler());
        // 2.得到batch处理器
        CrowdBatchTaskPending crowdBatchTaskPending = context.getBean(CrowdBatchTaskPending.class);
        // 3.读取文件得到每一行记录给到队列做batch处理
        ReadFileUtils.getCsvRow(messageTemplate.getCronCrowdPath(), row -> {
            if (CollUtil.isEmpty(row.getFieldMap())
                    || StrUtil.isBlank(row.getFieldMap().get(ReadFileUtils.RECEIVER_KEY))) {
                return;
            }
            // 4.每一行处理交给LazyPending
            HashMap<String, String> params = ReadFileUtils.getParamFromLine(row.getFieldMap());
            CrowdInfoVo crowdInfoVo = CrowdInfoVo.builder().receiver(row.getFieldMap().get(ReadFileUtils.RECEIVER_KEY))
                    .params(params).messageTemplateId(messageTemplateId).build();
            crowdBatchTaskPending.pending(crowdInfoVo);

            // 5.判断是否读取文件完成回收资源且更改状态
            onComplete(row, countCsvRow, crowdBatchTaskPending, messageTemplateId);
        });
    }
    /**
     * 文件遍历结束时
     * 1. 暂停单线程池消费(最后会回收线程池资源)
     * 2. 更改消息模板的状态(暂未实现)
     * @param row 读取文件行数
     * @param countCsvRow   文件总行数
     * @param crowdBatchTaskPending 批量处理任务器
     * @param messageTemplateId 模板id
     */
    private void onComplete(CsvRow row, long countCsvRow, AbstractLazyPending crowdBatchTaskPending, Long messageTemplateId) {
        if (row.getOriginalLineNumber() == countCsvRow) {
            crowdBatchTaskPending.setStop(true);
            log.info("messageTemplate:[{}] read csv file complete!", messageTemplateId);
        }
    }
}
