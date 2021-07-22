# Learning Mybatis

## 环境配置

- JDK 1.8
- Mysql 5.7
- maven 3.8.1
- IDEA

回顾：

- JDBC
- Mysql
- java基础
- maven知识
- jUnit

框架，配置文件的。最好的学习方式，看官方文档。

> [mybatis – MyBatis 3 | 入门](https://mybatis.org/mybatis-3/zh/getting-started.html)

# 1.简介

## 1.1 什么是Mybatis

<img src="C:\Users\27219\AppData\Roaming\Typora\typora-user-images\image-20210718210914033.png" alt="image-20210718210914033" style="zoom:80%;" />

- MyBatis 是一款优秀的**持久层框架**，
- 它支持自定义 SQL、存储过程以及高级映射。
- MyBatis 免除了几乎所有的 JDBC 代码以及设置参数和获取结果集的工作。
- MyBatis 可以通过简单的 XML 或注解来配置和映射原始类型、接口和 Java POJO（Plain Old Java Objects，普通老式 Java 对象）为数据库中的记录。
- MyBatis 本是apache的一个[开源项目](https://baike.baidu.com/item/开源项目/3406069)iBatis, 2010年这个[项目](https://baike.baidu.com/item/项目/477803)由apache software foundation 迁移到了[google code](https://baike.baidu.com/item/google code/2346604)，并且改名为MyBatis 。
- 2013年11月迁移到[Github](https://baike.baidu.com/item/Github/10145341)。

如何获得Mybatis？

- maven仓库

  ```xml
  <!-- https://mvnrepository.com/artifact/org.mybatis/mybatis -->
  <dependency>
      <groupId>org.mybatis</groupId>
      <artifactId>mybatis</artifactId>
      <version>3.5.6</version>
  </dependency>
  ```

- Github   [mybatis/mybatis-3: MyBatis SQL mapper framework for Java (github.com)](https://github.com/mybatis/mybatis-3)

- 中文文档  [mybatis – MyBatis 3 | 入门](https://mybatis.org/mybatis-3/zh/getting-started.html)

## 1.2 持久化

数据持久化

- 持久化就是将程序的数据在持久状态和瞬时状态转化的过程
- 内存：断电即失
- 数据库（JDBC），IO文件持久化
- 生活：冷藏，罐头

**为什么要持久化？**

- 有一些对象，不能让他丢掉
- 内存太贵了

## 1.3 持久层

Dao层，Service层，Controller层...

- 完成持久化的代码块
- 层界限十分明显

## 1.4 为什么需要Mybatis

- 方便
- 传统的JDBC代码太复杂了，简化框架，自动化
- 帮助程序员将数据存入数据库中
- 不用Mybatis也可以，更容易上手，技术没有高低之分
- 优点：简单易学：本身就很小且简单。没有任何第三方依赖，最简单安装只要两个jar文件+配置几个sql映射文件易于学习，易于使用，通过文档和源代码，可以比较完全的掌握它的设计思路和实现。
  灵活：mybatis不会对应用程序或者数据库的现有设计强加任何影响。 sql写在xml里，便于统一管理和优化。通过sql语句可以满足操作数据库的所有需求。
  解除sql与程序代码的耦合：通过提供DAO层，将业务逻辑和数据访问逻辑分离，使系统的设计更清晰，更易维护，更易单元测试。sql和代码的分离，提高了可维护性。
  提供映射标签，支持对象与数据库的orm字段关系映射
  提供对象关系映射标签，支持对象关系组建维护
  提供xml标签，支持编写动态sql。

# 2. 第一个Mybatis程序

思路：搭建环境---->导入mybatis---->编写代码--->测试

## 2.1搭建环境

搭建数据库

```sql
CREATE DATABASE mybatis;

USE mybatis

CREATE TABLE `user`(
	`id` INT(20) NOT NULL,
	`name` VARCHAR(30) DEFAULT NULL,
	`pwd` VARCHAR(30) DEFAULT NULL,
	PRIMARY KEY(`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8;

INSERT INTO `user`(`id`,`name`,`pwd`) VALUES
(1,'狂神','123456'),
(2,'张三','abcdef'),
(3,'李四','987654');
```

新建项目

1. 新建一个普通的maven项目

2. 删除src目录

   目的是新建一个子模块，不用再导入依赖

   ```xml
      <!--父工程-->
       <groupId>com.wangwang</groupId>
       <artifactId>learningMybatis</artifactId>
       <packaging>pom</packaging>
       <version>1.0-SNAPSHOT</version>
       <modules>
           <module>mybatis-01</module>
       </modules>
   
   ```

   

3. 导入maven依赖

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <project xmlns="http://maven.apache.org/POM/4.0.0"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
       <modelVersion>4.0.0</modelVersion>
       <!--父工程-->
       <groupId>com.wangwang</groupId>
       <artifactId>learningMybatis</artifactId>
       <version>1.0-SNAPSHOT</version>
   
       <properties>
           <maven.compiler.source>8</maven.compiler.source>
           <maven.compiler.target>8</maven.compiler.target>
       </properties>
       <!--导入依赖-->
       <dependencies>
           <!--mysql驱动-->
           <dependency>
               <groupId>mysql</groupId>
               <artifactId>mysql-connector-java</artifactId>
               <version>5.1.47</version>
           </dependency>
           <!--mybatis-->
           <dependency>
               <groupId>org.mybatis</groupId>
               <artifactId>mybatis</artifactId>
               <version>3.5.6</version>
           </dependency>
           <!--Junit-->
           <dependency>
               <groupId>junit</groupId>
               <artifactId>junit</artifactId>
               <version>4.13</version>
           </dependency>
       </dependencies>
   </project>
   ```


## 2.2 创建一个模块

> 编写mybatis的核心配置文件

XML 配置文件mybatis-config.xml中包含了对 MyBatis 系统的核心设置，包括获取数据库连接实例的数据源（DataSource）以及决定事务作用域和控制方式的事务管理器（TransactionManager）

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<!--configuration核心配置文件-->
<configuration>
    <environments default="development">
        <environment id="development">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <property name="driver" value="com.mysql.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql://localhost:3306/mybatis?useSSL=false&amp;useUnicode=true&amp;characterEncoding=UTF-8"/>
                <property name="username" value="root"/>
                <property name="password" value="root"/>
            </dataSource>
        </environment>
    </environments>
     <mappers>
        <mapper resource="com/yang/dao/UserMapper.xml"/>
    </mappers>
</configuration>

```

编写mybatis的工具类

```java
package com.yang.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

//SqlSessionFactory--->SqlSession
public class MybatisUtil {
    private static SqlSessionFactory sqlSessionFactory;
    static {
        try {
            String resource = "mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//    既然有了 SqlSessionFactory，顾名思义，我们可以从中获得 SqlSession 的实例。
//    SqlSession 提供了在数据库执行 SQL 命令所需的所有方法。
    public static SqlSession getSqlSession(){
        final SqlSession sqlSession = sqlSessionFactory.openSession();
        return sqlSession;
    }
}
```

## 2.3 编写代码

> 目录

<img src="C:\Users\27219\AppData\Roaming\Typora\typora-user-images\image-20210719204745413.png" alt="image-20210719204745413" style="zoom:67%;" />

![image-20210719211601663](C:\Users\27219\AppData\Roaming\Typora\typora-user-images\image-20210719211601663.png)

> 实体类

```java
package com.yang.pojo;

public class User {
    private int id;
    private String name;
    private String pwd;

    public User() {
    }

    public User(int id, String name, String pwd) {
        this.id = id;
        this.name = name;
        this.pwd = pwd;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pwd='" + pwd + '\'' +
                '}';
    }
}

```

> Dao接口

```java
public interface UserDao {
    List<User> getUserList();
}
```

> 接口实现类由原来的UserDaoImpl转变为一个Mapper配置文件

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!--namespace绑定一个对应的Dao/Mapper接口-->
<mapper namespace="com.yang.dao.UserDao">
<!--    select 查询语句-->
    <select id="getUserList" resultType="com.yang.pojo.User">
        select * from mybatis.user
    </select>
</mapper>
```

## 2.4 测试

注意点：

org.apache.ibatis.binding.BindingException: Type interface com.yang.dao.UserDao is not known to the MapperRegistry.

**MapperRegistry是什么？**

核心配置文件中配置Mappers

<img src="C:\Users\27219\AppData\Roaming\Typora\typora-user-images\image-20210719175825127.png" alt="image-20210719175825127" style="zoom: 67%;" />

另一个问题：maven由于约定大于配置，我们写的配置文件可能遇到无法导出或生效问题![image-20210719175946367](C:\Users\27219\AppData\Roaming\Typora\typora-user-images\image-20210719175946367.png)

解决方法：在pom.xml中插入下段代码

```xml
<!-- 解决Maven静态资源过滤问题 -->
    <build>
        <resources>
            <resource>
                <directory>src/main/resources</directory>
                <includes>
                    <include>**/*.properties</include>
                    <include>**/*.xml</include>
                </includes>
                <filtering>true</filtering>
            </resource>
            <resource>
                <directory>src/main/java</directory>
                <includes>
                    <include>**/*.properties</include>
                    <include>**/*.xml</include>
                </includes>
                <filtering>true</filtering>
            </resource>
        </resources>
</build>
```

> Junit测试

```java
package com.yang.dao;

import com.yang.pojo.User;
import com.yang.utils.MybatisUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

public class UserDaoTest {
    @Test
    public void test(){
        //第一步，获得SqlSession对象
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        //方式一：getMapper
        UserDao mapper = sqlSession.getMapper(UserDao.class);
        List<User> userList = mapper.getUserList();

        //方式二
//        List<User> userList = sqlSession.selectList("com.yang.dao.UserDao.getUserList");

        for (User user : userList) {
            System.out.println(user);
        }
        //关闭SqlSession
        sqlSession.close();
    }
}

```

方法一与方法二的区别：

<img src="C:\Users\27219\AppData\Roaming\Typora\typora-user-images\image-20210719210443645.png" alt="image-20210719210443645"  />

可能会遇到的问题：

1. 配置文件没有注册
2. 绑定接口错误
3. 方法名不对
4. 返回类型不对
5. maven导出资源问题

# 3. CRUD

## namespace

namespace中的包名要和DAO/Mapper接口中的包名一致

增删改需要提交事务

## 1.编写接口

```java
public interface UserMapper {
    //查询全部用户
    List<User> getUserList();

    //根据ID查询用户
    User getUserById(int id);

    //insert 一个用户
    int addUser(User user);

    //修改用户
    int UpdateUser(User user);

    //删除用户
    int DeleteUser(int id);
}
```

## 2.编写对应Mapper的SQL语句

```xml
<?xml version="1.0" encoding="UTF8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!--namespace绑定一个对应的Dao/Mapper接口-->
<mapper namespace="com.yang.dao.UserMapper">

<!--    select 查询语句-->
    <select id="getUserList" resultType="com.yang.pojo.User">
        select * from mybatis.user
    </select>
    <select id="getUserById" parameterType="int" resultType="com.yang.pojo.User">
        select * from mybatis.user where id= #{id}
    </select>
<!--对象中的属性可以直接取出来-->
    <insert id="addUser" parameterType="com.yang.pojo.User" >
        insert into user (id,name ,pwd)
        values (#{id},#{name},#{pwd});
    </insert>

    <update id="UpdateUser" parameterType="com.yang.pojo.User">
        update user
        set name = #{name},pwd = #{pwd}
        where id = #{id};
    </update>

    <delete id="DeleteUser" parameterType="int">
        delete
        from user
        where id = #{id};
    </delete>

</mapper>
```



## 3.测试

```java
package com.yang.dao;

import com.yang.pojo.User;
import com.yang.utils.MybatisUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

public class UserMapperTest {
    @Test
    public void test(){
        //第一步，获得SqlSession对象
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        //方式一：getMapper
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        List<User> userList = mapper.getUserList();

        //方式二
//        List<User> userList = sqlSession.selectList("com.yang.dao.UserDao.getUserList");

        for (User user : userList) {
            System.out.println(user);
        }
        //关闭SqlSession
        sqlSession.close();
    }
    @Test
    public void getUserById(){
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user = mapper.getUserById(1);
        System.out.println(user);
        sqlSession.close();
    }

//    增删改需要提交事务
    @Test
    public void addUser(){
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        int i = mapper.addUser(new User(4, "小光", "123123"));
        if(i>0) System.out.println("插入成功");
        // 提交事务
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void UpdateUser(){
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        int i = mapper.UpdateUser(new User(4, "明明", "123321"));
        if(i>0) System.out.println("修改成功");
        //提交事务
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void DeleteUser(){
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        int i = mapper.DeleteUser(4);
        if(i>0) {
            System.out.println("删除成功");
        }
        sqlSession.commit();
        sqlSession.close();

    }
}
```

## 4.万能mao

假设我们的实体类或数据库中的表，字段或参数过多，我们应当考虑使用map

```java
int addUser2(Map<String,Object> map);
```

```xml
<insert id="addUser2" parameterType="map">
    insert into user (id,name)
    values (#{userid},#{username});
</insert>
```

```java
public void addUser2(){
    SqlSession sqlSession = MybatisUtil.getSqlSession();
    UserMapper mapper = sqlSession.getMapper(UserMapper.class);
    HashMap<String, Object> map = new HashMap<>();
    map.put("userid",5);
    map.put("username","李伟");
    mapper.addUser2(map);
    sqlSession.commit();
    sqlSession.close();
}
```

Map传递参数，直接在sql取出即可   parameterType="map"

对象传递参数，直接在sql中取出对象的属性即可	parameterType="Object"

只有一个基本类型参数的情况下，可以直接在sql中取到

多个对象用map,**或者注解**

## 模糊查询

1.java代码执行的时候，传递通配符%%

```java
List<User> userList = mapper.getUserLike(%伟%);
```
2.在sql拼接中使用通配符

```java
    <select id="getUserLike" parameterType="String" resultType="com.yang.pojo.User">
        select * from mybatis.user where name like '%${value}%'
    </select>
```

> ${}表示拼接字符串，并且{}中只能使用value代表其中的参数

# 4.配置解析

## 4.1核心配置文件

**mybatis-config.xml**

```xml
properties（属性）
settings（设置）
typeAliases（类型别名）
typeHandlers（类型处理器）
objectFactory（对象工厂）
plugins（插件）
environments（环境配置）
environment（环境变量）
transactionManager（事务管理器）
dataSource（数据源）
databaseIdProvider（数据库厂商标识）
mappers（映射器）
```

## 4.2环境配置（environments）

[环境配置（environments）]: https://mybatis.org/mybatis-3/zh/configuration.html#environments

**不过要记住：尽管可以配置多个环境，但每个 SqlSessionFactory 实例只能选择一种环境**

学会使用多套环境配置

Mybatis 默认的事务管理器是JDBC，连接池：POOLED

## 4.3属性（properties）

[属性（properties）]: https://mybatis.org/mybatis-3/zh/configuration.html#properties

我们可以通过properties属性来实现引用配置文件

这些属性可以在外部进行配置，并可以进行动态替换。你既可以在典型的 Java 属性文件中配置这些属性，也可以在 properties 元素的子元素中设置{da.properties}

编写一个配置文件

db.properties

```properties
driver=com.mysql.jdbc.Driver
url=jdbc:mysql://localhost:3306/mybatis?useSSL=false&amp;useUnicode=true&amp;characterEncoding=UTf-8
user=root
password=root
```

在核心配置文件中引入

```xml
<!--引入外部配置文件不用配置其属性-->
    <properties resource="db.properties">
        <property name="username" value="hahaha"/>
        <property name="pwd" value="111111"/>
    </properties>
```

可以直接引入外部配置文件，也可以在其中增加一些属性，如果配置相同则优先读取内部属性，然后外部属性进行覆盖

## 4.4类型别名（typeAliases）

[类型别名（typeAliases）]: https://mybatis.org/mybatis-3/zh/configuration.html#typeAliases

类型别名可为 Java 类型设置一个缩写名字。 它仅用于 XML 配置，意在降低冗余的全限定类名书写。

```xml
    <typeAliases>
        <typeAlias type="com.yang.pojo.User" alias="User"/>
    </typeAliases>
```

也可以指定一个包名，MyBatis 会在包名下面搜索需要的 Java Bean,比如，扫描实体类的包，它的默认别名就为该实体类的类名首字母小写。

```xml
<typeAliases>
  <package name="com.yang.pojo"/>
</typeAliases>
```

在实体类比较少的时候使用第一种，多的时候使用第二种，第一种可以DIY别名，第二种只能用注解

![image-20210720095606455](C:\Users\27219\AppData\Roaming\Typora\typora-user-images\image-20210720095606455.png)

![image-20210720095633829](C:\Users\27219\AppData\Roaming\Typora\typora-user-images\image-20210720095633829.png)

![image-20210720095726048](C:\Users\27219\AppData\Roaming\Typora\typora-user-images\image-20210720095726048.png)

## 4.5设置（settings）

[设置（settings）]: https://mybatis.org/mybatis-3/zh/configuration.html#settings

<img src="C:\Users\27219\AppData\Roaming\Typora\typora-user-images\image-20210720175135974.png" alt="image-20210720175135974" style="zoom:67%;" />

## 4.6其他配置

- [typeHandlers（类型处理器）](https://mybatis.org/mybatis-3/zh/configuration.html#typeHandlers)
- [objectFactory（对象工厂）](https://mybatis.org/mybatis-3/zh/configuration.html#objectFactory)
- [plugins（插件）](https://mybatis.org/mybatis-3/zh/configuration.html#plugins)
  - MyBatis Generator Core
  - MyBatis Plus

## 4.7mappers（映射器）

[mappers（映射器）]: https://mybatis.org/mybatis-3/zh/configuration.html#mappers

MapperRegistry:注册绑定我们的Mapper文件；

方式一：【推荐使用】

```xml
<!--每一个Mapper。xml都需要在Mybatis核心配置文件中注册-->
<!-- 使用相对于类路径的资源引用 -->
    <mappers>
        <mapper resource="com/yang/dao/UserMapper.xml"/>
    </mappers>
```

方式二：使用class的方式注册

```xml
<!-- 使用映射器接口实现类的完全限定类名 -->    
	<mappers>
        <mapper class="com.yang.dao.UserMapper"/>
    </mappers>
```

注意点：

接口和配置文件必须同名，必须放在同一个包下，或者在resource下建一个同级目录的包

方式三：使用扫描包进行注入绑定

```xml
<!-- 将包内的映射器接口实现全部注册为映射器 -->
    <mappers>
        <package name="com.yang.dao"/>
    </mappers>

```

注意点：

接口和配置文件必须同名，必须放在同一个包下，或者在resource下建一个同级目录的包

## 4.8 生命周期和作用域

[生命周期和作用域]: https://mybatis.org/mybatis-3/zh/getting-started.html

生命周期和作用域是至关重要的，错误的使用会导致**非常严重的并发问题**

<img src="C:\Users\27219\AppData\Roaming\Typora\typora-user-images\image-20210720162145373.png" alt="image-20210720162145373" style="zoom: 67%;" />

#### SqlSessionFactoryBuilder

- 一旦创建了 SqlSessionFactory，就不再需要它了
- 局部方法变量

#### SqlSessionFactory

- 说白了就是可以想象成数据库连接池
- 一旦被创建就应该在应用的运行期间一直存在，**没有任何理由丢弃它或重新创建另一个实例**，多次重建 SqlSessionFactory 被视为一种代码“坏习惯”
- SqlSessionFactory 的最佳作用域是应用作用域
- 最简单的就是使用单例模式或者静态单例模式。

#### SqlSession

- 连接到连接池的一个请求！
- SqlSession 的实例不是线程安全的，因此是不能被共享的，所以它的最佳的作用域是请求或方法作用域
- 用完之后需要赶紧关闭，否则资源会被占用

<img src="C:\Users\27219\AppData\Roaming\Typora\typora-user-images\image-20210720162424473.png" alt="image-20210720162424473" style="zoom: 67%;" />

这里面的每一个MApper，就代表一个具体的业务

# 5.解决属性名与字段名不一致的问题

## 5.1问题

数据库中的字段

![image-20210720162642459](C:\Users\27219\AppData\Roaming\Typora\typora-user-images\image-20210720162642459.png)

新建一个项目，拷贝之前的，测试实体类字段不一致的情况

<img src="C:\Users\27219\AppData\Roaming\Typora\typora-user-images\image-20210720164327065.png" alt="image-20210720164327065" style="zoom: 67%;" /><img src="C:\Users\27219\AppData\Roaming\Typora\typora-user-images\image-20210720164440040.png" alt="image-20210720164440040" style="zoom:67%;" />

```java
//select * from mybatis.user where id= #{id}
//类型处理器
//select id,name,pwd from mybatis.user where id= #{id}
```

解决方法：

1.起别名

```java
//select * from mybatis.user where id= #{id}
//类型处理器
//select id,name,pwd as password from mybatis.user where id= #{id}
```

## 2.resultMap

结果集映射

```java
id name pwd (数据库字段)
id name password (实体类属性)
```

```java
    <resultMap id="UserMap" type="User">
        <!--column数据库中的字段，property实体类中的属性-->
        <result column="pwd" property="password"/>
    </resultMap>

<!--    select 查询语句-->
    <select id="getUserList" resultMap="UserMap">
        select * from mybatis.user
    </select>
```

`resultMap` 元素是 MyBatis 中最重要最强大的元素。它可以让你从 90% 的 JDBC `ResultSets` 数据提取代码中解放出来，并在一些情形下允许你进行一些 JDBC 不支持的操作。

ResultMap 的设计思想是，对简单的语句做到零配置，对于复杂一点的语句，只需要描述语句之间的关系就行了。

# 6.日志

## 6.1日志工厂

如果一个数据库操作出现了 异常我们需要排错，那么日志就是最好的助手！

曾经：sout  debug

现在：日志工厂

<img src="C:\Users\27219\AppData\Roaming\Typora\typora-user-images\image-20210720211501327.png" alt="image-20210720211501327" style="zoom:67%;" />

 SLF4J 

LOG4J 【掌握】

LOG4J2 

JDK_LOGGING

 COMMONS_LOGGING 

STDOUT_LOGGING【掌握】

 NO_LOGGING

在Mybatis中具体使用哪一个日志实现，在设置中设定！

**STDOUT_LOGGING标准日志输出**

在mybatis核心配置文件中，配置我们的日志

```xml
<settings>
<!--标志日志工厂实现-->
<setting name="logImpl" value="STDOUT_LOGGING"/>
</settings>
```

<img src="C:\Users\27219\AppData\Roaming\Typora\typora-user-images\image-20210720212222005.png" alt="image-20210720212222005" style="zoom:67%;" />

## 6.2Log4j

什么是log4j?

- Log4j是[Apache](https://baike.baidu.com/item/Apache/8512995)的一个开源项目，通过使用Log4j，我们可以控制日志信息输送的目的地是[控制台](https://baike.baidu.com/item/控制台/2438626)、文件、[GUI](https://baike.baidu.com/item/GUI)组件
- 我们可以控制每一条日志的输出格式；
- 通过定义每一条日志信息的级别，我们能够更加细致地控制日志的生成过程。
- 可以通过一个[配置文件](https://baike.baidu.com/item/配置文件/286550)来灵活地进行配置，而不需要修改应用的代码。

1.先导入log4j的包

```xml
<!-- https://mvnrepository.com/artifact/log4j/log4j -->
<dependency>
    <groupId>log4j</groupId>
    <artifactId>log4j</artifactId>
    <version>1.2.17</version>
</dependency>

```

2.log4j.properties

```properties
#将等级为DEBUG的日志信息输出到console和file这两个目的地，console和file的定义在下面的代码
log4j.rootLogger=DEBUG,console,file

#控制台输出的相关设置
log4j.appender.console = org.apache.log4j.ConsoleAppender
log4j.appender.console.Target = System.out
log4j.appender.console.Threshold=DEBUG
log4j.appender.console.layout = org.apache.log4j.PatternLayout
log4j.appender.console.layout.ConversionPattern=【%c】-%m%n

#文件输出的相关设置
log4j.appender.file = org.apache.log4j.RollingFileAppender
log4j.appender.file.File=./log/yang.log
log4j.appender.file.MaxFileSize=10mb
log4j.appender.file.Threshold=DEBUG
log4j.appender.file.layout=org.apache.log4j.PatternLayout
log4j.appender.file.layout.ConversionPattern=【%p】【%d{yy-MM-dd}】【%c】%m%n

#日志输出级别
log4j.logger.org.mybatis=DEBUG
log4j.logger.java.sql=DEBUG
log4j.logger.java.sql.Statement=DEBUG
log4j.logger.java.sql.ResultSet=DEBUG
log4j.logger.java.sql.PreparedStatement=DEBUG
```

3.配置log4j为日志的实现

```xm;
    <settings>
    <!--标志日志工厂实现-->
        <setting name="logImpl" value="LOG4J"/>
    </settings>
```

4.log4j的使用，直接测试运行刚才的查询

<img src="C:\Users\27219\AppData\Roaming\Typora\typora-user-images\image-20210720213201441.png" alt="image-20210720213201441" style="zoom:67%;" />

简单使用

1.再要使用log4j的类中，导入包import org.apache.log4j.Logger

2.日志对象，参数为当前类的class

```java
 static Logger logger = Logger.getLogger(UserMapperTest.class);
```

3.日志级别

```java
logger.info("info:进入了log4jtest");
logger.debug("debug:进入了log4jtest");
logger.error("error:进入了log4jtest");
logger.fatal("fatal:进入了log4jtest");
logger.trace("trace:进入了log4jtest");
logger.warn("warn:进入了log4jtest");
```

# 7.分页

**思考：为什么要分页？**      答：减少数据的处理量

## 7.1使用limit分页

```sql
select * from user limit startIndex,pageSize;
```

使用mybatis分页，核心SQL

1.接口

```java
//分页
List<User> getUserByLimit(Map<String,Integer> map);
```

2.Mapper.xml

```xml
   <!--分页-->
    <select id="getUserByLimit" parameterType="map" resultMap="UserMap">
        select * from user limit #{startIndex},#{pageSize}
    </select>
```

3.测试

```java
public void getUserByLimit(){
    SqlSession sqlSession = MybatisUtil.getSqlSession();
    UserMapper mapper = sqlSession.getMapper(UserMapper.class);
    HashMap<String, Integer> map = new HashMap<>();
    map.put("startIndex",2);
    map.put("pageSize",2);
    List<User> userByLimit = mapper.getUserByLimit(map);
    for (User user : userByLimit) {
        System.out.println(user);
    }
    sqlSession.close();
}
```

## 7.2 RowBounds分页

1.接口

```java
List<User> getUserByRowBounds();
```

2.mapper.xml

```xml
    <!--分页2-->
    <select id="getUserByRowBounds" resultMap="UserMap">
        select * from user
    </select>
```

3.测试

```java
    @Test
    public void getUserByRowBounds(){
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        //RowBounds实现
        RowBounds rowBounds = new RowBounds(1, 2);
        List<User> userList = sqlSession.selectList("com.yang.dao.UserMapper.getUserByRowBounds",null,rowBounds);
        for (User user : userList) {
            System.out.println(user);
        }
        sqlSession.close();
    }
```

## 7.3分页插件

<img src="C:\Users\27219\AppData\Roaming\Typora\typora-user-images\image-20210720214841326.png" alt="image-20210720214841326" style="zoom:67%;" />

# 8.使用注解开发

## 8.1 面向注解编程

- 大家之前都学过面向对象编程，也学习过接口，但是在真正的开发中，很多时候我们会选择面向接口编程
- **根本原因：--解耦--，可拓展，提高复用，分层开发中，上层不用管具体的实现，大家都遵守共同的标准，使得开发变得很容易，规范性更好**
- 在一个面向对象的系统中，系统的各种功能是由许许多多的不同对象协作完成的。在这种情况下，各个对象内部是如何实现自己的对系统设计人员来讲就不那么重要了。
- 而各个对象之间的协作关系则成为系统设计的关键，小到不同类之间的通信，达到各个模块之间的交互，在系统设计之初都是要着重考虑 的，这也是系统设计的主要工作内容，面向接口编程就是指按照这种思想来编程

#### 关于接口的理解

- 接口从更深层次的理解，应是定义（规范，约束）与实现（名实分离的原则）的分离
- 接口的本身反映了系统设计人员对系统的抽象理解
- 接口应该有两类：
  - 第一类是对一个个体的抽象，他可以对应为一个抽象体(Abstract class)
  - 第二类是对一个个体的某一方面的抽象，即形成一个抽象面(interface)
- 一个个体可能有多个抽象面，抽象体与抽象面是有区别的

## 8.2 使用注解开发

1.注解在接口上实现

```java
@Select("select * from user")
List<User> getUserList();
```

2.需要在核心配置文件中绑定接口

```xml
<mappers>
    <!--        <mapper resource="com/yang/dao/UserMapper.xml"/>-->
            <mapper class="com.yang.dao.UserMapper"/>
    <!--        <package name="com.yang.dao"/>-->
</mappers>
```

3.测试

本质：反射机制实现

底层：动态代理!

<img src="C:\Users\27219\AppData\Roaming\Typora\typora-user-images\image-20210720223857923.png" alt="image-20210720223857923" style="zoom:67%;" />

Mybatis详细执行流程

![image-20210720225403181](C:\Users\27219\AppData\Roaming\Typora\typora-user-images\image-20210720225403181.png)

编写接口增加注解

```java
    //根据ID查询用户
    @Select("select * from user where id = #{id} and name = #{name}")
    User getUserById(@Param("id") int id,@Param("name") String name);
```

#### 关于@param（）注解

基本类型的参数或者String类型，需要加上

引用类型不需要加

如果只有一个基本类型的话，可以忽略，但是建议大家都加上

我们在SQL中引用的就是我们这里的@param（）中设定的属性名



#{}可以防止SQL注入，${}则不可以

# 9.多对一处理

多个学生对应一个老师

对于学生而言，关联，多个学生关联一个老师【多对一】

查询方式：子查询，联表查询

对于老师而言，集合，一个老师有很多的学生【一对多】

sql：

```sql
CREATE TABLE `teacher` (
                           `id` INT(10) NOT NULL,
                           `name` VARCHAR(30) DEFAULT NULL,
                           PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

INSERT INTO teacher(`id`, `name`) VALUES (1, '秦老师');

CREATE TABLE `student` (
                           `id` INT(10) NOT NULL,
                           `name` VARCHAR(30) DEFAULT NULL,
                           `tid` INT(10) DEFAULT NULL,
                           PRIMARY KEY (`id`),
                           KEY `fktid` (`tid`),
                           CONSTRAINT `fktid` FOREIGN KEY (`tid`) REFERENCES `teacher` (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;
INSERT INTO `student` (`id`, `name`, `tid`) VALUES (1, '小明', 1);
INSERT INTO `student` (`id`, `name`, `tid`) VALUES (2, '小红', 1);
INSERT INTO `student` (`id`, `name`, `tid`) VALUES (3, '小张', 1);
INSERT INTO `student` (`id`, `name`, `tid`) VALUES (4, '小李', 1);
INSERT INTO `student` (`id`, `name`, `tid`) VALUES (5, '小王', 1);
```

<img src="C:\Users\27219\AppData\Roaming\Typora\typora-user-images\image-20210721100449652.png" alt="image-20210721100449652" style="zoom:80%;" />

#### 测试环境搭建(mybatis-06项目)

1.导入lombok

2.新建实体类Teacher，Student

3.建立Mapper接口

4.建立Mapper.xml 文件

5.在核心配置文件中绑定注册我们的Mapper接口或文件

6.查询测试是否能够成功

注意点：

```xml
    <mappers>
        <mapper resource="com/yang/dao/*Mapper.xml"/>
    </mappers>
报错问题：如果不结合spring使用mybatis不能用*mapper.xml，通配符是spring提供的功能
```

#### 按照查询嵌套处理

```java
<!--思路：
        1.查询所有的学生信息
        2.根据查询出来的tid,寻找对应的老师-->
    <select id="getStudent" resultMap="studentteacher">
        select * from student
    </select>
    <resultMap id="studentteacher" type="student">
        <result column="id" property="id"/>
        <result column="name" property="name"/>
        <!--复杂的属性我们需要单独处理
            对象：association
            集合：collection-->
        <association property="teacher" column="tid" javaType="teacher" select="getTeacher"/>
    </resultMap>
    <select id="getTeacher" resultType="teacher">
        select * from teacher 
    </select>
```

#### 按照结果嵌套处理

```xml
<!--按照结果嵌套处理-->
<select id="getStudent2" resultMap="studentteacher2">
    select s.id sid,s.name sname,t.name tname from student s,teacher t where s.tid=t.id
</select>
<resultMap id="studentteacher2" type="student">
    <result column="sid" property="id"/>
    <result column="sname" property="name"/>
    <association property="teacher" javaType="teacher">
        <result column="tname" property="teacher.name"/>
    </association>
</resultMap>
```

# 10.一对多处理

对老师而言就是一对多的关系！

1.环境搭建，和刚才一样（mybatis-07）

实体类

```java
public class Teacher {
    private int id;
    private String name;

    //一个老师有多个学生
    private List<Student> students;
}

public class Student {
    private int id;
    private String name;
    private int tid;
}
```

#### 按照结果嵌套处理

```xml
<select id="getTeacher1" parameterType="int" resultMap="teacherstudent">
    select s.id sid,s.name sname,t.id tid,t.name tname
    from student s,teacher t
    where s.tid= t.id and t.id = #{tid}
</select>
<resultMap id="teacherstudent" type="teacher">
    <result property="id" column="tid"/>
    <result property="name" column="tname"/>
    <!--javaType=""指定属性的类型
    指定泛型中的信息，我们使用oftype获取-->
    <collection property="students" ofType="student">
        <result property="id" column="sid"/>
        <result property="name" column="sname"/>
    </collection>
</resultMap>
```

#### 按照查询嵌套处理

```xml
<select id="getTeacher2" parameterType="int" resultMap="teacherstudent2">
    select * from teacher where id = #{tid}
</select>
<resultMap id="teacherstudent2" type="teacher">
    <collection property="students" javaType="ArrayList" ofType="Student" select="getStudentByTeacherID" column="id"/>
</resultMap>
<select id="getStudentByTeacherID" resultType="student">
    select * from student where tid = #{tid}
</select>
```

小结：

1.关联：association：【多对一】

2.集合：collection：【一对多】

3.javaType&ofType

1. JavaType用来指定实体类中属性的类型
2. ofType用来指定映射到List或者集合中的pojo类型，泛型中的约束类型

约束点：

保证SQL的可读性，尽量保证通俗易懂

注意一对多和多对一中，属性名和字段的问题

如果问题不好排查错误，建议使用日志

面试高频：

- mysql引擎
- innodb底层原理
- 索引及索引优化

# 11.动态SQL

[动态SQL]: https://mybatis.org/mybatis-3/zh/dynamic-sql.html

==**什么是动态SQL：动态SQL就是指根据不同的条件生成不同SQL语句**==

```properties
如果你之前用过 JSTL 或任何基于类 XML 语言的文本处理器，你对动态 SQL 元素可能会感觉似曾相识。在 MyBatis 之前的版本中，需要花时间了解大量的元素。借助功能强大的基于 OGNL 的表达式，MyBatis 3 替换了之前的大部分元素，大大精简了元素种类，现在要学习的元素种类比原来的一半还要少。
if
choose (when, otherwise)
trim (where, set)
foreach
```

#### 搭建环境

```sql
CREATE TABLE `blog`(
`id` VARCHAR(50) NOT NULL COMMENT 博客id,
`title` VARCHAR(100) NOT NULL COMMENT 博客标题,
`author` VARCHAR(30) NOT NULL COMMENT 博客作者,
`create_time` DATETIME NOT NULL COMMENT 创建时间,
`views` INT(30) NOT NULL COMMENT 浏览量
)ENGINE=INNODB DEFAULT CHARSET=utf8
```

创建一个基础工程（mybatis-08）

1.导包

2编写配置文件

3.编写实体类 

4.编写接口及对应Mapper.xml文件

#### IF

```xml
<select id="queryBlog" parameterType="map" resultType="blog">
    select * from blog where 1=1
    <if test="title != null">
        and title like #{title}
    </if>
    <if test="author != null">
        and  author like #{author}
    </if>
</select>
```

#### choose (when, otherwise)

有时候，我们不想使用所有的条件，而只是想从多个条件中选择一个使用。针对这种情况，MyBatis 提供了 choose 元素，它有点像 Java 中的 switch 语句。

还是上面的例子，但是策略变为：传入了 “title” 就按 “title” 查找，传入了 “author” 就按 “author” 查找的情形。若两者都没有传入，就返回标记为 featured 的 BLOG（这可能是管理员认为，与其返回大量的无意义随机 Blog，还不如返回一些由管理员精选的 Blog）

```xml
<select id="queryBlogChoose" parameterType="map" resultType="blog">
    select * from blog
    <where>
        <choose>
            <when test="title != null">
                and title like #{title}
            </when>
            <when test="author != null">
                and  author like #{author}
            </when>
            <otherwise>
                and views like #{views}
            </otherwise>
        </choose>
    </where>
</select>
```



#### trim (where, set)

```xml
<select id="queryBlogChoose" parameterType="map" resultType="blog">
    select * from blog
    <where>
        <if test="title != null">
            and title like #{title}
        </if>
        <if test="author != null">
            and  author like #{author}
        </if>
    </where>
</select>
```

![image-20210721230417768](C:\Users\27219\AppData\Roaming\Typora\typora-user-images\image-20210721230417768.png)

#### SQL片段

有的时候，我们可能会将一些功能的部分抽取出来，方便复用

1. 使用SQL标签抽取公共部分
2. 在需要使用的地方使用include标签引用即可

```xml
    <sql id="if-title-author">
        <if test="title != null">
            and title like #{title}
        </if>
        <if test="author != null">
            and  author like #{author}
        </if>
    </sql>
    <select id="queryBlogIF" parameterType="map" resultType="blog">
        select *
        from blog
        where 1=1
        <include refid="if-title-author"></include>
    </select>

```

注意事项：

- 最好基于单表来定义SQL标签
- 最好不要存在where标签

#### Foreach

![image-20210721232954904](C:\Users\27219\AppData\Roaming\Typora\typora-user-images\image-20210721232954904.png)

```xml
<!--    select * from blog where 1=1 and (id=1 or id=2 or id=3)
        我们现在传一个万能map， 这个map可以存储一个集合-->
    <select id="queryBlogForeach" parameterType="map" resultType="blog">
        select * from blog
        <where>
            <foreach collection="ids" item="id" open="and (" close=")" separator="or">
                id = #{id}
            </foreach>
        </where>
    </select>
```

==动态SQL就是在拼接SQL语句，我们只要保证SQL的正确性，按照SQL的格式，去排列组合就可以了==

建议：先在mysql中写出完整SQL，再对应的去修改成为我们的动态SQL

# 13、缓存（了解）

[缓存]: https://mybatis.org/mybatis-3/zh/sqlmap-xml.html#cache



## 13.1、简介

```
查询	：		连接数据库，耗资源！
		一次查询的结果，给他暂存在一个可以直接取到的地方！--->内存	：	缓存
		
我们再次查询相同数据的时候，直接走缓存，就不用走数据库了
```

1. 什么事缓存[Cache]?

   - 存在内存中的临时数据。

   - 将用户经常查询的数据放在缓存（内存）中，用户去查询数据就不用从磁盘上（关系型数据库数据文件）查询，

     从缓存中查询，从而提高查询效率，解决了高并发系统的性能问题。

2. 为什么使用缓存？

   - 减少和数据库的交互次数，减少系统开销，提高系统效率。

3. 什么样的数据能使用缓存？

   - 经常查询并且不经常改变的数据。

## 13.2、Mybatis缓存

<img src="C:\Users\27219\AppData\Roaming\Typora\typora-user-images\image-20210722102621764.png" alt="image-20210722102621764"  />

- MyBatis包含一个非常强大的查询缓存特性，它可以非常方便地定制和配置缓存。缓存可以极大的提升查询效率。

- MyBatis系统中默认定义了两级缓存：

  一级缓存

  和

  二级缓存

  - 默认情况下，只有一级缓存开启。（SqlSession级别的缓存，也称为本地缓存）
  - 二级缓存需要手动开启和配置，他是基于namespace级别的缓存。
  - 为了提扩展性，MyBatis定义了缓存接口Cache。我们可以通过实现Cache接口来自定义二级缓存

## 13.3、一级缓存

- 一级缓存也叫本地缓存：SqlSession
  - 与数据库同义词会话期间查询到的数据会放在本地缓存中。
  - 以后如果需要获取相同的数据，直接从缓存中拿，没有必要再去查询数据；

**测试步骤：**

1. 开启日志!

2. 测试在一个Session中查询两次相同的记录

3. 查看日志输出

   <img src="C:\Users\27219\AppData\Roaming\Typora\typora-user-images\image-20210722110124282.png" alt="image-20210722110124282" style="zoom:67%;" />

**缓存失效的情况：**

1. 查询不同的东西

2. 增删改操作，可能会改变原来的数据，所以必定会刷新缓存！

   <img src="C:\Users\27219\AppData\Roaming\Typora\typora-user-images\image-20210722112812169.png" alt="image-20210722112812169" style="zoom:67%;" />

3. 查询不同的Mapper.xml

4. 手动清理缓存！

   <img src="C:\Users\27219\AppData\Roaming\Typora\typora-user-images\image-20210722113022860.png" alt="image-20210722113022860" style="zoom: 67%;" /><img src="C:\Users\27219\AppData\Roaming\Typora\typora-user-images\image-20210722113102345.png" alt="image-20210722113102345" style="zoom: 67%;" />

小节：一级缓存默认是开启的，只在一次SqlSession中有效，也就是拿到连接到关闭连接这个区间段！

一级缓存就是一个Map。

## 13.4、二级缓存

- 二级缓存也叫全局缓存，一级缓存作用域太低了，所以诞生了二级缓存
- 基于namespace级别的缓存，一个名称空间，对应一个二级缓存；
- 工作机制
  - 一个会话查询一条数据，这个数据就会被放在当前会话的一级缓存中；
  - 如果当前会话关闭了，这个会话对应的一级缓存就没了；但是我们想要的是，会话关闭了，一级缓存中的数据会被保存到二级缓存中；
  - 新的会话查询信息，就可以从二级缓存中获取内容；
  - 不同的mapper查出的数据会放在自己对应的缓存（map）中；

步骤：

1. 开启全局缓存

   ```
   <!--显式的开启全局缓存-->
   <setting name="cacheEnabled" value="true"/>
   ```

2. 在要使用二级缓存的Mapper中开启

   ```
   <!--在当前Mapper.xml中使用二级缓存-->
   <cache/>
   也可以自定义参数
   <cache eviction="FIFO"
          flushInterval="60000"
          size="512"
          readOnly="true"/>
   ```

3. 测试

   1. 问题：我们需要将实体类序列化！否则就会报错

      >  java.io.NotSerializableException: com.rui.pojo.User

   2.解决方法：在实体类中实现序列化接口
   
   >  public class User implements Serializable {}



小结：

- 只要开启了二级缓存，在同一个Mapper下就有效
- 所有的数据都会先放在一级缓存中；
- 只有当会话提交，或者关闭的时候，才会提交到二级缓存中！

## 13.5、缓存原理

![image-20210722101434801](C:\Users\27219\AppData\Roaming\Typora\typora-user-images\image-20210722101434801.png)

## 13.6、自定义缓存-encache

```
EhCache 是一个纯Java的进程内缓存框架，具有快速、精干等特点，是Hibernate中默认的CacheProvider。
```

要在程序中使用ehcache，先要导包！

```
<!-- https://mvnrepository.com/artifact/org.mybatis.caches/mybatis-ehcache -->
<dependency>
    <groupId>org.mybatis.caches</groupId>
    <artifactId>mybatis-ehcache</artifactId>
    <version>1.1.0</version>
</dependency>
```

然后在mapper中指定使用ehcache缓存实现

```
<!--在当前Mapper.xml中使用二级缓存-->
<cache type="org.mybatis.caches.ehcache.EhcacheCache"/>
```

导入配置文件 ehcache.xml

```
<?xml version="1.0" encoding="UTF-8"?>
<ehcache xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:noNamespaceSchemaLocation="http://ehcache.org/ehcache.xsd"
         updateCheck="false">
    <!--
       diskStore：为缓存路径，ehcache分为内存和磁盘两级，此属性定义磁盘的缓存位置。参数解释如下：
       user.home – 用户主目录
       user.dir  – 用户当前工作目录
       java.io.tmpdir – 默认临时文件路径
     -->
    <diskStore path="java.io.tmpdir/Tmp_EhCache"/>
    <!--
       defaultCache：默认缓存策略，当ehcache找不到定义的缓存时，则使用这个缓存策略。只能定义一个。
     -->
    <!--
      name:缓存名称。
      maxElementsInMemory:缓存最大数目
      maxElementsOnDisk：硬盘最大缓存个数。
      eternal:对象是否永久有效，一但设置了，timeout将不起作用。
      overflowToDisk:是否保存到磁盘，当系统宕机时
      timeToIdleSeconds:设置对象在失效前的允许闲置时间（单位：秒）。仅当eternal=false对象不是永久有效时使用，可选属性，默认值是0，也就是可闲置时间无穷大。
      timeToLiveSeconds:设置对象在失效前允许存活时间（单位：秒）。最大时间介于创建时间和失效时间之间。仅当eternal=false对象不是永久有效时使用，默认是0.，也就是对象存活时间无穷大。
      diskPersistent：是否缓存虚拟机重启期数据 Whether the disk store persists between restarts of the Virtual Machine. The default value is false.
      diskSpoolBufferSizeMB：这个参数设置DiskStore（磁盘缓存）的缓存区大小。默认是30MB。每个Cache都应该有自己的一个缓冲区。
      diskExpiryThreadIntervalSeconds：磁盘失效线程运行时间间隔，默认是120秒。
      memoryStoreEvictionPolicy：当达到maxElementsInMemory限制时，Ehcache将会根据指定的策略去清理内存。默认策略是LRU（最近最少使用）。你可以设置为FIFO（先进先出）或是LFU（较少使用）。
      clearOnFlush：内存数量最大时是否清除。
      memoryStoreEvictionPolicy:可选策略有：LRU（最近最少使用，默认策略）、FIFO（先进先出）、LFU（最少访问次数）。
      FIFO，first in first out，这个是大家最熟的，先进先出。
      LFU， Less Frequently Used，就是上面例子中使用的策略，直白一点就是讲一直以来最少被使用的。如上面所讲，缓存的元素有一个hit属性，hit值最小的将会被清出缓存。
      LRU，Least Recently Used，最近最少使用的，缓存的元素有一个时间戳，当缓存容量满了，而又需要腾出地方来缓存新的元素的时候，那么现有缓存元素中时间戳离当前时间最远的元素将被清出缓存。
   -->
    <defaultCache
            eternal="false"
            maxElementsInMemory="10000"
            overflowToDisk="false"
            diskPersistent="false"
            timeToIdleSeconds="1800"
            timeToLiveSeconds="259200"
            memoryStoreEvictionPolicy="LRU"/>
 
    <cache
            name="cloud_user"
            eternal="false"
            maxElementsInMemory="5000"
            overflowToDisk="false"
            diskPersistent="false"
            timeToIdleSeconds="1800"
            timeToLiveSeconds="1800"
            memoryStoreEvictionPolicy="LRU"/>
 
</ehcache>
```
