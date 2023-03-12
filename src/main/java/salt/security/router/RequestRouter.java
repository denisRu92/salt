package salt.security.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import salt.security.handler.RequestHandler;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class RequestRouter {
    @Bean
    public RouterFunction<ServerResponse> requestRoute(RequestHandler requestHandler) {
        return RouterFunctions.route(POST("/v1/model").and(accept(APPLICATION_JSON)), requestHandler::addModel)
                .andRoute(POST("/v1/validate").and(accept(APPLICATION_JSON)), requestHandler::validateRequest);
    }
}
