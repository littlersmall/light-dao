package com.littlersmall.lightdao.executor;

import com.littlersmall.lightdao.annotation.*;
import com.littlersmall.lightdao.exception.ExecutorException;
import com.littlersmall.lightdao.executor.model.MethodMetaModel;
import com.littlersmall.lightdao.executor.model.SqlMetaModel;
import com.littlersmall.lightdao.utils.ReflectTool;
import lombok.Data;
import lombok.extern.java.Log;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sigh on 2016/1/22.
 */
//在运行时动态构造一个Dao中方法的sql元信息
@Log
public class SqlMetaModelBuilder {
    final static Pattern PATTERN = Pattern.compile("\\{([a-zA-Z0-9_\\.]+)\\}");

    private MethodMetaModel methodMetaModel;
    private Object[] rawArgs;

    public SqlMetaModelBuilder(MethodMetaModel methodMetaModel, Object[] rawArgs) {
        this.methodMetaModel = methodMetaModel;
        this.rawArgs = rawArgs;
    }

    //1 构建sql语句和参数
    //2 拷贝一些运行时需要的Method元数据
    public SqlMetaModel build() {
        SqlMetaModel sqlMetaModel = new SqlMetaModel();

        //1
        conSqlAndArgs(sqlMetaModel);
        //2
        BeanUtils.copyProperties(methodMetaModel, sqlMetaModel);

        log.info("sql meta data: " + sqlMetaModel);

        return sqlMetaModel;
    }

    //2016/3/18新增功能，将{1}, {2}替换为参数的第1,2个参数
    //
    //
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
                    Object arg = ReflectTool.getFieldByName(rawArgs[index], params[1]);
                    //3
                    argList.add(arg);
                }
            } else if (Character.isDigit(key.charAt(0))) { //{1}, {2}
                try {
                    int index = Integer.valueOf(key);

                    //3
                    argList.add(rawArgs[index - 1]);
                } catch (Exception e) {
                    throw new ExecutorException(e);
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

    /* for test */
    public static void main(String[] args) {
        SqlMetaModel sqlMetaModel = new SqlMetaModelBuilder(new MethodMetaModelBuilder(TestDao.class.getMethods()[0]).build(),
                new Object[] { 3, "abc", new TestClass() }).build();

        System.out.println(sqlMetaModel.getSql());
        System.out.println(Arrays.asList(sqlMetaModel.getArgs()));
    }

    interface TestDao {
        @Select("select * from abc where id = {param1}, name = {param2}, age = {param3.value}") void get(@SqlParam("param1") int param1,
                @StringParam("param2") String param2, @SqlParam("param3") TestClass param3);
    }

    @Data
    static class TestClass {
        private int value = 5;
    }
}
