package salt.security.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import salt.security.dao.LocalStore;
import salt.security.model.Model;
import salt.security.service.ModelService;
import salt.security.service.ValidationService;

@Configuration
public class ServiceConfiguration {

    @Bean("ModelStore")
    public LocalStore<String, Model> getModelStore() {
        return new LocalStore<>();
    }

    @Bean
    public ModelService getModelService(@Qualifier("ModelStore") LocalStore<String, Model> modelLocalStore) {
        return new ModelService(modelLocalStore);
    }

    @Bean
    public ValidationService getValidationService(@Qualifier("ModelStore") LocalStore<String, Model> modelLocalStore) {
        return new ValidationService(modelLocalStore);
    }
}
