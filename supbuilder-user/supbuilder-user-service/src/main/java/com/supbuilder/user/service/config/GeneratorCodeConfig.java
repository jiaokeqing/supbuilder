package com.supbuilder.user.service.config;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.TemplateType;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.Arrays;
import java.util.List;

/**
 * @author 王广帅
 * @since 2022/8/19 0:13
 */
public class GeneratorCodeConfig {
    public static void main(String[] args) {
        // 要生成的数据库表名
        List<String> tableNames = Arrays.asList("sup_user");
        // 创建代码生成工具类
        FastAutoGenerator generator = create(tableNames);
        // 执行生成代码
        generator.execute();
    }

    private static FastAutoGenerator create(List<String> tableNames) {
        // 数据库连接地址，
        String url = "jdbc:mysql://localhost:3306/supbuilder?serverTimezone=GMT%2B8&useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true";
        // 数据库用户名
        String name = "root";
        // 数据库密码
        String password = "123456";
        FastAutoGenerator generator = FastAutoGenerator.create(url, name, password)
                // 全局配置
                .globalConfig(builder -> {
                    // 获取生成的代码路径,这里没有写死，是运行时获取的，这样可以防止不同的开发人员来回修改生成路径的问题。
                    String outputDir = System.getProperty("user.dir") + "/supbuilder-user/supbuilder-user-service/src/main/java";
                    builder.outputDir(outputDir)
                            .dateType(DateType.ONLY_DATE)
                            // 生成的类注释中的作者名称，为了统一表示，这里写死了
                            .author("Auto Generator");
                })
                // 生成的代码包路径配置
                .packageConfig(builder -> {
                    // 生成的代包公共路径
                    builder.parent("com.supbuilder.user.service");
                    // 生成的mapper xml的存放目录，是在parent路径下面的
                    builder.xml("mapper")
                            // 生成的实例类目录
                            .entity("domain")
                            // 生成的service目录
                            .service("service")
                            // 生成的ampper目录
                            .mapper("mapper");

                }).strategyConfig(builder -> {
                    // 添加要生成的的数据库表
                    builder.addInclude(tableNames)
                            // 启用大写模式
                            .enableCapitalMode();
                    // 配置生成的实体类策略，不生成serialVersionID
                    builder.entityBuilder().disableSerialVersionUID()
                            // 如果数据库表名带下划线，按驼峰命名法
                            .columnNaming(NamingStrategy.underline_to_camel)
                            // 使用lombok
                            .enableLombok()
                            // 标记实例类的主键生成方式，如果插入时没有指定，刚自动分配一个，默认是雪花算法
                            .idType(IdType.ASSIGN_ID)
                            // 实例类每次生成的时候，覆盖旧的实体类
                            .enableFileOverride()
                            // 指定生成的实体类名称
                            .convertFileName(entityName -> entityName)
                            // 指定生成的service接口名称
                            .serviceBuilder().convertServiceFileName(entityName -> "I" + entityName + "Service")
                            // 指定生成的serviceImpl的名称
                            .convertServiceImplFileName(entityName -> entityName + "Service");

                }).templateConfig(builder -> {
                    // 不生成Controller
                    builder.disable(TemplateType.CONTROLLER);
                });
        return generator;
    }

}

