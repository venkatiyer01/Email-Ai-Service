package io.emailassistant.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import io.netty.resolver.DefaultAddressResolverGroup;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient() {

        HttpClient httpClient = HttpClient.create()
                // ✅ Fix DNS for Apple Silicon
                .resolver(DefaultAddressResolverGroup.INSTANCE)

                // ✅ Connection timeout
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)

                // ✅ Response timeout
                .responseTimeout(Duration.ofSeconds(30))

                // ✅ Read/Write timeout handlers
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(30))
                                .addHandlerLast(new WriteTimeoutHandler(30))
                );

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}