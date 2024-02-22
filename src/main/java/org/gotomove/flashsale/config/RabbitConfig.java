package org.gotomove.flashsale.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @Author zhang
 * @Date 2024/2/21
 * @Description
 */
@Configuration
public class RabbitConfig {
    @Bean
    public Queue queue(){
        return new Queue("queue", true);
    }
}
