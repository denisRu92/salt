package salt.security.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.Exceptions;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import salt.security.configuration.ApiConfiguration;
import salt.security.model.Model;
import salt.security.model.ValidationRequest;
import salt.security.service.ModelService;
import salt.security.service.ValidationService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@Component
public class RequestHandler {
    private final ExecutorService executorService;
    private final ModelService modelService;
    private final ValidationService validationService;

    @Autowired
    public RequestHandler(ApiConfiguration apiConfiguration, ModelService modelService, ValidationService validationService) {
        this.executorService = Executors.newFixedThreadPool(apiConfiguration.getThreadPoolSize());
        this.modelService = modelService;
        this.validationService = validationService;
    }

    public Mono<ServerResponse> addModel(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(Model.class)
                .publishOn(Schedulers.fromExecutorService(executorService))
                .map(modelService::store)
                .flatMap(stored -> {
                    if (stored) {
                        return ServerResponse
                                .ok()
                                .build();
                    } else {
                        return ServerResponse
                                .badRequest()
                                .build();
                    }
                });
    }

    public Mono<ServerResponse> validateRequest(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(ValidationRequest.class)
                .publishOn(Schedulers.fromExecutorService(executorService))
                .map(request -> {
                    try {
                        return validationService.validate(request);
                    } catch (RuntimeException e) {
                        throw Exceptions.propagate(e);
                    }
                })
                .flatMap(response -> ServerResponse
                        .ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(response))
                .onErrorResume(error -> ServerResponse
                        .badRequest()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(error));
    }
}

