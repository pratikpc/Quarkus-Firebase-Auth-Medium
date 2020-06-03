package com.pc.firebase;

import java.util.Set;
import javax.annotation.security.PermitAll;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import io.smallrye.jwt.auth.principal.DefaultJWTCallerPrincipal;

@Path("/firebase/")
public class FirebaseAuth {

    @GET
    @Path("")
    @Produces(MediaType.TEXT_PLAIN)
    @PermitAll
    public String hello(@Context SecurityContext securityContext) {
        final var user = (DefaultJWTCallerPrincipal) securityContext.getUserPrincipal();
        if (user == null)
            return "Hello Anonymous User";
        final var email = user.getClaim("email");
        return "hello " + email;
    }

    @GET
    @Path("claim_names")
    @Produces(MediaType.TEXT_PLAIN)
    @PermitAll
    public Set<String> claimNames(@Context SecurityContext securityContext) {
        final var user = (DefaultJWTCallerPrincipal) securityContext.getUserPrincipal();
        if (user == null)
            return Set.of();
        return user.getClaimNames();
    }
}