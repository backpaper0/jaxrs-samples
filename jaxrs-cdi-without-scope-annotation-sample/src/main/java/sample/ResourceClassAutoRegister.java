package sample;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterTypeDiscovery;
import javax.enterprise.inject.spi.AnnotatedConstructor;
import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.util.AnnotationLiteral;

public class ResourceClassAutoRegister implements Extension {

    public void register(@Observes AfterTypeDiscovery context, BeanManager bm) {
        // HelloApiクラスをハードコーディングしてるから汎用的ではない！！！
        // リソースクラスをスキャンする方法を考えないといけない〜
        AnnotatedType<?> type = bm.createAnnotatedType(HelloApi.class);
        String id = HelloApi.class.getName();
        context.addAnnotatedType(new AnnotatedTypeImpl<>(type), id);
    }

    /*
     * すべてのリソースクラスを登録するようなまどろっこしい真似をしなくても
     * BeforeBeanDiscovery.addStereotypeを使えば良いと思ったんだけど、
     * バグってるっぽい、、、
     * https://issues.jboss.org/browse/WELD-1624
     */
    //    public void addPathAsStereotype(@Observes BeforeBeanDiscovery context, BeanManager bm) {
    //        context.addStereotype(Path.class, new StereotypeImpl(), new RequestScopedImpl());
    //    }

    private static class AnnotatedTypeImpl<X> implements AnnotatedType<X> {

        private final AnnotatedType<X> type;
        private final RequestScoped requestScoped = new RequestScopedImpl();

        public AnnotatedTypeImpl(AnnotatedType<X> type) {
            this.type = type;
        }

        @Override
        public Type getBaseType() {
            return type.getBaseType();
        }

        @Override
        public Set<Type> getTypeClosure() {
            return type.getTypeClosure();
        }

        @Override
        public <T extends Annotation> T getAnnotation(Class<T> annotationType) {
            if (annotationType == RequestScoped.class) {
                return (T) requestScoped;
            }
            return type.getAnnotation(annotationType);
        }

        @Override
        public Set<Annotation> getAnnotations() {
            return Stream.concat(Stream.of(requestScoped), type.getAnnotations().stream())
                    .collect(Collectors.toSet());
        }

        @Override
        public boolean isAnnotationPresent(Class<? extends Annotation> annotationType) {
            if (annotationType == RequestScoped.class) {
                return true;
            }
            return type.isAnnotationPresent(annotationType);
        }

        @Override
        public Class<X> getJavaClass() {
            return type.getJavaClass();
        }

        @Override
        public Set<AnnotatedConstructor<X>> getConstructors() {
            return type.getConstructors();
        }

        @Override
        public Set<AnnotatedMethod<? super X>> getMethods() {
            return type.getMethods();
        }

        @Override
        public Set<AnnotatedField<? super X>> getFields() {
            return type.getFields();
        }
    }

    private static class RequestScopedImpl extends AnnotationLiteral<RequestScoped>
            implements RequestScoped {
    }
}