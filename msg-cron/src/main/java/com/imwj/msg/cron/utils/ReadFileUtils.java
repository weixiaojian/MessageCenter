package com.imwj.msg.cron.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.csv.*;
import com.google.common.base.Throwables;
import com.imwj.msg.cron.csv.CountFileRowHandler;
import com.imwj.msg.cron.domain.CrowdInfoVo;
import lombok.extern.slf4j.Slf4j;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 读取人群文件 工具类
 * @author wj
 * @create 2022-05-26 16:56
 */
@Slf4j
public class ReadFileUtils {

    /**
     * csv文件 存储 接收者 的列名
     */
    public static final String RECEIVER_KEY = "userId";

    /**
     * 读取csvwenjian，每读取一行都会嗲用csvRowHandler对应的方法
     * @param path
     * @param csvRowHandler
     */
    public static void getCsvRow(String path, CsvRowHandler csvRowHandler){
        try{
            //首行作为标题
            CsvReader reader = CsvUtil.getReader(new FileReader(path), new CsvReadConfig().setContainsHeader(true));
            reader.read(csvRowHandler);
        }catch (Exception e){
            log.error("ReadFileUtils#getCsvRow fail!{}", Throwables.getStackTraceAsString(e));
        }
    }

    /**
     * 从文件的每一行数据获取到params信息
     * @param fieldMap
     * @return
     */
    public static HashMap<String, String> getParamFromLine(Map<String, String> fieldMap){
        HashMap<String, String> params = MapUtil.newHashMap();
        for(Map.Entry<String, String> entry : fieldMap.entrySet()){
            if(!ReadFileUtils.RECEIVER_KEY.equals(entry.getKey())){
                params.put(entry.getKey(), entry.getValue());
            }
        }
        return params;
    }

    /**
     * 读取csv文件，获取文件里的行数
     *
     * @param path
     * @param countFileRowHandler
     */
    public static long countCsvRow(String path, CountFileRowHandler countFileRowHandler) {
        try {
            // 把首行当做是标题，获取reader
            CsvReader reader = CsvUtil.getReader(new FileReader(path),
                    new CsvReadConfig().setContainsHeader(true));
            reader.read(countFileRowHandler);
        } catch (Exception e) {
            log.error("ReadFileUtils#getCsvRow fail!{}", Throwables.getStackTraceAsString(e));
        }
        return countFileRowHandler.getRowSize();
    }

    /**
     * 一次性读取csv文件
     * 1. 获取第一行信息(id,paramsKey1,params2Key2)，第一列默认为接收者Id
     * 2. 把文件信息塞进对象内
     * 3. 把对象返回
     * @param path
     * @return
     */
    @Deprecated
    public static List<CrowdInfoVo> getCsvRowList(String path) {
        List<CrowdInfoVo> result = new ArrayList<>();
        try {
            CsvData data = CsvUtil.getReader().read(FileUtil.file(path));
            if(data == null || data.getRow(0) == null || data.getRow(1) == null){
                log.error("read csv file empty!,path:{}", path);
            }
            // 第一行默认为头信息  一般从第二行开始遍历，第一列为接收者Id(不处理)
            CsvRow headInfo = data.getRow(0);
            for(int i=1; i<data.getRowCount(); i++){
                CsvRow row = data.getRow(i);
                Map<String, String> param = MapUtil.newHashMap();
                for(int j=1; j<headInfo.size(); j++){
                    param.put(headInfo.get(j), row.get(j));
                }
                result.add(CrowdInfoVo.builder().receiver(CollUtil.getFirst(row.iterator())).params(param).build());
            }
        }catch (Exception e){
            log.error("TaskHandler#getCsvRowList fail!{}", Throwables.getStackTraceAsString(e));
        }
        return result;
    }

    public static void main(String[] args) {
        ReadFileUtils.getCsvRow("E:/upload/2022-07-06/test.csv", row -> {
            HashMap<String, String> params = ReadFileUtils.getParamFromLine(row.getFieldMap());
            System.out.println(params);
        });
    }
}
