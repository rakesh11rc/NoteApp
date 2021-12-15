package org.myprojects.noteapp.controller;

import org.myprojects.noteapp.model.Note;
import org.myprojects.noteapp.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/notebook/{id}/note")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @GetMapping(value = "{noteId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Note get(@PathVariable("id") String notebookId, @PathVariable("noteId") String noteId) {
        return noteService.getNote(notebookId, noteId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Note> create(@PathVariable("id") String notebookId, @RequestBody Note note) throws Exception {
        noteService.createNote(notebookId, note );
        return new ResponseEntity(note, HttpStatus.CREATED);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Note> update(@PathVariable("id") String notebookId, @RequestBody Note note) {
        noteService.updateNote(notebookId, note);
        return new ResponseEntity(note, HttpStatus.OK);
    }

    @DeleteMapping("{noteId}")
    public ResponseEntity delete(@PathVariable("id") String notebookId, @PathVariable("noteId") String noteId) {
        noteService.deleteNote(notebookId, noteId);
        return new ResponseEntity(HttpStatus.OK);
    }


}
