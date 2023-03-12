package salt.security.model.validator;

public class BooleanValidator implements FieldValidator {
    @Override
    public boolean validate(Object value) {
        return value instanceof Boolean;
    }
}
