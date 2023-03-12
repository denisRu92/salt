package salt.security.model.validator;

import java.util.HashMap;
import java.util.Map;

public class ValidatorFactory {
    private static Map<String, FieldValidator> validators;

    static {
        validators = new HashMap<>() {{
            put("Int", new IntegerValidator());
            put("String", new StringValidator());
            put("Boolean", new BooleanValidator());
            put("List", new ListValidator());
            put("Date", new DateValidator());
            put("Email", new EmailValidator());
            put("UUID", new UUIDValidator());
            put("Auth-Token", new BearerTokenValidator());
        }};
    }

    public static boolean validate(String type, Object value) {
        if (validators.containsKey(type)) {
            return validators.get(type).validate(value);
        }

        return false;
    }
}
