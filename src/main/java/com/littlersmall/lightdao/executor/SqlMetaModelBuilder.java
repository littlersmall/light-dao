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

    //1 拷贝一些运行时需要的Method元数据
    //2 构建sql语句和参数
    public SqlMetaModel build() {
        SqlMetaModel sqlMetaModel = new SqlMetaModel();

        //1
        BeanUtils.copyProperties(methodMetaModel, sqlMetaModel);
        //2
        conBatchSqlAndArgs(sqlMetaModel);

        log.info("sql meta data: " + sqlMetaModel);

        return sqlMetaModel;
    }

    private void conBatchSqlAndArgs(SqlMetaModel sqlMetaModel) {
        int length = 1;

        if (null != rawArgs) {
            for (Object object : rawArgs) {
                if (object instanceof List) {
                    System.out.println("bbbbbbbb" + object.getClass());
                    length = ((List<?>) object).size();
                    break;
                }
            }
        }

        conSqlAndArgs(sqlMetaModel, length);
    }

    //2016/3/31新增功能，支持批量插入，例如 insert(@ListParam("users") List<User> users);
    //2016/3/18新增功能，将{0}, {1}替换为参数的第1,2个参数
    //
    //1 循环构建sql和args
    //2 遍历sql，查找被@SqlParam和@StringPara标记的参数
    //3 将标记的位置替换成? or String，
    //4 将被标记参数按序放入args中
    private void conSqlAndArgs(SqlMetaModel sqlMetaModel, int length) {
        List<Object[]> argLists = new ArrayList<Object[]>();
        Map<String, Integer> stringParamMap = methodMetaModel.getStringParamMap();
        Map<String, Integer> sqlParamMap = methodMetaModel.getSqlParamMap();

        String rawSql = methodMetaModel.getRawSql();
        Matcher matcher = PATTERN.matcher(rawSql);

        //1
        for (int index = 0; index < length; index++) {
            int start = 0;
            List<Object> argList = new ArrayList<Object>();
            StringBuilder realSqlBuilder = new StringBuilder();

            //2
            while (matcher.find(start)) {
                realSqlBuilder.append(rawSql.substring(start, matcher.start()));

                String key = matcher.group(1);
                Object value = "";

                //3
                //StringParam 直接替换，不走批处理策略
                if (stringParamMap.containsKey(key)) {
                    int i = stringParamMap.get(key);

                    value = rawArgs[i].toString();
                } else { //SqlParam
                    value = "?";

                    if (sqlParamMap.containsKey(key)) { //标准的SqlParam
                        int i = sqlParamMap.get(key);

                        //4
                        argList.add(ReflectTool.getListObject(rawArgs[i], index));
                    } else if (key.contains(".")) { //特殊的SqlParam 例如 user.name
                        String params[] = key.split("\\.");

                        if (sqlParamMap.containsKey(params[0])) {
                            int i = sqlParamMap.get(params[0]);
                            Object param = ReflectTool.getListObject(rawArgs[i], index);
                            Object arg = ReflectTool.getFieldByName(param, params[1]);

                            //4
                            argList.add(arg);
                        } else if (Character.isDigit(params[0].charAt(0))) { //{0.id}, {1.name}
                            int i = Integer.valueOf(params[0]);
                            Object param = ReflectTool.getListObject(rawArgs[i], index);
                            Object arg = ReflectTool.getFieldByName(param, params[1]);

                            //4
                            argList.add(arg);
                        }
                    } else if (Character.isDigit(key.charAt(0))) { //{0}, {1}
                        try {
                            int i = Integer.valueOf(key);
                            Object arg = ReflectTool.getListObject(rawArgs[i], index);

                            //4
                            argList.add(arg);
                        } catch (Exception e) {
                            throw new ExecutorException(e);
                        }
                    } else {
                        throw new ExecutorException("param format error");
                    }
                }

                realSqlBuilder.append(value);
                start = matcher.end();
            }

            realSqlBuilder.append(rawSql.substring(start));

            if (0 == index) {
                sqlMetaModel.setSql(realSqlBuilder.toString());
            }

            argLists.add(argList.toArray());
        }

        sqlMetaModel.setArgsList(argLists);
    }

    /* for test */
    public static void main(String[] args) {
        SqlMetaModel sqlMetaModel = new SqlMetaModelBuilder(new MethodMetaModelBuilder(TestDao.class.getMethods()[0]).build(),
                new Object[] { 3, "abc", new TestClass() }).build();

        System.out.println(sqlMetaModel.getSql());
        System.out.println(Arrays.asList(sqlMetaModel.getArgsList().get(0)));
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
