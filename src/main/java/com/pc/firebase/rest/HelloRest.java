package com.pc.firebase.rest;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Path("/api/hello/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class HelloRest {
    @Inject
    @ConfigProperty(name = "mp.jwt.verify.issuer", defaultValue = "false")
    String issuer;

    @GET
    @Path("")
    public String Hello(){
        return "Hello";
    }
    @GET
    @Path("issuer")
    public String Issuer(){
        return issuer;
    }


}