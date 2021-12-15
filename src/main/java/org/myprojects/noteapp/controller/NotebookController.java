package org.myprojects.noteapp.controller;

import org.myprojects.noteapp.model.Notebook;
import org.myprojects.noteapp.service.NotebookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/notebook")
public class NotebookController {

    @Autowired
    private NotebookService noteService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Notebook> getAll() {
        return noteService.getAllNotebook();
    }

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Notebook get(@PathVariable("id") String id) {
        return noteService.getNotebook(id);
    }

    @GetMapping(value = "{id}/{tags}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Notebook getWithTags(@PathVariable("id") String id, @PathVariable("tags") List<String> tags) {
        return noteService.filterAndGetNotebook(id, tags);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Notebook> create(@RequestBody Notebook notebook) {
        noteService.createNotebook(notebook);
        return new ResponseEntity<>(notebook, HttpStatus.CREATED);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Notebook> update(@RequestBody Notebook notebook) {
        noteService.updateNotebook(notebook);
        return new ResponseEntity<>(notebook, HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable("id") String id) {
        noteService.deleteNotebook(id);
        return new ResponseEntity(HttpStatus.OK);
    }

}
