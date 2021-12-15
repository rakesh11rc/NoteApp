package org.myprojects.noteapp.service;

import org.myprojects.noteapp.model.Note;
import org.myprojects.noteapp.model.Notebook;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class NotebookService {

    private List<Notebook> notebooks = new ArrayList<>();

    public List<Notebook> getAllNotebook() {
        return notebooks;
    }

    public Notebook getNotebook(final String id) {
        return notebooks.stream()
                .filter(notebook -> notebook.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Notebook filterAndGetNotebook(final String id, final List<String> tags) {
        Notebook existingNotebook = notebooks.stream()
                                    .filter(notebook -> notebook.getId().equals(id))
                                    .findFirst()
                                    .orElse(null);

        if(existingNotebook != null) {
            List<Note> notes = existingNotebook.getNotes().stream()
                    .filter(note -> note.getTags().stream().anyMatch(tags::contains))
                    .collect(Collectors.toList());

            Notebook filteredNotebook = Notebook.builder()
                    .id(existingNotebook.getId())
                    .title(existingNotebook.getTitle())
                    .notes(notes)
                    .build();

            return filteredNotebook;
        }

        return existingNotebook;
    }

    public void createNotebook(final Notebook notebook) {
        String uuid = UUID.randomUUID().toString();
        notebook.setId(uuid);
        notebook.setNotes(new ArrayList<>());
        notebooks.add(notebook);
    }

    public void updateNotebook(final Notebook notebook) {
        Notebook existingNotebook = getNotebook(notebook.getId());
        if(Objects.nonNull(existingNotebook)) {
            notebooks.remove(existingNotebook);
            notebooks.add(notebook);
        }
    }

    public void deleteNotebook(final String id) {
        if(Objects.nonNull(getNotebook(id))) {
            notebooks.remove(getNotebook(id));
        }
    }
}
