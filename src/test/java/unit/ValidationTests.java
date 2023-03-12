package unit;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import salt.security.Application;
import salt.security.model.Model;
import salt.security.model.ValidationRequest;
import salt.security.model.ValidationResponse;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

@SpringBootTest(classes = {Application.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@TestPropertySource(locations = "/application.yaml")
public class ValidationTests {
    private final ObjectMapper objectMapper = JsonMapper.builder().build();

    @Autowired
    private WebTestClient client;

    @Before
    public void initModels() throws IOException {
        String inputFileName = Objects.requireNonNull(ValidationTests.class.getClassLoader().getResource("testdata/all_models.json")).getFile();

        try (Reader fileReader = new FileReader(inputFileName, StandardCharsets.UTF_8)) {
            List<Model> models = objectMapper.readValue(fileReader, new TypeReference<>() {});

            models.forEach(model -> client
                    .post()
                    .uri("/v1/model")
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .bodyValue(model)
                    .exchange()
                    .expectStatus().isOk());
        }
    }

    @Test
    public void validRequest() throws IOException {
        String inputFileName = Objects.requireNonNull(getClass().getClassLoader().getResource("testdata/request_valid.json")).getFile();

        try (Reader fileReader = new FileReader(inputFileName, StandardCharsets.UTF_8)) {
            ValidationRequest validationRequest = objectMapper.readValue(fileReader, ValidationRequest.class);
            client
                    .post()
                    .uri("/v1/validate")
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .bodyValue(validationRequest)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(ValidationResponse.class)
                    .value(ValidationResponse::isValid, Matchers.equalTo(true))
                    .value(response -> response.getAbnormalFields().isEmpty(), Matchers.equalTo(true));
        }
    }

    @Test
    public void abnormalTypeRequest() throws IOException {
        String inputFileName = Objects.requireNonNull(getClass().getClassLoader().getResource("testdata/request_abnormal_type.json")).getFile();

        try (Reader fileReader = new FileReader(inputFileName, StandardCharsets.UTF_8)) {
            ValidationRequest validationRequest = objectMapper.readValue(fileReader, ValidationRequest.class);
            client
                    .post()
                    .uri("/v1/validate")
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .bodyValue(validationRequest)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(ValidationResponse.class)
                    .value(ValidationResponse::isValid, Matchers.equalTo(false))
                    .value(response -> response.getAbnormalFields().isEmpty(), Matchers.equalTo(false));
        }
    }

    @Test
    public void abnormalRequirementRequest() throws IOException {
        String inputFileName = Objects.requireNonNull(getClass().getClassLoader().getResource("testdata/request_abnormal_requirement.json")).getFile();

        try (Reader fileReader = new FileReader(inputFileName, StandardCharsets.UTF_8)) {
            ValidationRequest validationRequest = objectMapper.readValue(fileReader, ValidationRequest.class);
            client
                    .post()
                    .uri("/v1/validate")
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .bodyValue(validationRequest)
                    .exchange()
                    .expectStatus()
                    .isOk()
                    .expectBody(ValidationResponse.class)
                    .value(ValidationResponse::isValid, Matchers.equalTo(false))
                    .value(response -> response.getAbnormalFields().isEmpty(), Matchers.equalTo(false));
        }
    }

    @Test
    public void invalidRequest() throws IOException {
        String inputFileName = Objects.requireNonNull(getClass().getClassLoader().getResource("testdata/request_invalid.json")).getFile();

        try (Reader fileReader = new FileReader(inputFileName, StandardCharsets.UTF_8)) {
            ValidationRequest validationRequest = objectMapper.readValue(fileReader, ValidationRequest.class);
            client
                    .post()
                    .uri("/v1/validate")
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .bodyValue(validationRequest)
                    .exchange()
                    .expectStatus()
                    .isBadRequest();
        }
    }
}
