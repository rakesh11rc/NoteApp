package org.myprojects.noteapp.service;

import org.junit.jupiter.api.Test;
import org.myprojects.noteapp.model.Note;
import org.myprojects.noteapp.model.Notebook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class NotebookServiceTest {

    @Autowired
    private NotebookService notebookService;

    @Autowired NoteService noteService;

    @Test
    public void testGetEmptyNotebooks() {
        List<Notebook> notebooks = notebookService.getAllNotebook();
        assertThat(notebooks.isEmpty());
    }

    @Test
    public void testCreateNotebook() {
        Notebook notebook = buildNotebook("testNotebook");
        notebookService.createNotebook(notebook);
        List<Notebook> notebooks = notebookService.getAllNotebook();
        assertThat(notebooks.size()).isEqualTo(1);
        assertThat(notebooks.get(0).getTitle()).isEqualTo(notebook.getTitle());

        notebookService.deleteNotebook(notebooks.get(0).getId());
    }

    @Test
    public void testCreateMultipleNotebooks() {
        Notebook notebook1 = buildNotebook("testNotebook");
        notebookService.createNotebook(notebook1);

        Notebook notebook2 = buildNotebook("testNotebook2");
        notebookService.createNotebook(notebook2);

        List<Notebook> notebooks = notebookService.getAllNotebook();
        assertThat(notebooks.size()).isEqualTo(2);
        assertThat(notebooks.get(0).getTitle()).isEqualTo(notebook1.getTitle());
        assertThat(notebooks.get(1).getTitle()).isEqualTo(notebook2.getTitle());

        notebookService.deleteNotebook(notebooks.get(0).getId());
        notebookService.deleteNotebook(notebooks.get(0).getId());
    }

    @Test
    public void testFilterAndGetNotebook() {
        Notebook notebook = buildNotebook("testNotebook");
        notebookService.createNotebook(notebook);
        notebook = notebookService.getAllNotebook().get(0);

        Note note1 = buildNote("note1", Collections.singletonList("tag1"));
        Note note2 = buildNote("note2", Collections.emptyList());
        Note note3 = buildNote("note3", Arrays.asList("tag2", "tag1"));
        Note note4 = buildNote("note4", Arrays.asList("tag3", "tag5"));

        noteService.createNote(notebook.getId(), note1);
        noteService.createNote(notebook.getId(), note2);
        noteService.createNote(notebook.getId(), note3);
        noteService.createNote(notebook.getId(), note4);

        notebook = notebookService.filterAndGetNotebook(notebook.getId(), Collections.singletonList("tag1"));

        assertThat(notebook.getNotes().size()).isEqualTo(2);
        assertThat(notebook.getNotes().get(0).getTitle()).isEqualTo("note1");
        assertThat(notebook.getNotes().get(1).getTitle()).isEqualTo("note3");

        notebookService.deleteNotebook(notebook.getId());
    }

    @Test
    public void testUpdateNotebook() {
        Notebook notebook = buildNotebook("testNotebook");
        notebookService.createNotebook(notebook);

        Notebook existingNotebook = notebookService.getAllNotebook().get(0);
        existingNotebook.setTitle("newTitle");
        notebookService.updateNotebook(notebook);
        List<Notebook> notebooks = notebookService.getAllNotebook();
        assertThat(notebooks.size()).isEqualTo(1);
        assertThat(notebooks.get(0).getTitle()).isEqualTo("newTitle");

        notebookService.deleteNotebook(notebooks.get(0).getId());
    }

    @Test
    public void testDeleteNotebook() {
        Notebook notebook = buildNotebook("testNotebook");
        notebookService.createNotebook(notebook);
        Notebook existingNotebook = notebookService.getAllNotebook().get(0);

        notebookService.deleteNotebook(existingNotebook.getId());
        List<Notebook> notebooks = notebookService.getAllNotebook();
        assertThat(notebooks.size()).isEqualTo(0);
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
