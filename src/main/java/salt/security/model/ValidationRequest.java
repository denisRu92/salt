package salt.security.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import salt.security.model.validator.ValidationResult;
import salt.security.model.validator.ValidatorFactory;

import java.util.*;

public class ValidationRequest {
    private String path;
    private String method;

    @JsonProperty("query_params")
    private Map<String, Data> queryParams;

    private Map<String, Data> headers;
    private Map<String, Data> body;

    private ValidationRequest() {
        this.queryParams = new HashMap<>();
        this.headers = new HashMap<>();
        this.body = new HashMap<>();
    }

    public boolean isValid() {
        return path != null && !path.isEmpty() &&
                method != null && !method.isEmpty();
    }

    public String getId() {
        return String.format("%s:%s", method, path);
    }

    public void setQueryParams(List<Data> queryParams) {
        if (queryParams != null) {
            queryParams.forEach(queryParam -> this.queryParams.put(queryParam.getName(), queryParam));
        }
    }

    public void setHeaders(List<Data> headers) {
        if (headers != null) {
            headers.forEach(header -> this.headers.put(header.getName(), header));
        }
    }

    public void setBody(List<Data> body) {
        if (body != null) {
            body.forEach(value -> this.body.put(value.getName(), value));
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Collection<Data> getQueryParams() {
        return queryParams.values();
    }

    public Collection<Data> getHeaders() {
        return headers.values();
    }

    public Collection<Data> getBody() {
        return body.values();
    }

    public List<ValidationResult> validateQueryParams(Model model) {
        List<ValidationResult> results = new ArrayList<>();

        this.queryParams.values().forEach(param -> {
            Metadata paramMetadata = model.getQueryParam(param.getName());
            ValidationResult result = validate(paramMetadata, param, "query params");

            if (result != null) {
                results.add(result);
            }
        });

        // Check required query params
        model.getQueryParams()
                .stream()
                .filter(Metadata::isRequired)
                .map(Metadata::getName)
                .forEach(requiredParam -> {
                    if (!queryParams.containsKey(requiredParam)) {
                        results.add(ValidationResult.builder()
                                .name(requiredParam)
                                .where("query params")
                                .valid(false)
                                .reason("field is required but doesn't exists")
                                .build());
                    }
                });

        return results;
    }

    public List<ValidationResult> validateHeaders(Model model) {
        List<ValidationResult> results = new ArrayList<>();

        this.headers.values().forEach(header -> {
            Metadata headerMetadata = model.getHeader(header.getName());
            ValidationResult result = validate(headerMetadata, header, "headers");

            if (result != null) {
                results.add(result);
            }
        });

        // Check required headers
        model.getHeaders()
                .stream()
                .filter(Metadata::isRequired)
                .map(Metadata::getName)
                .forEach(requiredHeader -> {
                    if (!headers.containsKey(requiredHeader)) {
                        results.add(ValidationResult.builder()
                                .name(requiredHeader)
                                .where("headers")
                                .valid(false)
                                .reason("field is required but doesn't exists")
                                .build());
                    }
                });

        return results;
    }

    public List<ValidationResult> validateBody(Model model) {
        List<ValidationResult> results = new ArrayList<>();

        this.body.values().forEach(field -> {
            Metadata fieldMetadata = model.getBodyField(field.getName());
            ValidationResult result = validate(fieldMetadata, field, "body");

            if (result != null) {
                results.add(result);
            }
        });

        // Check required body fields
        model.getBody()
                .stream()
                .filter(Metadata::isRequired)
                .map(Metadata::getName)
                .forEach(requiredField -> {
                    if (!body.containsKey(requiredField)) {
                        results.add(ValidationResult.builder()
                                .name(requiredField)
                                .where("body")
                                .valid(false)
                                .reason("field is required but doesn't exists")
                                .build());
                    }
                });

        return results;
    }

    /**
     * The idea is to validate that a field matches the types that the model specifies.
     * For example: If a field should be only of type 'String' but we get type 'Auth-Token', which is basically a string,
     * it would be considered valid.
     * If this should result in abnormal, we just need to always run
     * all {@link salt.security.model.validator.FieldValidator validators} and see if there's a match for a type that
     * should have not matched.
     */
    private ValidationResult validate(Metadata metadata, Data data, String where) {
        // Check if the request has a field that's not in the model.
        // Maybe in the real world it's not required because the model can miss?
        if (metadata == null) {
            return ValidationResult.builder()
                    .name(data.getName())
                    .where(where)
                    .valid(false)
                    .reason("field is unknown")
                    .build();
        } else if (!metadata.getTypes().stream()
                .allMatch(type -> ValidatorFactory.validate(type, data.getValue()))) {
            return ValidationResult.builder()
                    .name(data.getName())
                    .where(where)
                    .valid(false)
                    .reason("field doesn't match type")
                    .build();
        }

        return null;
    }
}
