package salt.security.model.validator;

public class StringValidator implements FieldValidator {
    @Override
    public boolean validate(Object value) {
        return value instanceof String;
    }
}
