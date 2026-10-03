package org.example;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class ControllerConfiguration {

    @Bean
    public TestController testController() {
        return new TestController();
    }
}
