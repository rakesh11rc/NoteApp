package org.myprojects.noteapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.myprojects.noteapp.model.Note;
import org.myprojects.noteapp.model.Notebook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static Notebook notebook;

    @BeforeAll
    public void setup() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

         notebook = Notebook.builder()
                .id(null)
                .title("test3")
                .notes(null)
                .build();

        MvcResult result = mockMvc.perform( MockMvcRequestBuilders
                        .post("/v1/notebook")
                        .content(asJsonString(notebook))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        String contentAsString = result.getResponse().getContentAsString();

        notebook = objectMapper.readValue(contentAsString, Notebook.class);
    }

    @AfterAll
    public void cleanup() throws Exception {
        mockMvc.perform(delete("/v1/notebook/{id}",notebook.getId()))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreateNote() throws Exception {
        Note note = Note.builder()
                .id(null)
                .title("test")
                .tags(Collections.singletonList("test"))
                .build();

        mockMvc.perform( MockMvcRequestBuilders
        .post("/v1/notebook/{id}/note", notebook.getId())
        .content(asJsonString(note))
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated());
    }

    @Test
    void testGetNonExistingNote() throws Exception {
        mockMvc.perform(get("/v1/notebook/{id}/note/{noteId}", notebook.getId(), 1L))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
    }

    @Test
    public void testGetExistingNote() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();

        Note note = Note.builder()
                .id(null)
                .title("test1")
                .tags(Collections.singletonList("test"))
                .build();

        MvcResult result = mockMvc.perform( MockMvcRequestBuilders
                            .post("/v1/notebook/{id}/note", notebook.getId())
                            .content(asJsonString(note))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                            .andExpect(status().isCreated())
                            .andReturn();

        String contentAsString = result.getResponse().getContentAsString();

        Note response = objectMapper.readValue(contentAsString, Note.class);

        mockMvc.perform(get("/v1/notebook/{id}/note/{noteId}",notebook.getId(), response.getId()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("test1"));

        mockMvc.perform(delete("/v1/notebook/{id}/note/{noteId}",notebook.getId(), response.getId()))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteNote() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();

        Note note = Note.builder()
                .id(null)
                .title("test1")
                .tags(Collections.singletonList("test"))
                .build();

        MvcResult result = mockMvc.perform( MockMvcRequestBuilders
                        .post("/v1/notebook/{id}/note", notebook.getId())
                        .content(asJsonString(note))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        String contentAsString = result.getResponse().getContentAsString();

        Note response = objectMapper.readValue(contentAsString, Note.class);

        mockMvc.perform(delete("/v1/notebook/{id}/note/{noteId}",notebook.getId(), response.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/v1/notebook/{id}/note/{noteId}",notebook.getId(), response.getId()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());

    }

    @Test
    public void testUpdateNote() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();

        Note note = Note.builder()
                .id(null)
                .title("test1")
                .tags(Collections.singletonList("test"))
                .build();

        MvcResult result = mockMvc.perform( MockMvcRequestBuilders
                        .post("/v1/notebook/{id}/note", notebook.getId())
                        .content(asJsonString(note))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        String contentAsString = result.getResponse().getContentAsString();

        Note response = objectMapper.readValue(contentAsString, Note.class);

        mockMvc.perform(get("/v1/notebook/{id}/note/{noteId}",notebook.getId(), response.getId()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("test1"));

        response.setTitle("newTest");

        result = mockMvc.perform( MockMvcRequestBuilders
                        .put("/v1/notebook/{id}/note", notebook.getId())
                        .content(asJsonString(response))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        contentAsString = result.getResponse().getContentAsString();

        response = objectMapper.readValue(contentAsString, Note.class);

        mockMvc.perform(get("/v1/notebook/{id}/note/{noteId}",notebook.getId(), response.getId()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("newTest"));

        mockMvc.perform(delete("/v1/notebook/{id}/note/{noteId}",notebook.getId(), response.getId()))
                .andExpect(status().isOk());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
