package salt.security.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import salt.security.model.validator.ValidationResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ValidationResponse {
    private boolean valid;

    @JsonProperty("abnormal_fields")
    private List<ValidationResult> abnormalFields;

    public ValidationResponse() {
        this.abnormalFields = new ArrayList<>();
    }

    public void addAllAbnormalFields(Collection<ValidationResult> validationResult) {
        this.abnormalFields.addAll(validationResult);
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public List<ValidationResult> getAbnormalFields() {
        return abnormalFields;
    }

    public void setAbnormalFields(List<ValidationResult> abnormalFields) {
        this.abnormalFields = abnormalFields;
    }
}
