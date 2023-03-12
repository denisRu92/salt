package salt.security.model.validator;

import java.util.Collection;

public class ListValidator implements FieldValidator {
    @Override
    public boolean validate(Object value) {
        return value instanceof Collection<?>;
    }
}
