package com.pc.firebase.models;

import javax.validation.constraints.NotBlank;

public class NoteModel {
    @NotBlank
    public final String UserId;

    @NotBlank
    public final String Text;

    public NoteModel(
        String UserId,
        String Text
    ){
        super();
        this.UserId = UserId;
        this.Text = Text;
    }
}