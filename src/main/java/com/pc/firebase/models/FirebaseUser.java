package com.pc.firebase.models;

import javax.ws.rs.core.SecurityContext;
import io.smallrye.jwt.auth.principal.DefaultJWTCallerPrincipal;

public class FirebaseUser {

  public final DefaultJWTCallerPrincipal Principal;

  public FirebaseUser(final SecurityContext securityContext) {
    this.Principal = (DefaultJWTCallerPrincipal) securityContext.getUserPrincipal();
  }

  public boolean IsNull(){
    return this.Principal == null;
  }

  public String getClaim(final String key) {
    return Principal.getClaim(key);
  }

  public String Email() {
    return this.getClaim("email");
  }

  public String Name() {
    return this.getClaim("name");
  }

  public String Picture() {
    return this.getClaim("picture");
  }

  public String Id() {
    return this.getClaim("user_id");
  }
}
