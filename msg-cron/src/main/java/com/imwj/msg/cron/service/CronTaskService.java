package com.imwj.msg.cron.service;


import com.imwj.msg.common.vo.BasicResultVO;
import com.imwj.msg.cron.entity.XxlJobGroup;
import com.imwj.msg.cron.entity.XxlJobInfo;

/**
 * xxl-job service
 * @author wj
 * @create 2022-05-07 17:47
 */
public interface CronTaskService {

    /**
     * 新增/修改 定时任务
     * @param xxlJobInfo
     * @return 新增时返回任务Id，修改时无返回
     */
    BasicResultVO saveCronTask(XxlJobInfo xxlJobInfo);

    /**
     * 删除定时任务
     * @param taskId
     * @return
     */
    BasicResultVO deleteCronTask(Integer taskId);

    /**
     * 启动定时任务
     * @param taskId
     * @return
     */
    BasicResultVO startCronTask(Integer taskId);


    /**
     * 暂停定时任务
     *
     * @param taskId
     * @return
     */
    BasicResultVO stopCronTask(Integer taskId);

    /**
     * 得到执行器Id
     * @param appName
     * @param title
     * @return
     */
    BasicResultVO getGroupId(String appName, String title);

    /**
     * 创建执行器
     * @param xxlJobGroup
     * @return
     */
    BasicResultVO createGroup(XxlJobGroup xxlJobGroup);
}
