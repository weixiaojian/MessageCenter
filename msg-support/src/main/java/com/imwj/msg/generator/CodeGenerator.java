package com.imwj.msg.generator;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

/**
 * @author langao_q
 * @since 2021-12-29 16:22
 */
public class CodeGenerator {


    /**
     * 数据库类型
     */
    private final DbType dbType = DbType.MYSQL;
    /**
     * 数据库连结信息
     */
    private final String dbUrl = "jdbc:mysql://localhost:3306/msg_center";
    private final String driver = "org.gjt.mm.mysql.Driver";
    private final String userName = "root";
    private final String password = "123456";

    /**
     * 项目名
     */
    private final String projectName = "msg-support";
    /**
     * 指定包名
     */
    private final String packageName = "com.imwj.msg.generator";
    /**
     * 作者名
     */
    private final String author = "langao_q";
    /**
     * 指定生成的表名（要大写）
     */
    private final String[] tableNames = new String[]{"sms_record"};

    /**
     * 项目路径
     */
    private final String path = "E:\\idea-work\\imwj\\MessageCenter\\";

    /**
     * 根据表自动生成
     * @param serviceNameStartWithI 默认为false
     * @param packageName      包名
     * @param tableNames      表名
     * @author Terry
     */
    private void generateByTables(boolean serviceNameStartWithI, String packageName, String... tableNames) {
        //配置数据源
        DataSourceConfig dataSourceConfig = getDataSourceConfig();
        // 策略配置
        StrategyConfig strategyConfig = getStrategyConfig(tableNames);
        //全局变量配置
        GlobalConfig globalConfig = getGlobalConfig(serviceNameStartWithI);
        //包名配置
        PackageConfig packageConfig = getPackageConfig(packageName);
        //自动生成
        atuoGenerator(dataSourceConfig, strategyConfig, globalConfig, packageConfig);
    }
    /**
     * 集成
     * @param dataSourceConfig 配置数据源
     * @param strategyConfig  策略配置
     * @param config      全局变量配置
     * @param packageConfig  包名配置
     * @author Terry
     */
    private void atuoGenerator(DataSourceConfig dataSourceConfig, StrategyConfig strategyConfig, GlobalConfig config, PackageConfig packageConfig) {
        // 配置模板：设置是否生成controller、service、mapper等，设置为null(不生成)、不set默认会生成
        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setController(null);
        templateConfig.setService(null);
        templateConfig.setServiceImpl(null);

        new AutoGenerator()
                .setGlobalConfig(config)
                .setDataSource(dataSourceConfig)
                .setStrategy(strategyConfig)
                .setPackageInfo(packageConfig)
                .setTemplateEngine(new FreemarkerTemplateEngine())
                .setTemplate(templateConfig)
                .execute();
    }
    /**
     * 设置包名
     * @param packageName 父路径包名
     * @param packageName 模块名
     * @return PackageConfig 包名配置
     * @author Terry
     */
    private PackageConfig getPackageConfig(String packageName) {
        return new PackageConfig()
                .setParent(packageName);
    }
    /**
     * 全局配置
     * @param serviceNameStartWithI false
     * @return GlobalConfig
     * @author Terry
     */
    private GlobalConfig getGlobalConfig(boolean serviceNameStartWithI) {
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig
                .setBaseColumnList(true)
                .setBaseResultMap(true)
                .setActiveRecord(false)
                //作者
                .setAuthor(author)
                //设置输出路径
                .setOutputDir(getOutputDir(projectName))
                .setFileOverride(true);
        if (!serviceNameStartWithI) {
            //设置service名
            globalConfig.setServiceName("%sService");
        }
        return globalConfig;
    }
    /**
     * 返回项目路径
     * @param projectName 项目名
     * @return 项目路径
     * @author Terry
     */
    private String getOutputDir(String projectName) {
        return path + projectName + "/src/main/java/";
    }
    /**
     * 策略配置
     * @param tableNames 表名
     * @return StrategyConfig
     * @author Terry
     */
    private StrategyConfig getStrategyConfig(String... tableNames) {
        return new StrategyConfig()
                // 全局大写命名 ORACLE 注意
                .setCapitalMode(true)
                //从数据库表到文件的命名策略
                .setNaming(NamingStrategy.underline_to_camel)
                //需要生成的的表名，多个表名传数组
                .setInclude(tableNames)
                //使用lombok
                .setEntityLombokModel(true)
                //去掉表名前缀
                .setTablePrefix("tab_")
                //rest风格
                .setRestControllerStyle(true);
    }
    /**
     * 配置数据源
     * @return 数据源配置 DataSourceConfig
     * @author Terry
     */
    private DataSourceConfig getDataSourceConfig() {
        return new DataSourceConfig().setDbType(dbType)
                .setUrl(dbUrl)
                .setUsername(userName)
                .setPassword(password)
                .setDriverName(driver);
    }
    /**
     * 根据表自动生成
     * @param packageName 包名
     * @param tableNames 表名
     * @author Terry
     */
    @SuppressWarnings("unused")
    private void generateByTables(String packageName, String... tableNames) {
        generateByTables(true, packageName, tableNames);
    }

    /**
     * 代码生成
     * @param args
     */
    public static void main(String[] args) {
        CodeGenerator generator = new CodeGenerator();
        generator.generateByTables(false, generator.packageName, generator.tableNames);
    }
}
