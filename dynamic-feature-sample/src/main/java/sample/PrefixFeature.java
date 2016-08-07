package sample;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;

import sample.annotation.Prefix;

@Provider
public class PrefixFeature implements DynamicFeature {

    @Override
    public void configure(ResourceInfo resourceInfo, FeatureContext context) {

        //リソースメソッドのjava.lang.reflect.Methodインスタンスを取得する。
        //configureメソッドはリソースメソッド毎に呼び出される。
        Method method = resourceInfo.getResourceMethod();

        Prefix prefix = method.getAnnotation(Prefix.class);
        if (prefix != null) {
            //アノテーションが付いていたらフィルターを登録する。
            context.register(new PrefixFilter(prefix));
        }
    }

    static class PrefixFilter implements ContainerResponseFilter {

        final Prefix prefix;

        public PrefixFilter(Prefix prefix) {
            this.prefix = prefix;
        }

        @Override
        public void filter(ContainerRequestContext requestContext,
                ContainerResponseContext responseContext) throws IOException {
            Object entity = responseContext.getEntity();
            if (entity instanceof String) {
                String newEntity = prefix.value() + entity;
                responseContext.setEntity(newEntity);
            }
        }
    }
}
