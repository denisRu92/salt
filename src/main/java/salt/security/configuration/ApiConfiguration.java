package salt.security.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class ApiConfiguration {
    @Value("${api.thread.pool.size:20}")
    private int threadPoolSize;
}
