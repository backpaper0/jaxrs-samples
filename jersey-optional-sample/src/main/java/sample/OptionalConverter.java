package sample;

import java.util.Optional;

import javax.ws.rs.ext.ParamConverter;

public class OptionalConverter<T> implements ParamConverter<Optional<T>> {

    private final ParamConverter<T> converter;

    public OptionalConverter(ParamConverter<T> converter) {
        this.converter = converter;
    }

    @Override
    public Optional<T> fromString(String value) {
        if (value == null || value.isEmpty()
                || value.codePoints().allMatch(Character::isWhitespace)) {
            return Optional.empty();
        }
        return Optional.ofNullable(converter.fromString(value));
    }

    @Override
    public String toString(Optional<T> value) {
        return value.map(Object::toString).orElse(null);
    }
}
