package com.pt.share.core.config;

import cn.hutool.core.lang.Snowflake;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName HutoolConfig
 * @Description TODO
 * @Author potato
 * @Date 2020/12/15 下午9:19
 **/
@Configuration
public class HutoolConfig {
    @Bean
    public Snowflake snowflake(){
        return new Snowflake(0,0,true);
    }

}
