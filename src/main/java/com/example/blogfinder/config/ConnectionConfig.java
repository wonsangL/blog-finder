package com.example.blogfinder.config;

import io.netty.channel.ChannelOption;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Configuration
public class ConnectionConfig {
    @Value("${httpclient.response-timeout-seconds}")
    private int responseTimeoutSecond;

    @Value("${httpclient.connection-timeout-millis}")
    private int connectionTimeoutMillis;

    @Bean
    public HttpClient httpClient() {
        return HttpClient.create()
                .responseTimeout(Duration.ofSeconds(responseTimeoutSecond))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectionTimeoutMillis);
    }
}
