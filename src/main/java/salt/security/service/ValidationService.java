package salt.security.service;

import lombok.extern.slf4j.Slf4j;
import salt.security.dao.Store;
import salt.security.model.Model;
import salt.security.model.ValidationRequest;
import salt.security.model.ValidationResponse;

@Slf4j
public class ValidationService {
    private final Store<String, Model> modelStore;

    public ValidationService(Store<String, Model> modelStore) {
        this.modelStore = modelStore;
    }

    public ValidationResponse validate(ValidationRequest request) throws RuntimeException {
        ValidationResponse response = new ValidationResponse();
        response.setValid(false);

        if (request != null && request.isValid()) {
            Model model = modelStore.get(request.getId());

            if (model != null) {
                response.addAllAbnormalFields(request.validateBody(model));
                response.addAllAbnormalFields(request.validateHeaders(model));
                response.addAllAbnormalFields(request.validateQueryParams(model));

                if (response.getAbnormalFields().isEmpty()) {
                    response.setValid(true);
                }
            } else {
                throw new RuntimeException(String.format("no model for path %s and method %s", request.getPath(), request.getMethod()));
            }
        } else {
            throw new RuntimeException(("method and path field must not be empty"));
        }

        return response;
    }
}
