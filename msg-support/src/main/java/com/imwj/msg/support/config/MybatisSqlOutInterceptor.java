package com.imwj.msg.support.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;


/**
 * sql打印拦截器
 * @author langao_q
 * @since 2021-01-31 17:57
 */
@Slf4j
@Component
@Intercepts({@Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }),
            @Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class })})
public class MybatisSqlOutInterceptor implements Interceptor {

	private Properties properties;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
    	if (invocation == null || invocation.getArgs().length < 1) {
            return null;
        }
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        Object parameter = null;
        if (invocation.getArgs().length > 1) {
            parameter = invocation.getArgs()[1];
        }
        String sqlId = mappedStatement.getId();
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        Configuration configuration = mappedStatement.getConfiguration();
        Object returnValue = null;
        String sql = "";
        Long time = null;
        long start = System.currentTimeMillis();
		try {
            sql = getSql(configuration, boundSql, sqlId);
            returnValue = invocation.proceed();
            long end = System.currentTimeMillis();
            time = (end - start);
            log.info("====sql耗时【"+time+"】毫秒===" + sql);
		} catch (Exception e) {
            log.error("错误====sql===" + sql,e);
			throw e;
		}
        sql = null;
        return returnValue;
    }

    public static String getSql(Configuration configuration, BoundSql boundSql, String sqlId) {
        StringBuilder str = new StringBuilder();
        try {
            String sql = showSql(configuration, boundSql);
            str = new StringBuilder();
            str.append(sql);
        } catch (Exception e) {

        }
        return str.toString();
    }

    private static String getParameterValue(Object obj) {
        String value = null;
        if (obj instanceof String) {
            value = "'" + obj.toString() + "'";
        } else if (obj instanceof Date) {
            DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
            value = "to_date('" + formatter.format(obj) + "','yyyy-mm-dd hh24:mi:ss')";
        } else {
            if (obj != null) {
                value = obj.toString();
            } else {
                value = "";
            }

        }
        return value;
    }

    public static String showSql(Configuration configuration, BoundSql boundSql) {
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
        if (parameterMappings.size() > 0 && parameterObject != null) {
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                sql = sql.replaceFirst("\\?", getParameterValue(parameterObject));

            } else {
                MetaObject metaObject = configuration.newMetaObject(parameterObject);
                for (ParameterMapping parameterMapping : parameterMappings) {
                    String propertyName = parameterMapping.getProperty();
                    if (metaObject.hasGetter(propertyName)) {
                        Object obj = metaObject.getValue(propertyName);
                        sql = sql.replaceFirst("\\?", getParameterValue(obj));
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        Object obj = boundSql.getAdditionalParameter(propertyName);
                        sql = sql.replaceFirst("\\?", getParameterValue(obj));
                    }
                }
            }
        }
        return sql;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties0) {
        this.properties = properties0;
    }
}
