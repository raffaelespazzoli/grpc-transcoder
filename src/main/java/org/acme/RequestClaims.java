package org.acme;


import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.spi.CDI;
import java.util.Map;

@RequestScoped
public class RequestClaims {
    Map<String,String> claims;

    public RequestClaims(){

    }
    public void setClaims(Map<String, String> claims) {
        this.claims = claims;
    }

    public Map<String,String> getClaims(){
        return claims;
    }

    public static RequestClaims get() {
        return CDI.current().select(RequestClaims.class).get();
    }
}
