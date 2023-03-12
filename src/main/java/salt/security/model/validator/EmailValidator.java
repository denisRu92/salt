package salt.security.model.validator;

import java.util.regex.Pattern;

public class EmailValidator implements FieldValidator {
    private final static Pattern pattern = Pattern.compile("^(.+)@(\\S+)$");

    @Override
    public boolean validate(Object value) {
        return value instanceof String &&
                pattern.matcher((String) value).matches();
    }
}
