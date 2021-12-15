package org.myprojects.noteapp.service;

import org.junit.jupiter.api.Test;
import org.myprojects.noteapp.model.Note;
import org.myprojects.noteapp.model.Notebook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class NoteServiceTest {

    @Autowired
    private NotebookService notebookService;

    @Autowired
    private NoteService noteService;

    @Test
    public void testCreateNoteWithNoTags() {
        Notebook notebook = buildNotebook("testNotebook");
        notebookService.createNotebook(notebook);
        notebook = notebookService.getAllNotebook().get(0);
        Note note = buildNote("testNote", Collections.emptyList());
        noteService.createNote(notebook.getId(), note);
        notebook = notebookService.getNotebook(notebook.getId());
        assertThat(notebook.getNotes().size()).isEqualTo(1);
        assertThat(notebook.getNotes().get(0).getTitle()).isEqualTo("testNote");
        assertThat(notebook.getNotes().get(0).getTags().size()).isEqualTo(0);

        noteService.deleteNote(notebook.getId(), notebook.getNotes().get(0).getId());
        notebookService.deleteNotebook(notebook.getId());

    }

    @Test
    public void testCreateNoteWithTags() {
        Notebook notebook = buildNotebook("testNotebook");
        notebookService.createNotebook(notebook);
        notebook = notebookService.getAllNotebook().get(0);
        Note note = buildNote("testNote", Collections.singletonList("testTag"));
        noteService.createNote(notebook.getId(), note);
        notebook = notebookService.getNotebook(notebook.getId());
        assertThat(notebook.getNotes().size()).isEqualTo(1);
        assertThat(notebook.getNotes().get(0).getTitle()).isEqualTo("testNote");
        assertThat(notebook.getNotes().get(0).getTags().size()).isEqualTo(1);
        assertThat(notebook.getNotes().get(0).getTags().get(0)).isEqualTo("testTag");

        noteService.deleteNote(notebook.getId(), notebook.getNotes().get(0).getId());
        notebookService.deleteNotebook(notebook.getId());

    }

    @Test
    public void testGetNote() {
        Notebook notebook = buildNotebook("testNotebook");
        notebookService.createNotebook(notebook);
        notebook = notebookService.getAllNotebook().get(0);
        Note note = buildNote("testNote", Collections.singletonList("testTag"));
        noteService.createNote(notebook.getId(), note);
        notebook = notebookService.getAllNotebook().get(0);
        Note savedNote = noteService.getNote(notebook.getId(), notebook.getNotes().get(0).getId());
        assertThat(savedNote.getTitle()).isEqualTo("testNote");
        assertThat(savedNote.getTags().size()).isEqualTo(1);
        assertThat(savedNote.getTags().get(0)).isEqualTo("testTag");

        noteService.deleteNote(notebook.getId(), notebook.getNotes().get(0).getId());
        notebookService.deleteNotebook(notebook.getId());
    }

    @Test
    public  void testUpdateNote() {
        Notebook notebook = buildNotebook("testNotebook");
        notebookService.createNotebook(notebook);
        notebook = notebookService.getAllNotebook().get(0);
        Note note = buildNote("testNote", Collections.singletonList("testTag"));
        noteService.createNote(notebook.getId(), note);
        notebook = notebookService.getAllNotebook().get(0);
        Note savedNote = noteService.getNote(notebook.getId(), notebook.getNotes().get(0).getId());
        assertThat(savedNote.getTitle()).isEqualTo("testNote");
        assertThat(savedNote.getTags().size()).isEqualTo(1);
        assertThat(savedNote.getTags().get(0)).isEqualTo("testTag");

        note.setTitle("newTitle");
        note.setTags(Collections.emptyList());
        noteService.updateNote(notebook.getId(), note);
        assertThat(savedNote.getTitle()).isEqualTo("newTitle");
        assertThat(savedNote.getTags().size()).isEqualTo(0);

        noteService.deleteNote(notebook.getId(), notebook.getNotes().get(0).getId());
        notebookService.deleteNotebook(notebook.getId());
    }

    @Test
    public void testDeleteNote() {
        Notebook notebook = buildNotebook("testNotebook");
        notebookService.createNotebook(notebook);
        notebook = notebookService.getAllNotebook().get(0);
        Note note = buildNote("testNote", Collections.singletonList("testTag"));
        noteService.createNote(notebook.getId(), note);
        notebook = notebookService.getAllNotebook().get(0);

        noteService.deleteNote(notebook.getId(), notebook.getNotes().get(0).getId());
        assertThat(notebook.getNotes().size()).isEqualTo(0);

        notebookService.deleteNotebook(notebook.getId());
    }

    private Notebook buildNotebook(final String title) {
        Notebook notebook = Notebook.builder()
                .title(title)
                .notes(Collections.emptyList())
                .build();

        return notebook;
    }

    private Note buildNote(final String title, final List<String> tags) {
        Note note = Note.builder()
                .title(title)
                .tags(tags)
                .build();

        return note;
    }

}
