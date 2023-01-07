package com.nirlvy.smart_freezer_backend.utils;

import java.util.Collections;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;

public class CodeGenerator {

    public static void main(String[] args) {
        generate();
    }

    private static void generate() {
        FastAutoGenerator
                .create("jdbc:mysql://localhost:3306/smart_freezer?serverTimeZone=GMT%2b8?allowPublicKeyRetrieval=true",
                        "root", "520624iL")
                .globalConfig(builder -> {
                    builder.author("nirlvy") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            // .fileOverride() // 覆盖已生成文件
                            .outputDir("/home/nirlvy/Documents/smart_freezer_total/smart_freezer_backend"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.nirlvy.smart_freezer_backend") // 设置父包名
                            .moduleName("") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml,
                                    "/home/nirlvy/Documents/smart_freezer_total/smart_freezer_backend/src/main/resources/mapper")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("user") // 设置需要生成的表名
                            .addTablePrefix("t_", "c_")// 设置过滤表前缀
                            .mapperBuilder().enableFileOverride();
                })
                .execute();
    }
}
