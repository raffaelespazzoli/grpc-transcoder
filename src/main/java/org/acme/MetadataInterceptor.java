package org.acme;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.grpc.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;

@ApplicationScoped
public class MetadataInterceptor implements ServerInterceptor {



    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall, @NotNull Metadata metadata, ServerCallHandler<ReqT, RespT> serverCallHandler) {
        System.out.println(metadata.toString());
        try {
            Map<String,String> claims=new HashMap<>();
            String jsonClaims=new String(Base64.getDecoder().decode(metadata.get(Metadata.Key.of("x-jwt",Metadata.ASCII_STRING_MARSHALLER))));
            new ObjectMapper().readValue(jsonClaims, HashMap.class).forEach((o, o2) -> claims.put(o.toString(),o2.toString()));
            RequestClaims.get().setClaims(claims);
        } catch (Exception e) {
            e.printStackTrace();
            Status status = Status.FAILED_PRECONDITION.withDescription("x-jwt mandatory header not found or not parseable");
            serverCall.close(status, metadata);
            return new ServerCall.Listener<>() {
                // noop
            };
        }
        return Contexts.interceptCall(Context.current(), serverCall, metadata, serverCallHandler);
    }
}



