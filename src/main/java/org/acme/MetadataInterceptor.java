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
//        System.out.println("metadata: "+metadata.toString());
//        System.out.println("method description: "+serverCall.getMethodDescriptor().toString());
//        System.out.println("attributes: "+serverCall.getAttributes().toString());
//        System.out.println("requestMarshaller=: "+(MethodDescriptor.Marshaller)serverCall.getAttributes().get(Attributes()));
//        System.out.println("authority: "+serverCall.getAuthority());
//        System.out.println("security level: "+serverCall.getSecurityLevel());
//        System.out.println("serverCallHandler: "+serverCallHandler.toString());
                ///MutinyHelloGrpcGrpc.HelloGrpcImplBase
        RequestContext requestContext=RequestContext.get();
        Map<String,String> claims=new HashMap<>();
        String claimsString=metadata.get(Metadata.Key.of("x-jwt",Metadata.ASCII_STRING_MARSHALLER));
        if (claimsString!=null && claimsString!=""){
            try {
                String jsonClaims=new String(Base64.getDecoder().decode(claimsString));
                new ObjectMapper().readValue(jsonClaims, HashMap.class).forEach((o, o2) -> claims.put(o.toString(),o2.toString()));
                requestContext.setClaims(claims);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String authHeader=metadata.get(Metadata.Key.of("authorization",Metadata.ASCII_STRING_MARSHALLER));
        if (authHeader!=null && authHeader!="" && authHeader.startsWith("Bearer ")) {
            requestContext.setJwt(authHeader.substring(7));
        }

        Map<String,String> traces=new HashMap<>();
        for (String traceKey:RequestContext.traceKeys){
            String traceValue=metadata.get(Metadata.Key.of(traceKey,Metadata.ASCII_STRING_MARSHALLER));
            if (traceValue!=null && traceValue !=""){
              traces.put(traceKey,traceValue);
            }
        }
        requestContext.setTraceContext(traces);
        return Contexts.interceptCall(Context.current(), serverCall, metadata, serverCallHandler);
    }
}



