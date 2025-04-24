package org.example.langchain4j.generator;


import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

public class CodeGenerator {
    public static void main(String[] args) {
        // 使用 FastAutoGenerator 快速配置代码生成器
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/xiaozhi?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf8",
                        "root", "123456")
                .globalConfig(builder -> {
                    builder.author("liu bo") // 设置作者
                            .outputDir("src/main/java"); // 输出目录（Java 文件）
                })
                .packageConfig(builder -> {
                    builder.parent("org.example.langchain4j") // 设置父包名
                            .entity("entity") // 设置实体类包名
                            .mapper("mapper") // 设置 Mapper 接口包名
                            .xml("mapper") // 设置 Mapper XML 文件包名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, "src/main/resources/mapper"))
                            .build();// 设置 Mapper XML 文件的输出路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("appointment") // 设置需要生成的表名
                            .entityBuilder()
                            .enableLombok() // 启用 Lombok
                            .enableTableFieldAnnotation() // 启用字段注解
                            .controllerBuilder().disable() // 禁用 Controller 生成
                            .serviceBuilder().disable() // 禁用 Service 生成
                            .disableServiceImpl(); // 禁用 ServiceImpl 生成
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用 Freemarker 模板引擎
                .execute(); // 执行生成
    }

}