package org.acme;

import io.quarkus.grpc.GrpcService;

import io.quarkus.grpc.RegisterInterceptor;
import io.smallrye.mutiny.Uni;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

@GrpcService
@RegisterInterceptor(MetadataInterceptor.class)
public class HelloGrpcService implements HelloGrpc {

    @Inject
    RequestClaims requestClaims;
    @Override
    public Uni<HelloReply> sayHello(@NotNull HelloRequest request) {
        System.out.println(requestClaims.getClaims());
        return Uni.createFrom().item("Hello " + request.getName() + "!")
                .map(msg -> HelloReply.newBuilder().setMessage(msg).build());
    }

}
