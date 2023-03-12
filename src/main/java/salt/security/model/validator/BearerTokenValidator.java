package salt.security.model.validator;

import java.util.regex.Pattern;

public class BearerTokenValidator implements FieldValidator {
    private final static Pattern pattern = Pattern.compile("Bearer .+");

    @Override
    public boolean validate(Object value) {
        return value instanceof String &&
                pattern.matcher((String) value).matches();
    }
}
