package salt.security.dao;

public interface Store<Key, Value> {
    void put(Key key, Value value);
    Value get(Key key);
    Value delete(Key key);
}
