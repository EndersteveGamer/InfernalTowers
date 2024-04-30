package api.entities.builder;

import api.Position;

import java.util.Optional;

public interface EntityBuilder<T, V> {
    Optional<V> build(T data, Position position);
}
