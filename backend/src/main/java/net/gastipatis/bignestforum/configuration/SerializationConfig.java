package net.gastipatis.bignestforum.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.problem.jackson.ProblemModule;

@Configuration
public class SerializationConfig {

    @Bean
    public ProblemModule problemModule() {
        return new ProblemModule();
    }
}
