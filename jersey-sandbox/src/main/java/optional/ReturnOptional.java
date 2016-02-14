package optional;

import java.io.IOException;
import java.util.Optional;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Response.Status;

/**
 * リソースメソッドの戻り値にOptionalを使うための仕掛けをするフィルターです。
 *
 */
public class ReturnOptional implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext,
            ContainerResponseContext responseContext) throws IOException {

        //戻り値がOpionalでない場合は対象外
        if (responseContext.getEntityClass() != Optional.class) {
            return;
        }

        //Optionalを剥がしてエンティティをセットしなおす
        //Optional.emptyの場合はnullをセットしてステータスコード204 No Contentをセットする
        Optional<?> opt = (Optional<?>) responseContext.getEntity();
        responseContext.setEntity(opt.orElse(null));
        responseContext.setStatusInfo(
                opt.map(a -> responseContext.getStatusInfo()).orElse(Status.NO_CONTENT));
    }
}
