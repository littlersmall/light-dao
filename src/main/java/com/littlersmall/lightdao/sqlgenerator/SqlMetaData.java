package com.littlersmall.lightdao.sqlgenerator;

import com.littlersmall.lightdao.annotation.*;
import com.littlersmall.lightdao.enums.SqlType;
import com.littlersmall.lightdao.utils.ReflectTool;
import lombok.Data;
import org.springframework.jdbc.core.RowMapper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sigh on 2016/1/22.
 */
public class SqlMetaData {
    final static Pattern PATTERN = Pattern.compile("\\{([a-zA-Z0-9_\\.]+)\\}");

    private final MethodMetaData methodMetaData;
    private final Object[] rawArgs;
    private SqlModel sqlModel = new SqlModel();

    @Data
    public static class SqlModel {
        private String sql;
        private SqlType sqlType;
        private Object[] args;
        private RowMapper rowMapper;
        private boolean isReturnList;
    }

    public SqlMetaData(Method method, Object[] rawArgs) {
        this.methodMetaData = new MethodMetaData(method);
        this.rawArgs = rawArgs;
    }

    public void build() {
        conSqlAndSqlType();
        conRowMapper();

        sqlModel.isReturnList = methodMetaData.isReturnList();
    }

    public SqlModel getSqlModel() {
        return sqlModel;
    }

    private void conSqlAndSqlType() {
        Annotation annotation = methodMetaData.getSqlAnnotation();
        String rawSql;

        if (annotation instanceof Select) {
            sqlModel.sqlType = SqlType.SELECT;
            rawSql = ((Select) annotation).value();
        } else if (annotation instanceof Update) {
            sqlModel.sqlType = SqlType.UPDATE;
            rawSql = ((Update) annotation).value();
        } else if (annotation instanceof Execute) {
            sqlModel.sqlType = SqlType.EXECUTE;
            rawSql = ((Execute) annotation).value();
        } else {
            sqlModel.sqlType = null;
            rawSql = null;
            //todo
        }

        //将{}标记的数据替换成 ?
        conSqlAndArgs(rawSql);
    }

    private void conSqlAndArgs(String rawSql) {
        StringBuilder realSqlBuilder = new StringBuilder();
        Matcher matcher = PATTERN.matcher(rawSql);
        Map<String, Integer> stringParamMap = methodMetaData.getStringParam();
        Map<String, Integer> sqlParamMap = methodMetaData.getSqlParam();
        List<Object> argList = new ArrayList<Object>();

        int start = 0;

        while (matcher.find(start)) {
            realSqlBuilder.append(rawSql.substring(start, matcher.start()));

            String key = matcher.group(1);
            Object value = "?";

            if (stringParamMap.containsKey(key)) {
                int index = stringParamMap.get(key);
                value = rawArgs[index].toString();
            } else if (sqlParamMap.containsKey(key)) {
                int index = sqlParamMap.get(key);
                argList.add(rawArgs[index]);
            } else if (key.contains(".")) {
                String params[] = key.split("\\.");

                if (sqlParamMap.containsKey(params[0])) {
                    int index = sqlParamMap.get(params[0]);
                    Object arg = ReflectTool.getNamedField(rawArgs[index], params[1]);
                    argList.add(arg);
                }
            }

            realSqlBuilder.append(value);
            start = matcher.end();
        }

        realSqlBuilder.append(rawSql.substring(start));

        sqlModel.sql = realSqlBuilder.toString();
        sqlModel.args = argList.toArray();
    }

    private void conRowMapper() {
        Class<?> returnType = methodMetaData.getReturnType();

        sqlModel.rowMapper = GenerateRowMapper.mapRow(returnType);

        System.out.println("return type is " + returnType.getName());
    }

    public static void main(String[] args) {
        SqlMetaData sqlMetaData = new SqlMetaData(TestDao.class.getMethods()[0], new Object[] {3, "abc", new TestClass()});
        sqlMetaData.conSqlAndSqlType();

        System.out.println(sqlMetaData.sqlModel.sql);

        System.out.println(Arrays.asList(sqlMetaData.sqlModel.args));
    }

    interface TestDao {
        @Select("select * from abc where id = {param1}, name = {param2}, age = {param3.cc}")
        void get(@SqlParam("param1") int param1, @StringParam("param2") String param2, @SqlParam("param3") TestClass param3);
    }

    static class TestClass {
        private int cc = 5;

        public int getCc() {
            return cc;
        }

        public void setCc(int cc) {
            this.cc = cc;
        }
    }
}
