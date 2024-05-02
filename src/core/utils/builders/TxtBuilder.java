package core.utils.builders;

import api.Position;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public abstract class TxtBuilder<T> {
    protected final Map<Predicate<Character>, BiFunction<Character, Position, T>> builderMap = new HashMap<>();

    protected void registerFromString(String chars, BiFunction<Character, Position, T> builderFunc) {
        builderMap.put(
            chr -> chars.contains(String.valueOf(chr)), builderFunc
        );
    }

    public Optional<T> build(Character data, Position position) {
        for (Predicate<Character> predicate : builderMap.keySet()) {
            if (predicate.test(data)) return Optional.of(builderMap.get(predicate).apply(data, position));
        }
        return Optional.empty();
    }
}