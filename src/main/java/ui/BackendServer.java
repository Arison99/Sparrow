package ui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"ui", "gateway", "hardware", "core", "handlers", "adapters"})
public class BackendServer {
    public static void main(String[] args) {
        SpringApplication.run(BackendServer.class, args);
    }
}
