

# 超级创作

🌩最全面的办公辅助工具⚡️supbuilder

<!-- PROJECT SHIELDS -->
[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
[![MIT License][license-shield]][license-url]

[comment]: <> ([![LinkedIn][linkedin-shield]][linkedin-url])

<!-- PROJECT LOGO -->
<br />

<p align="center">
  <a href="https://github.com/jiaokeqing/zxduck/">
    <img src="/readme/images/logo.png" alt="Logo" width="100" height="100">
  </a>
</p>

<h3 align="center">超级创作</h3>
  <p align="center">
    一个全能的文档辅助软件去满足您的办公需求！
    <br />
    <a href="https://github.com/jiaokeqing/supbuilder"><strong>探索本项目的文档 »</strong></a>
    <br />
    <br />
    <a href="https://github.com/jiaokeqing/supbuilder">查看Demo</a>
    ·
    <a href="https://github.com/jiaokeqing/supbuilder/issues">报告Bug</a>
    ·
    <a href="https://github.com/jiaokeqing/supbuilder/issues">提出新特性</a>
  </p>



本篇README.md面向开发者

## 目录

- [上手指南](#上手指南)
    - [开发前的配置要求](#开发前的配置要求)
    - [安装步骤](#安装步骤)
- [文件目录说明](#文件目录说明)
- [开发的架构](#开发的架构)
- [部署](#部署)
- [使用到的框架](#使用到的框架)
- [使用到的插件](#使用到的插件)
- [框架规范约定](#框架规范约定)
- [Service层开发规范](#Service层开发规范)
- [Dao层开发规范](#Dao层开发规范)
- [异常处理规范](#异常处理规范)
- [单元测试规范](#单元测试规范)

- [贡献者](#贡献者)
    - [如何参与开源项目](#如何参与开源项目)
- [版本控制](#版本控制)
- [作者](#作者)
- [鸣谢](#鸣谢)

### 上手指南
###### 开发前的配置要求
###### 开发环境
+ JDK 1.8
+ Mysql 5.7
+ Maven 3.6.3
+ Redis 3.2
###### 开发工具
+ IDEA
+ PostMan


###### **安装步骤**

1. Clone the repo

```sh
git clone https://github.com/jiaokeqing/supbuilder.git
```

### 文件目录说明
```
supbuilder
├── readme              //README相关内容
├── supbuilder-common   //公共模块
├── supbuilder-core     //核心模块
├── supbuilder-db       //数据连接模块
├── supbuilder-wx       //微信端模块
├── pom.xml
└── README.md

```





### 开发的架构

正在全力完善中...

### 部署

暂无

### 使用到的框架

- [Springboot](https://spring.io/projects/spring-boot/)
- [SpringCloud](https://spring.io/projects/spring-cloud/)
- [SpringCloudAlibaba](https://github.com/alibaba/spring-cloud-alibaba/)
- [Spring Security](https://spring.io/guides/tutorials/spring-security-and-angular-js/)
- [Mybatis](https://mybatis.org/mybatis-3/)
- [Swagger](https://swagger.io/)

### 使用到的插件
-  Alibaba Java Coding Guidelines
-  lombok
-  mybatis-generator
### Service层开发规范
+ 方法参数注意事项：
1.必须使用强类型。
2.超过 4 个参数时使用数据模型类进行封装。
3.不允许使用复杂对象，如：Request、Response 等。
+ 方法返回值注意事项：
1.不允许返回 ApiResult
+ 不应该包含非业务逻辑相关的代码
+ 不能包含 DAO 层实现代码
+ 不要在 private 方法上编写注解
+ Service 类不应该继承包含增删改善功能的基类
### Dao层开发规范
+ 数据分页必须在数据库层面进行。
+ 不使用存储过程。
### 异常处理规范
+ DAO 层不需要手动抛出异常，若有底层数据访问类，可在底层封装 DAOException，DAO层可以不捕获异常，若捕获必须向上传递异常，在该层不需要打印异常日志。
+ Service 层可以捕获异常，在一些情况下可通过异常控制代码流程，比如通过参数校验异常控制程序不往下执行，通过这些方式，减少 if 代码，Service 层捕获异常后可向上传递异常，若不传递则必须记录异常日志，若向上传递则不记录日志。
+ Controller 层与 Service 层的异常处理方式一致，向上传递异常时，由 Controller 上的切面进行日志记录和返回结果的包装。
+ 原则：自行处理异常必须记录日志（完整异常堆栈），向上传递异常则不需要，DAO 层必须向上传递异常。
+ 不允许忽略异常（try 后什么都不做）
+ 一级自定义异常继承自 RunTimeException 
### 单元测试规范
+ 单元测试必须遵守 AIR（Automatic、Independent、Repeatable） 原则。
+ 单元测试必须保证依赖的测试环境是受控的，测试用例执行的成功与否是符合预期的。
+ 代码仓库中的单元测试执行失败时会阻碍后续工作的进行，一旦出错必须立即解决。

### 框架规范约定

约定优于配置(convention over configuration)，此框架约定了很多编程规范，下面一一列举：

```

- service类，需要在叫名`service`的包下，并以`Service`结尾，如`CmsArticleServiceImpl`

- controller类，需要在以`controller`结尾的包下，类名以Controller结尾，如`CmsArticleController.java`，并继承`BaseController`

- spring task类，需要在叫名`task`的包下，并以`Task`结尾，如`TestTask.java`

- mapper.xml，需要在名叫`mapper`的包下，并以`Mapper.xml`结尾，如`CmsArticleMapper.xml`

- mapper接口，需要在名叫`mapper`的包下，并以`Mapper`结尾，如`CmsArticleMapper.java`

- model实体类，需要在名叫`model`的包下，命名规则为数据表转驼峰规则，如`CmsArticle.java`

- spring配置文件，命名规则为`applicationContext-*.xml`

- 类名：首字母大写驼峰规则；方法名：首字母小写驼峰规则；常量：全大写；变量：首字母小写驼峰规则，尽量非缩写

- springmvc配置加到对应模块的`springMVC-servlet.xml`文件里

- 配置文件放到`src/main/resources`目录下

- 静态资源文件放到`src/main/webapp/resources`目录下

- jsp文件，需要在`/WEB-INF/jsp`目录下

- `RequestMapping`和返回物理试图路径的url尽量写全路径，如：`@RequestMapping("/manage")`、`return "/manage/index"`

- `RequestMapping`指定method

- 模块命名为`项目`-`子项目`-`业务`，如`zheng-cms-admin`

- 数据表命名为：`子系统`_`表`，如`cms_article`

- 更多规范，参考[[阿里巴巴Java开发手册] http://git.oschina.net/shuzheng/zheng/attach_files

```
### 贡献者

- [jiaokeqing](https://github.com/jiaokeqing)

#### 如何参与开源项目

贡献使开源社区成为一个学习、激励和创造的绝佳场所。你所作的任何贡献都是**非常感谢**的。


1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request



### 版本控制

该项目使用Git进行版本管理。您可以在repository参看当前可用版本。

### 作者

13583243451@163.com

[comment]: <> (知乎:xxxx  &ensp; qq:xxxxxx    )

*您也可以在贡献者名单中参看所有参与该项目的开发者。*

### 版权说明

[comment]: <> (该项目签署了MIT 授权许可，详情请参阅 [LICENSE.txt]&#40;https://github.com/jiaokeqing/supbuilder/LICENSE.txt&#41;)
**版权所有，禁止商用**

### 鸣谢


- [GitHub Emoji Cheat Sheet](https://www.webpagefx.com/tools/emoji-cheat-sheet)
- [Img Shields](https://shields.io)
- [Choose an Open Source License](https://choosealicense.com)
- [GitHub Pages](https://pages.github.com)
- [Animate.css](https://daneden.github.io/animate.css)

<!-- links -->
<!-- links -->
[your-project-path]:jiaokeqing/supbuilder
[contributors-shield]: https://img.shields.io/github/contributors/jiaokeqing/supbuilder.svg?style=flat-square
[contributors-url]: https://github.com/jiaokeqing/supbuilder/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/jiaokeqing/supbuilder.svg?style=flat-square
[forks-url]: https://github.com/jiaokeqing/supbuilder/network/members
[stars-shield]: https://img.shields.io/github/stars/jiaokeqing/supbuilder.svg?style=flat-square
[stars-url]: https://github.com/jiaokeqing/supbuilder/stargazers
[issues-shield]: https://img.shields.io/github/issues/jiaokeqing/supbuilder.svg?style=flat-square
[issues-url]: https://github.com/jiaokeqing/supbuilder/issues
[license-shield]: https://img.shields.io/github/license/jiaokeqing/supbuilder.svg?style=flat-square
[license-url]: https://github.com/jiaokeqing/supbuilder/blob/640e857a3ee82ebaf13fe9d18698940c1f548fe1/LICENSE
[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=flat-square&logo=linkedin&colorB=555
[linkedin-url]: https://linkedin.com/in/shaojintian


