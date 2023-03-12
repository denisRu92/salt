package salt.security.model.validator;

import java.util.regex.Pattern;

public class DateValidator implements FieldValidator {
    private final static Pattern pattern = Pattern.compile("^\\d{2}-\\d{2}-\\d{4}$");

    @Override
    public boolean validate(Object value) {
        return value instanceof String &&
                pattern.matcher((String) value).matches();
    }
}
