package org.gotomove.flashsale.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.service.Header;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author zhang
 * @Date 2024/2/21
 * @Description
 */
@Configuration
public class RabbitMQConfig {
//    private static final String QUEUE1 = "queue01";
//    private static final String QUEUE2 = "queue02";
//    private static final String EXCHANGE1 = "fanoutExchange";
//    private static final String EXCHANGE2 = "directExchange";
//    private static final String EXCHANGE3 = "topicExchange";
//    private static final String EXCHANGE4 = "headersExchange";
//    private static final String ROUTINGKEY01 = "queue.red";
//    private static final String ROUTINGKEY02 = "queue.green";
//    private static final String ROUTINGKEY03 = "#.queue.#";
//    private static final String ROUTINGKEY04 = "*.queue.#";
//
//    // fanout交换机模式。消息队列发送给交换机之后，交换机给每一台绑定的队列发送消息
//    @Bean
//    public Queue queue01(){
//        return new Queue(QUEUE1, true);
//    }
//
//    @Bean
//    public Queue queue02() {
//        return new Queue(QUEUE2, true);
//    }
//
//    @Bean
//    public FanoutExchange fanoutExchange() {
//        return new FanoutExchange(EXCHANGE1);
//    }
//
//    @Bean
//    public Binding binding01() {
//        return BindingBuilder.bind(queue01()).to(fanoutExchange());
//    }
//
//    @Bean
//    public Binding binding02() {
//        return BindingBuilder.bind(queue02()).to(fanoutExchange());
//    }
//
//    // direct 交换机
//    @Bean
//    public DirectExchange directExchange() {
//        return new DirectExchange(EXCHANGE2);
//    }
//
//    @Bean
//    public Binding binding03() {
//        return BindingBuilder.bind(queue01()).to(directExchange()).with(ROUTINGKEY01);
//    }
//
//    @Bean
//    public Binding binding04() {
//        return BindingBuilder.bind(queue02()).to(directExchange()).with(ROUTINGKEY02);
//    }
//
//    // topic 交换机
//    @Bean
//    public TopicExchange topicExchange() {
//        return new TopicExchange(EXCHANGE3);
//    }
//
//    @Bean
//    public Binding binding05() {
//        return BindingBuilder.bind(queue01()).to(topicExchange()).with(ROUTINGKEY03);
//    }
//
//    @Bean
//    public Binding binding06() {
//        return BindingBuilder.bind(queue02()).to(topicExchange()).with(ROUTINGKEY04);
//    }
//
//    // headers 交换机
//    @Bean
//    public HeadersExchange headersExchange() {
//        return new HeadersExchange(EXCHANGE4);
//    }
//
//    @Bean
//    public Binding binding07() {
//        Map<String, Object> map = new HashMap<>();
//        map.put("color", "red");
//        map.put("speed", "low");
//        return BindingBuilder.bind(queue01()).to(headersExchange()).whereAll(map).match();
//    }
//
//    @Bean
//    public Binding binding08() {
//        Map<String, Object> map = new HashMap<>();
//        map.put("color", "red");
//        map.put("speed", "fast");
//        return BindingBuilder.bind(queue01()).to(headersExchange()).whereAll(map).match();
//    }
    private static final String QUEUE = "flashSaleQueue";
    private static final String EXCHANGE = "flashSaleExchange";

    @Bean
    public Queue queue01() {
        return new Queue(QUEUE);
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue01()).to(topicExchange()).with("flashSale.#");
    }
}
