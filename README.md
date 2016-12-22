# light-dao

**20161222 更新**
1 增加新的sql调用方式
```
QueryCreator queryCreator = new QueryCreator(creatorExample.myDataSource);
System.out.println(queryCreator.select(InfoDao.Info.class)
        .from("info")
        .where("id = 1")
        .and("user_id = 2")
        .groupBy("id")
        .orderBy("user_id")
        .limit(1)
        .execute());
```

2 使用示例见example.CreatorExample

3 修复部分文件名大小写问题

---

**20160727 更新**

1 修复bug(在输出日志时产生的空指针问题)

2 将spring框架调整为<scope>provided</scope>，避免打包spring

3 增加返回值支持类型(Long, Integer 等wrapper类型)

4 增加映射时支持类型(Date 日期处理)

5 增加对于java8 default方法的支持

6 调整部分日志输出格式

---

**20160331新增功能**

支持批量插入，例如 insert(@ListParam("users") List<User> users);

---

**20160318新增功能**

将{0}, {1}替换为参数的第1,2个参数

---

light-dao是一个轻量级的orm处理框架。

它的核心思想是尽可能简单的使用原生sql。

它基于spring构建，可以和mybaties等其他数据处理框架配合使用。


一 由来

---

在我们的系统中，主要使用Mybaties作为ORM处理框架。但是Mybaties相对重量级，哪怕只是简单使用，仍需要配置大量的xml。比如下面这样的配置
```
countryList = sqlMapper.selectList("<script>" +
        "select * from country " +
        "   <where>" +
        "       <if test=\"id != null\">" +
        "           id < #{id}" +
        "       </if>" +
        "   </where>" +
        "</script>", country, Country.class);
```
在简单sql的情况下还可以接受。当sql再复杂一些，比如：
```
 <resultMap id="AssociationResultMap" type="com.test.mybatis.vo.MybatisOrder" >  
   <id column="ORDERID" property="orderid" jdbcType="DECIMAL" />  
   <result column="ORDERTYPE" property="ordertype" jdbcType="VARCHAR" />  
   <result column="ORDERDATE" property="orderdate" jdbcType="DATE" />  
  
<association property="customer" column="CUSTOMERID"   
    resultMap="com.test.mybatis.mapper.MybatiscustomerMapper.BaseResultMap"/>   
<collection property="itemList" column="ORDERID" javaType="ArrayList"   
    ofType="com.test.mybatis.vo.MybatisOrderItem"   
    resultMap="com.test.mybatis.mapper.MybatisOrderItemMapper.BaseResultMap"/>  
 </resultMap>  
 <select id="getOrderAssociation" parameterType="String" resultMap="AssociationResultMap">    
    SELECT *    
      FROM mybatisOrder ord LEFT JOIN mybatiscustomer customer ON ord.customerId = customer.ID   
      LEFT JOIN mybatisOrderItem item ON ord.orderid = item.orderid   
     WHERE ord.orderid = #{id}  
  </select>   
```
作为一个习惯了使用原生Sql的开发者来说，实在是难以接受这种xml的设定，尤其时当一些纯粹的数据查询语句使用极为复杂的sql时，再对sql进行一次xml配置实在是有心无力。比如下面的这个sql：
```
select (IFNULL(payment, 0) - IFNULL(repayment, 0)) balance from
 (select sum(amount) payment from payment_instruction_0000 where mifi_id in
  (select distinct(t1.mifi_id) mifi_id from overdue_record t1, loan_contract_0000 t2
      where t1.status = 1
        and (t1.con_id = t2.id and t2.product_id in (76)))
    and product_id in (76)) as pay,
 (select sum(amount) repayment from repay_detail_instruction_0000 where repay_type in (1, 3, 7, 10) and mifi_id in
   (select distinct(t1.mifi_id) mifi_id from overdue_record t1, loan_contract_0000 t2
       where t1.status = 1
        and (t1.con_id = t2.id and t2.product_id in (76)))
	and product_id in (76)) as repay;
```
如果想改成xml配置......

真的做不到啊.......

在一次次的被mybaties的复杂配置搞的头疼无奈之后；

在一次次的xml配置失败，无限重新上线之后；

在一次次吐槽无力后
——

终于下定决心自己写一套dao处理框架，这套框架中坚决不要再出现任何关于sql的xml。
而且要满足下面几个要求：

1 支持原始的sql使用

2 支持sql的变量替换

3 支持sql返回结果的映射

二 示例

---

开发了一月有余，目前差不多完成，也已经替换了线上的一些Mybaties部分。贴一下现在的示例代码
```
@Dao(dbName = "my_db")
public interface InfoDao {
    String TABLE_NAME = " info ";
    String ALL_COLUMN = " id, information, user_id ";
    String ALL_VALUES = " {param.id}, {param.information}, {param.userId} ";

    @Data
    class Info { //查询结果的Bean，也也可以是Thrift生成的Bean
        int id;
        String information;
        int userId;
    }

    @Execute("create table " + TABLE_NAME + " (id int, information varchar, user_id int)")
    void create();

    @Update("insert into " + TABLE_NAME + "(" +  ALL_COLUMN + ")" + " values(" + ALL_VALUES + ")")
    int insert(@SqlParam("param") Info info);

    @Data
    class UserInfo {
        String information;
        String name;
    }

    //跨表查询示例
    @Select("select information, name from info, user" 
        + " where info.user_id = user.id")
    List<UserInfo> selectUserInfo();
```
而针对上面代码的数据库配置只有如下的简单几行：
```
<bean id="myDbDataSource" class="org.apache.commons.dbcp.BasicDataSource"
		  destroy-method="close" lazy-init="false">
	<property name="driverClassName" value="org.h2.Driver"></property>
	<property name="url" value="jdbc:h2:mem:mifi_internal_account"></property>
	<property name="username" value="sa"></property>
	<property name="password" value=""></property>
</bean>
```
任你的sql再复杂，所有的配置也只是针对数据库的访问。
再贴一个复杂sql的使用情况:
```
@Select("select count(1) from (select t1.mifi_id, IFNULL(sum(amount), 0) payment from payment_instruction_0000 t1, "
            + " loan_contract_0000 t2 where (account_time >= {startTime} and account_time < {endTime})"
            + " and (t1.con_id = t2.id and t2.product_id in {productList})"
            + " group by mifi_id) as pay left join (select t1.mifi_id, sum(amount) repayment from repay_detail_instruction_0000 t1, "
            + " loan_contract_0000 t2 where (account_time >= {startTime} and account_time < {endTime})"
            + " and (t1.con_id = t2.id and t2.product_id in ({productList}))"
            + " and (t1.repay_type in (1, 3, 7, 10))"
            + " group by mifi_id) as repay on pay.mifi_id = repay.mifi_id where payment - IFNULL(repayment, 0) > 0")
long getBalanceNum(@SqlParam("startTime") long startTime, @SqlParam("endTime") long endTime, @StringParam("productList") String productList)
```
基本就像把原生sql直接传进去一样。

三 源码讲解：

---

源码基本上分为三层

1 spring层，用于向spring的BeanFactory注册用户自定义的Dao接口

2 executor层，将Dao接口中的每一个方法组装成一个一个的executor执行体

3 数据库层，执行实际的sql

结构图如下：
![层级结构图](http://upload-images.jianshu.io/upload_images/1397675-1cc02e510854638b.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

具体实现部分请参考源码，注释比较丰富，实现也相对比较直观。

四 使用方式

---

1 声明一个Dao接口类
```
@Dao(dbName = "my_db") //必须使用@Dao注解 必须指明数据库的名字(dbName)
public interface InfoDao     //接口名必须以Dao结尾(方便被Resolver发现)
```
2 将sql语句定义为方法
```
@Select("select id, name from " + TABLE_NAME + " where id = {id}")
User select(@SqlParam("id") int id); 
```
a sql语句的注解必须为@Select, @Update, @Execute三种之一，一般来说，select表示查询语句，update表示有写入的语句，比如insert和update，execute表示ddl语句，比如create table这种

b 在sql语句中，请将参数使用{}来标记。一般来说，可以标记正常的命名参数，比如{id}，还可以标记bean的属性参数，比如{user.name}

c 函数参数也有两种标记方式，@StringParam和@SqlParam，被@StringParam标记的参数将直接替换sql中同名的参数，比如"select {name} from user" @StringParma String name("littlersmall"),sql语句将被替换为"select littlersmall from user"。通常用于一些可变字符串的直接替换。
被@SqlParam标记的参数有两种形式，普通的命名参数和bean的属性参数，而且参数可以是任意类型，比如：
{id} @SqlParam int id;
{user.name} @SqlParam User user(无需再写user.name);
该方式主要用于sql的参数替换

d 返回值通常有几种：

(1) select类型，可以返回基本类型，比如int, long等，还可以返回用户自定义的Bean类型，比如User比如Thrift生成的bean，还可以返回多条结果，使用一个List，比如List<User>, List<String>

(2) Update类型，只能返回int，表示插入或修改的行数

(3) Execute类型无返回值

3 使用Dao接口
```
@Service //一定是被spring管理的类
public class UserDaoExample {    
    @Autowired    UserDao userDao;  //直接Autowired就好
    ...
}
```
4 数据库配置，请写在applicationContext.xml中，例如
```
<bean id="MyDbDataSource" class="org.apache.commons.dbcp.BasicDataSource"
	destroy-method="close" lazy-init="false">
	<property name="driverClassName" value="org.h2.Driver"></property>
	<property name="url" value="jdbc:h2:mem:mifi_internal_account"></property>
	<property name="username" value="sa"></property>
	<property name="password" value=""></property>
</bean>
```
a 请使用数据库名作为beanId的前缀，比如本例中，数据库的名字为my_db，使用大写的驼峰命名方式MyDb

b beanId结尾部分请使用DataSource

五 源码地址

---
github地址
>https://github.com/littlersmall/light-dao

请直接使用mvn package获得项目的jar包
或者通过mvn deploy的方式将其构建到自己的中央库中

六 参考

---

>项目参考了大量的网上资料，感谢baidu，google，bing，soso
参考了部分mybaties实现，部分spring实现，以及paoding-rose框架

七 总结

---

1 有一个大致的方向之后就应该马上动手，越拖延越没有动力

2 java的反射确实很强大

3 每个类，每个函数尽量只做一件事情

4 反复的重构很有必要，而且也很有收获

5 再复杂的代码，也是由最简单的东西一步步进化而来
