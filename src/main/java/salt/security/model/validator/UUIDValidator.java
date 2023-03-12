package salt.security.model.validator;

import java.util.regex.Pattern;

public class UUIDValidator implements FieldValidator {
    private final static Pattern pattern = Pattern.compile("([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})");

    @Override
    public boolean validate(Object value) {
        return value instanceof String &&
                pattern.matcher((String) value).matches();
    }
}
