package salt.security.model.validator;

public class IntegerValidator implements FieldValidator {
    @Override
    public boolean validate(Object value) {
        return value instanceof Integer;
    }
}
