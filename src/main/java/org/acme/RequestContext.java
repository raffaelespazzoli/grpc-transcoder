package org.acme;

import io.quarkus.security.UnauthorizedException;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.spi.CDI;
import java.util.Map;

@RequestScoped
public class RequestContext {

    public static final String[] traceKeys=new String[]{
            "x-b3-traceid",
            "x-b3-spanid",
            "x-b3-parentspanid",
            "x-b3-sampled",
            "x-b3-flags",
            "x-datadog-trace-id",
            "x-datadog-parent-id",
            "x-datadog-sampling-priority",
            "x-ot-span-context",
            "grpc-trace-bin",
            "x-cloud-trace-context"};

    public static final String userKey ="http://brightlysoftware.com/claim/user";
    public static final String tenantKey ="http://brightlysoftware.com/claim/tenant";
    Map<String,String> claims;
    Map<String,String> traceContext;
    String jwt;

    public void setClaims(Map<String, String> claims) {
        this.claims = claims;
    }

    public Map<String,String> getClaims(){
        return claims;
    }

    public void setTraceContext(Map<String, String> traceContext) {
        this.traceContext = traceContext;
    }

    public Map<String,String> getTraceContext(){
        return traceContext;
    }

    public String getUser(){
        if (claims.containsKey(userKey)) {
         return claims.get(userKey);
        }
        else throw new UnauthorizedException("user claim not present");
    }

    public String getTenant(){
        if (claims.containsKey(tenantKey)) {
            return claims.get(tenantKey);
        }
        else throw new UnauthorizedException("tenant not present");
    }

    public String getJwt() {
        if (jwt ==null || jwt==""){
            throw new UnauthorizedException("jwt not present");
        }
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public static RequestContext get() {
        return CDI.current().select(RequestContext.class).get();
    }
}
