package sample;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.glassfish.hk2.api.ClassAnalyzer;
import org.glassfish.hk2.api.DynamicConfiguration;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.api.MultiException;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.hk2.utilities.binding.BindingBuilderFactory;
import org.glassfish.hk2.utilities.binding.ServiceBindingBuilder;
import org.glassfish.jersey.internal.inject.JerseyClassAnalyzer;
import org.glassfish.jersey.server.spi.ComponentProvider;

import com.google.inject.Guice;
import com.google.inject.Injector;

public final class GuiceProvider implements ComponentProvider {

    private ServiceLocator locator;

    private Injector injector;

    @Override
    public void initialize(ServiceLocator locator) {
        this.locator = locator;
        //this.injector = locator.getService(Injector.class);
        this.injector = Guice.createInjector();
        ServiceLocatorUtilities.bind(locator, new AbstractBinder() {

            @Override
            protected void configure() {
                bind(GuiceAdditionalClassAnalyzer.class)
                        .analyzeWith(ClassAnalyzer.DEFAULT_IMPLEMENTATION_NAME)
                        .named(GuiceAdditionalClassAnalyzer.class.getSimpleName())
                        .to(ClassAnalyzer.class).in(Singleton.class);
            }
        });
    }

    @Override
    public boolean bind(Class<?> component, Set<Class<?>> providerContracts) {

        //ComponentFilter filter = locator.getService(ComponentFilter.class);
        //if (filter.isTarget(component) == false) {
        //    return false;
        //}

        ServiceBindingBuilder<Object> builder = BindingBuilderFactory
                .newFactoryBinder(new GuiceFactory(component));

        builder.to(component);
        providerContracts.forEach(builder::to);

        DynamicConfiguration config = ServiceLocatorUtilities.createDynamicConfiguration(locator);
        BindingBuilderFactory.addBinding(builder, config);
        config.commit();

        return true;
    }

    @Override
    public void done() {
    }

    private class GuiceFactory implements Factory<Object> {

        private final Class<?> component;

        public GuiceFactory(Class<?> component) {
            this.component = Objects.requireNonNull(component);
        }

        @Override
        public Object provide() {
            Object injectMe = injector.getInstance(component);
            String strategy = GuiceAdditionalClassAnalyzer.class.getSimpleName();
            locator.inject(injectMe, strategy);
            return injectMe;
        }

        @Override
        public void dispose(Object instance) {
        }
    }

    private static class GuiceAdditionalClassAnalyzer implements ClassAnalyzer {

        private final ClassAnalyzer delegate;

        @Inject
        public GuiceAdditionalClassAnalyzer(
                @Named(JerseyClassAnalyzer.NAME) ClassAnalyzer delegate) {
            this.delegate = delegate;
        }

        @Override
        public <T> Set<Method> getInitializerMethods(Class<T> clazz) throws MultiException {
            return get(delegate.getInitializerMethods(clazz));
        }

        @Override
        public <T> Set<Field> getFields(Class<T> clazz) throws MultiException {
            return get(delegate.getFields(clazz));
        }

        private static <T extends AccessibleObject> Set<T> get(Collection<T> cs) {
            return cs.stream().filter(x -> x.isAnnotationPresent(Inject.class) == false)
                    .collect(Collectors.toSet());
        }

        @Override
        public <T> Constructor<T> getConstructor(Class<T> clazz)
                throws MultiException, NoSuchMethodException {
            throw new UnsupportedOperationException();
        }

        @Override
        public <T> Method getPostConstructMethod(Class<T> clazz) throws MultiException {
            throw new UnsupportedOperationException();
        }

        @Override
        public <T> Method getPreDestroyMethod(Class<T> clazz) throws MultiException {
            throw new UnsupportedOperationException();
        }
    }
}
