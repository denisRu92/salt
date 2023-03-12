package salt.security.service;

import lombok.extern.slf4j.Slf4j;
import salt.security.dao.Store;
import salt.security.model.Model;

@Slf4j
public class ModelService {
    private final Store<String, Model> store;

    public ModelService(Store<String, Model> store) {
        this.store = store;
    }

    public boolean store(Model model) {
        if (model != null && model.isValid()) {
            this.store.put(model.getId(), model);
            log.info("Stored model for {}", model.getId());
            return true;
        }

        return false;
    }
}
