package com.supbuilder.mp.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Administrator
 * @version 1.0
 **/
@Configuration
@MapperScan("cn.itcast.mp.mapper")
public class MybatisPlusConfig {

    //配置分页插件的bean
    @Bean
    public PaginationInterceptor paginationInterceptor(){
        return new PaginationInterceptor();
    }
}
