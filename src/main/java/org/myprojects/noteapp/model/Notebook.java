package org.myprojects.noteapp.model;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Data
@Builder
@Jacksonized
public class Notebook {

    private String id;

    @NonNull
    private String title;

    private List<Note> notes;

}
