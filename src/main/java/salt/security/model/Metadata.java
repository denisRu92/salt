package salt.security.model;

import java.util.HashSet;
import java.util.Set;

public class Metadata {
    private String name;
    private Set<String> types;
    private boolean required;

    public Metadata() {
        this.types = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getTypes() {
        return types;
    }

    public void setTypes(Set<String> types) {
        this.types = types;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }
}
