package org.myprojects.noteapp.model;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.Date;
import java.util.List;

@Data
@Builder
@Jacksonized
public class Note {

    private String id;

    private String title;

    private Date createdDate;

    private Date lastModifiedDate;

    private List<String> tags;

}
