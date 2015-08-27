package sample;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

import javax.ws.rs.core.Context;
import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;

@Provider
public class OptionalProvider implements ParamConverterProvider {

    @Context
    private ParamConverterProvider provider;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public <T> ParamConverter<T> getConverter(Class<T> rawType,
            Type genericType, Annotation[] annotations) {
        if (rawType == Optional.class
                && genericType instanceof ParameterizedType) {
            Type type = ((ParameterizedType) genericType)
                    .getActualTypeArguments()[0];
            if (type instanceof Class) {
                Class<?> clazz = (Class<?>) type;
                ParamConverter<?> converter = provider.getConverter(clazz,
                        clazz, annotations);
                return new OptionalConverter(converter);
            }
        }
        return null;
    }
}
