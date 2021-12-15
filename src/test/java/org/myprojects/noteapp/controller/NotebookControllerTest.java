package org.myprojects.noteapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
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

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class NotebookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetNonExistingNotebook() throws Exception {
        mockMvc.perform(get("/v1/notebook/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
    }

    @Test
    void testCreateNotebook() throws Exception {

        Notebook notebook = Notebook.builder()
                .id(null)
                .title("test")
                .notes(null)
                .build();

        mockMvc.perform( MockMvcRequestBuilders
        .post("/v1/notebook")
        .content(asJsonString(notebook))
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated());
    }

    @Test
    void testGetExistingNotebook() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();

        Notebook notebook = Notebook.builder()
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

        Notebook response = objectMapper.readValue(contentAsString, Notebook.class);

        mockMvc.perform(get("/v1/notebook/{id}",response.getId()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("test3"));

        mockMvc.perform(delete("/v1/notebook/{id}",response.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAllNotebook() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();

        Notebook notebook = Notebook.builder()
                .id(null)
                .title("test1")
                .notes(null)
                .build();

        MvcResult result1 = mockMvc.perform( MockMvcRequestBuilders
                            .post("/v1/notebook")
                            .content(asJsonString(notebook))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                            .andExpect(status().isCreated())
                             .andReturn();

        String content1 = result1.getResponse().getContentAsString();

        Notebook response1 = objectMapper.readValue(content1, Notebook.class);

        notebook = Notebook.builder()
                .id(null)
                .title("test2")
                .notes(null)
                .build();

        MvcResult result2 = mockMvc.perform( MockMvcRequestBuilders
                            .post("/v1/notebook")
                            .content(asJsonString(notebook))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                            .andExpect(status().isCreated())
                            .andReturn();

        String content2 = result2.getResponse().getContentAsString();

        Notebook response2 = objectMapper.readValue(content2, Notebook.class);

        mockMvc.perform(get("/v1/notebook"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].title").value("test1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].title").value("test2"));

        mockMvc.perform(delete("/v1/notebook/{id}",response1.getId()))
                .andExpect(status().isOk());
        mockMvc.perform(delete("/v1/notebook/{id}",response2.getId()))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteNotebook() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();

        Notebook notebook = Notebook.builder()
                .id(null)
                .title("test1")
                .notes(null)
                .build();

        MvcResult result = mockMvc.perform( MockMvcRequestBuilders
                        .post("/v1/notebook")
                        .content(asJsonString(notebook))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        Notebook response = objectMapper.readValue(content, Notebook.class);

        mockMvc.perform(delete("/v1/notebook/{id}",response.getId()))
                .andExpect(status().isOk());

    }

    @Test
    public void testFilterAndGetNotebook() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();

        Notebook notebook = Notebook.builder()
                .id(null)
                .title("test1")
                .notes(null)
                .build();

        MvcResult result1 = mockMvc.perform( MockMvcRequestBuilders
                        .post("/v1/notebook")
                        .content(asJsonString(notebook))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        String content = result1.getResponse().getContentAsString();

        Notebook response = objectMapper.readValue(content, Notebook.class);

        Note note = Note.builder()
                .id(null)
                .title("title1")
                .tags(Collections.singletonList("tag1"))
                .build();

        mockMvc.perform( MockMvcRequestBuilders
                        .post("/v1/notebook/{id}/note", response.getId())
                        .content(asJsonString(note))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        note = Note.builder()
                .id(null)
                .title("title2")
                .tags(Collections.singletonList("tag99"))
                .build();

        mockMvc.perform( MockMvcRequestBuilders
                        .post("/v1/notebook/{id}/note", response.getId())
                        .content(asJsonString(note))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/v1/notebook/{id}/tag99", response.getId()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.notes", hasSize(1)))
                .andExpect(jsonPath("$.notes[0].title").value("title2"));

        mockMvc.perform(delete("/v1/notebook/{id}",response.getId()))
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
