package com.pc.firebase.repositories;

import com.pc.firebase.models.NoteModel;
import java.util.stream.StreamSupport;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.Tuple;

@ApplicationScoped
public class NoteRepositories {
    @Inject
    PgPool client;
    // @Inject
    // UserRepository users;

    @Inject
    @ConfigProperty(name = "com.pc.firebase.rest.create", defaultValue = "false")
    boolean schemaCreate;

    private final String Table = "NOTES";

    @PostConstruct
    void Initialize() {
        if (schemaCreate) {
            SetupDatabase().await().indefinitely();
        }
    }

    public Uni<Boolean> SetupDatabase() {
        return CreateTable();
    }

    public Uni<Boolean> DropTable() {
        return client.query("DROP TABLE IF EXISTS " + Table + " CASCADE").onItem()
                .apply(x -> x.size() == 0);
    }

    private Uni<Boolean> CreateTable() {
        return client
                .query("CREATE TABLE IF NOT EXISTS " + Table + " (USER_KEY VARCHAR PRIMARY KEY,"
                        + "NOTE VARCHAR(20) NOT NULL DEFAULT(FALSE))")
                .onItem().apply(x -> x.size() == 0);
    }

    // Returns IDs of Appointments's added
    // Takes in only values important to perform insertion
    public Uni<String> Add(final NoteModel note) {
        return client
                .preparedQuery("INSERT INTO " + Table + " (USER_KEY, NOTE) " + " VALUES ($1,$2)"
                        + "  ON CONFLICT (USER_KEY) DO UPDATE SET NOTE=$2 RETURNING (NOTE);",
                        Tuple.of(note.UserId, note.Text))
                .onItem().apply(rowSet -> rowSet.iterator().next().getString(("note")));
    }

    private NoteModel RowToModel(final Row row) {
        return new NoteModel(row.getString("user_key"), row.getString("txt_usr"));
    }

    public Uni<NoteModel> Get(final String user) {
        return client
                .preparedQuery("SELECT coalesce(NOTE, '') as TXT_USR, 1 as query_id FROM " + Table
                        + " WHERE USER_KEY = $1 union all "
                        + "select null, 1 where not exists (select 1 from " + Table
                        + " where USER_KEY = $1 LIMIT 1);", Tuple.of(user))
                .onItem().apply(set -> set.iterator().next()).onItem()
                .apply(row -> RowToModel(row));
    }

    public Multi<NoteModel> Get() {
        return client.query("SELECT coalesce(NOTE, '') as TXT_USR FROM " + Table).onItem()
                .produceMulti(set -> Multi.createFrom()
                        .items(() -> StreamSupport.stream(set.spliterator(), false)))
                .onItem().apply(row -> RowToModel(row));
    }
}
