package sample;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;

@Dependent
public class ResourceProvider {

    @Resource
    @Produces
    @Dependent
    private ManagedExecutorService executor;
}
