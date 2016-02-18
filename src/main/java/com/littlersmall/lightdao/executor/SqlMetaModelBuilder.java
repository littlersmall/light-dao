package com.littlersmall.lightdao.executor;

import com.littlersmall.lightdao.annotation.*;
import com.littlersmall.lightdao.executor.model.MethodMetaModel;
import com.littlersmall.lightdao.executor.model.SqlMetaModel;
import com.littlersmall.lightdao.utils.ReflectTool;
import org.springframework.jdbc.core.RowMapper;

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
//构造一个Dao中方法的sql元信息
public class SqlMetaModelBuilder {
    final static Pattern PATTERN = Pattern.compile("\\{([a-zA-Z0-9_\\.]+)\\}");

    private MethodMetaModel methodMetaModel;
    private Object[] rawArgs;

    public SqlMetaModelBuilder(Method method, Object[] rawArgs) {
        this.methodMetaModel = new MethodMetaModelBuilder(method).build();
        this.rawArgs = rawArgs;
    }

    //1 构建sql语句和参数
    //2 构建RowMapper和返回类型
    public SqlMetaModel build() {
        SqlMetaModel sqlMetaModel = new SqlMetaModel();

        //1
        conSqlAndArgs(sqlMetaModel);
        //2
        conRowMapperAndReturnType(sqlMetaModel);

        return sqlMetaModel;
    }

    //1 遍历sql，查找被@SqlParam和@StringPara标记的参数
    //2 将标记的位置替换成? or String，
    //3 将被标记参数按序放入args中
    private void conSqlAndArgs(SqlMetaModel sqlMetaModel) {
        String rawSql = methodMetaModel.getRawSql();
        Map<String, Integer> stringParamMap = methodMetaModel.getStringParamMap();
        Map<String, Integer> sqlParamMap = methodMetaModel.getSqlParamMap();
        List<Object> argList = new ArrayList<Object>();
        StringBuilder realSqlBuilder = new StringBuilder();

        int start = 0;
        Matcher matcher = PATTERN.matcher(rawSql);
        //1
        while (matcher.find(start)) {
            realSqlBuilder.append(rawSql.substring(start, matcher.start()));

            String key = matcher.group(1);
            Object value = "?";

            //2
            //StringParam
            if (stringParamMap.containsKey(key)) {
                int index = stringParamMap.get(key);
                value = rawArgs[index].toString();
            } else if (sqlParamMap.containsKey(key)) { //SqlParam
                int index = sqlParamMap.get(key);
                //3
                argList.add(rawArgs[index]);
            } else if (key.contains(".")) { //特殊的SqlParam 例如 user.name
                String params[] = key.split("\\.");

                if (sqlParamMap.containsKey(params[0])) {
                    int index = sqlParamMap.get(params[0]);
                    Object arg = ReflectTool.getNamedField(rawArgs[index], params[1]);
                    //3
                    argList.add(arg);
                }
            }

            realSqlBuilder.append(value);
            start = matcher.end();
        }

        realSqlBuilder.append(rawSql.substring(start));

        sqlMetaModel.setSql(realSqlBuilder.toString());
        sqlMetaModel.setArgs(argList.toArray());
        sqlMetaModel.setSqlType(methodMetaModel.getSqlType());
    }

    //1 获得returnType
    //2 构建RowMapper
    private void conRowMapperAndReturnType(SqlMetaModel sqlMetaModel) {
        //1
        Class<?> returnType = methodMetaModel.getReturnType();
        sqlMetaModel.setReturnList(methodMetaModel.isReturnList());

        //2
        RowMapper rowMapper = RowMapperGenerator.mapRow(returnType);
        sqlMetaModel.setRowMapper(rowMapper);
    }

    /* for test */
    public static void main(String[] args) {
        SqlMetaModel sqlMetaModel = new SqlMetaModelBuilder(TestDao.class.getMethods()[0],
                new Object[] { 3, "abc", new TestClass() }).build();

        System.out.println(sqlMetaModel.getSql());
        System.out.println(Arrays.asList(sqlMetaModel.getArgs()));
    }

    interface TestDao {
        @Select("select * from abc where id = {param1}, name = {param2}, age = {param3.cc}") void get(@SqlParam("param1") int param1,
                @StringParam("param2") String param2, @SqlParam("param3") TestClass param3);
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
