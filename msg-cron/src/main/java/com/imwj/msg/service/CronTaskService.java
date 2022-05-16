package com.imwj.msg.service;

import com.imwj.msg.entity.XxlJobGroup;
import com.imwj.msg.entity.XxlJobInfo;
import com.imwj.msg.vo.BasicResultVO;

/**
 * @author wj
 * @create 2022-05-07 17:47
 */
public interface CronTaskService {
    /**
     * 新增/修改 定时任务
     *
     * @return 新增时返回任务Id，修改时无返回
     */
    BasicResultVO saveCronTask(XxlJobInfo xxlJobInfo);

    /**
     * 删除定时任务
     *
     * @param taskId
     */
    BasicResultVO deleteCronTask(Integer taskId);

    /**
     * 启动定时任务
     *
     * @param taskId
     */
    BasicResultVO startCronTask(Integer taskId);


    /**
     * 暂停定时任务
     *
     * @param taskId
     */
    BasicResultVO stopCronTask(Integer taskId);

    /**
     * 得到执行器Id
     *
     * @return
     */
    BasicResultVO getGroupId(String appName, String title);

    /**
     * 创建执行器
     */
    BasicResultVO createGroup(XxlJobGroup xxlJobGroup);
}
