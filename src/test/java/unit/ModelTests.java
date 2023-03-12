package unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
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

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@SpringBootTest(classes = {Application.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@TestPropertySource(locations = "/application.yaml")
public class ModelTests {
    private final ObjectMapper objectMapper = JsonMapper.builder().build();

    @Autowired
    private WebTestClient client;

    @Test
    public void storeModel() throws IOException {
        String inputFileName = Objects.requireNonNull(getClass().getClassLoader().getResource("testdata/model.json")).getFile();

        try (Reader fileReader = new FileReader(inputFileName, StandardCharsets.UTF_8)) {
            Model model = objectMapper.readValue(fileReader, Model.class);
            client
                    .post()
                    .uri("/v1/model")
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .bodyValue(model)
                    .exchange()
                    .expectStatus().isOk();
        }
    }

    @Test
    public void invalidModel() throws IOException {
        String inputFileName = Objects.requireNonNull(getClass().getClassLoader().getResource("testdata/model_invalid.json")).getFile();

        try (Reader fileReader = new FileReader(inputFileName, StandardCharsets.UTF_8)) {
            Model model = objectMapper.readValue(fileReader, Model.class);
            client
                    .post()
                    .uri("/v1/model")
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .bodyValue(model)
                    .exchange()
                    .expectStatus().isBadRequest();
        }
    }

    @Test
    public void missingBodyField() throws IOException {
        String inputFileName = Objects.requireNonNull(getClass().getClassLoader().getResource("testdata/model_missing_body.json")).getFile();

        try (Reader fileReader = new FileReader(inputFileName, StandardCharsets.UTF_8)) {
            Model model = objectMapper.readValue(fileReader, Model.class);
            client
                    .post()
                    .uri("/v1/model")
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .bodyValue(model)
                    .exchange()
                    .expectStatus().isOk();
        }
    }
}
