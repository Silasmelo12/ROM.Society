package concept.com.example.club.integration.resend.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    private  static final Logger log =  LoggerFactory.getLogger(AsyncConfig.class);

    @Bean(name = "emailExecutor")
    public Executor emailExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(50);
        executor.setThreadNamePrefix("email-");
        executor.setKeepAliveSeconds(60);
        executor.setRejectedExecutionHandler(
                (runnable, pool) -> log.error("Fila de emails cheia. Email descartado.  Considere aumentar queueCapacity."
                ));
        executor.initialize();
        return executor;
    }

}
