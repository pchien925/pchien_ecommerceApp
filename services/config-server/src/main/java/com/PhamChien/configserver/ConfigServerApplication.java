package com.PhamChien.configserver;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class ConfigServerApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class, args);
    }
    @Override
    public void run(String... args) throws Exception {
        Dotenv dotenv = Dotenv.configure()
                .directory("D:/DoAnCNTT/ECommerceApp/services/config-server")
                .load();

        String dbUsername = dotenv.get("DB_USERNAME");
        String dbPassword = dotenv.get("DB_PASSWORD");
        String dbUrl = dotenv.get("DB_URL");
        String serverPort = dotenv.get("SERVER_PORT");

        System.out.println("DB Username: " + dbUsername);
        System.out.println("DB Password: " + dbPassword);
        System.out.println("DB URL: " + dbUrl);
        System.out.println("Server Port: " + serverPort);
    }
}


