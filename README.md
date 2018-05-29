## restassured + testng + aspectj + allure

### 框架特点
- 数据驱动
- 切面封装 [AspectJ][0]
- DSL语法, thanks to [restassured][1]
- 灵活+扩展方便
  
[0]: https://eclipse.org/aspectj/
[1]: https://github.com/rest-assured/rest-assured/wiki/Usage
  
## 使用方法

1 引入maven工程

~~~xml
<dependency>
  <groupId>com.microdev.automation</groupId>
  <artifactId>restng</artifactId>
  <version>0.0.1-SNAPSHOT</version>
  <scope>test</scope>
</dependency>
~~~

2 编写用例

3 启动

```sh
mvn clean test
```

4 查看报告

```sh
mvn io.qameta.allure:allure-maven:serve 
```

[报告demo][2]

[2]: http://10.247.18.82:8080/jenkins/job/RestNgTest/allure/

## 用例说明

#### 支持2种用例形式:
- 关键字驱动
- 代码+数据驱动

#### 为什么要使用**代码+数据驱动**?
- 灵活性
- 扩展性
- 减少代码冗余
- 开源框架无缝接入。

#### 为什么要使用aop的思想做封装?

- 代码更加简洁优雅
- 不需要修改旧用例

[关于AspectJ，你需要知道的一切][4]

[4]: http://linbinghe.com/2017/65db25bc.html