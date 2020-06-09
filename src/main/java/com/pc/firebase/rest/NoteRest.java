package com.pc.firebase.rest;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import com.pc.firebase.models.FirebaseUser;
import com.pc.firebase.models.NoteModel;
import com.pc.firebase.repositories.NoteRepositories;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

@Path("/api/note/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NoteRest {

    @Inject
    NoteRepositories notes;

    @Inject
    @ConfigProperty(name = "com.pc.firebase.rest.debug", defaultValue = "false")
    boolean devMode;

    @GET
    @Path("")
    @PermitAll()
    public Uni<String> Get(@Context final SecurityContext securityContext) {
        final var user = new FirebaseUser(securityContext);
        if (user.IsNull())
            return Uni.createFrom().nullItem();
        final var id = user.Id();
        return notes.Get(id).onItem().apply(note -> note.Text);
    }

    @POST
    @Path("")
    @PermitAll()
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Uni<String> Add(final @FormParam("note") String Note,
            @Context final SecurityContext securityContext) {
        final var user = new FirebaseUser(securityContext);
        final var id = user.Id();
        final var note = new NoteModel(id, Note);
        return notes.Add(note);
    }

    @GET
    @Path("all")
    @Produces(MediaType.TEXT_PLAIN)
    @PermitAll
    public Multi<NoteModel> Get() {
        if (!devMode)
            return Multi.createFrom().empty();
        return notes.Get();
    }

}
