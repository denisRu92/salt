package salt.security.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class Model {
    private String path;
    private String method;

    @JsonProperty("query_params")
    private final HashMap<String, Metadata> queryParams;

    private final HashMap<String, Metadata> headers;
    private final HashMap<String, Metadata> body;

    private Model() {
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

    public void setQueryParams(List<Metadata> queryParams) {
        if (queryParams != null) {
            queryParams.forEach(queryParam -> this.queryParams.put(queryParam.getName(), queryParam));
        }
    }

    public void setHeaders(List<Metadata> headers) {
        if (headers != null) {
            headers.forEach(header -> this.headers.put(header.getName(), header));
        }
    }

    public void setBody(List<Metadata> body) {
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

    public Collection<Metadata> getQueryParams() {
        return queryParams.values();
    }

    public Collection<Metadata> getHeaders() {
        return headers.values();
    }

    public Collection<Metadata> getBody() {
        return body.values();
    }

    public Metadata getQueryParam(String key) {
        return this.queryParams.get(key);
    }

    public Metadata getBodyField(String key) {
        return this.body.get(key);
    }

    public Metadata getHeader(String key) {
        return this.headers.get(key);
    }
}
