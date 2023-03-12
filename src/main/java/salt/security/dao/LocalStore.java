package salt.security.dao;

import java.util.HashMap;
import java.util.Map;

/**
 * For this assignment we would use local map, but in true production environment
 * we should use persistent database that would store these model.
 * We can and need to use local caching for models so that we won't access the database on every
 * request validation.
 */
public class LocalStore<Key, Value> implements Store<Key, Value> {
    private final Map<Key, Value> store;

    public LocalStore() {
        this.store = new HashMap<>();
    }

    @Override
    public void put(Key key, Value value) {
        this.store.put(key, value);
    }

    @Override
    public Value get(Key key) {
        return this.store.get(key);
    }

    @Override
    public Value delete(Key key) {
        return this.store.remove(key);
    }
}
