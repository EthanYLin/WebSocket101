package com.example.websocket101;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import com.corundumstudio.socketio.Configuration;

@org.springframework.context.annotation.Configuration
public class SocketIOConfig {
    @Value("${rt-server.host}")
    private String host;

    @Value("${rt-server.port}")
    private Integer port;

    @Bean
    public SocketIOServer socketIOServer() {
        Configuration config = new Configuration();
        //config.setHostname(host);
        config.setPort(port);

        SocketIOServer server = new SocketIOServer(config);
        server.start();

        return server;
    }

    @Bean
    public SpringAnnotationScanner springAnnotationScanner() {
        return new SpringAnnotationScanner(socketIOServer());
    }
}
