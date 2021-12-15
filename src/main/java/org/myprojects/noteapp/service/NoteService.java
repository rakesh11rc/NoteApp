package org.myprojects.noteapp.service;

import org.myprojects.noteapp.model.Note;
import org.myprojects.noteapp.model.Notebook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Service
public class NoteService {

    @Autowired
    private NotebookService notebookService;

    public Note getNote(final String notebookId, final String noteId) {

        Notebook notebook = notebookService.getNotebook(notebookId);

        if(Objects.nonNull(notebook)) {
            return notebook.getNotes().stream()
                    .filter(note -> note.getId().equals(noteId))
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    public void createNote(final String notebookId, final Note note) {
        Notebook notebook = notebookService.getNotebook(notebookId);
        if(Objects.nonNull(notebook)) {
            String uuid = UUID.randomUUID().toString();
            note.setId(uuid);
            note.setCreatedDate(new Date());
            note.setLastModifiedDate(new Date());

            notebook.getNotes().add(note);
        }
    }

    public void deleteNote(final String notebookId, final String noteId) {
        Notebook notebook = notebookService.getNotebook(notebookId);
        if(Objects.nonNull(notebook)) {
            Note note = getNote(notebookId, noteId);
            if(Objects.nonNull(note)) {
                notebook.getNotes().remove(note);
            }
        }

    }

    public void updateNote(final String notebookId, final Note note) {
        Notebook notebook = notebookService.getNotebook(notebookId);
        if(Objects.nonNull(notebook) && Objects.nonNull(getNote(notebookId, note.getId()))) {
            deleteNote(notebookId, note.getId());
            note.setLastModifiedDate(new Date());
            notebook.getNotes().add(note);
        }

    }
}
