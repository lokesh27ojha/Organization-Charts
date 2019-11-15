package com.whitedeath.organizationchart.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.whitedeath.organizationchart.Model.Designation;
import com.whitedeath.organizationchart.Repository.DesignationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testng.annotations.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc

public class DesignationControllerTest extends AbstractTestNGSpringContextTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DesignationRepo designationRepo;

    //get method for getting all designations
    @Test
    void showAllDesignationsDetails() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/designations");
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
    }

    //get method. for getting a particular designation. checking for right designation
    @Test
    void showDesignationRight() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/designations/1");
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
    }

    //get method. for getting a single designation. checking for wrong designationId that doesnt exist
    @Test
    void showDesignationWrongDesignationId() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/designations/456");
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound());
    }

    //get method. for getting a single designation. checking for wrong designationId that is negative
    @Test
    void showDesignationNegativeDesignationId() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/designations/-10");
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }

    //post method. for inserting the right designation details
    @Test
    void insertDesignationRightDetails() throws Exception {
        Designation designation = new Designation(11, "Lokesh");
        String input = mapToJson(designation);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/designations")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(input);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated());
    }

    //post method. for inserting wrong designation details as designationId is less than zero
    @Test
    void insertDesignationWrongDetailDesignationIdLessThanZero() throws Exception {
        Designation designation = new Designation(-233, "Ojha");
        String input = mapToJson(designation);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/designations")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(input);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }

    //post method. for inserting wrong designation details as designationId is null
    @Test
    void insertDesignationWrongDetailDesignationIdNull() throws Exception {
        Designation designation = new Designation(null, "sohil");
        String input = mapToJson(designation);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/designations")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(input);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }

    //post method. for inserting wrong designation details as name is null
    @Test
    void insertDesignationWrongDetailNameNull() throws Exception {
        Designation designation = new Designation(12, null);
        String input = mapToJson(designation);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/designations")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(input);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }

    //post method. for inserting wrong designation details as name empty
    @Test
    void insertDesignationWrongDetailsNameEmpty() throws Exception {
        Designation designation = new Designation(12, "");
        String input = mapToJson(designation);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/designations")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(input);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }

    //post method. for inserting wrong details as name having numbers
    @Test
    void insertDesignationWrongDetailsNameWithNumbers() throws Exception {
        Designation designation = new Designation(14, "loki123");
        String input = mapToJson(designation);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/designations")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(input);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }


    //post method. for inserting wrong details as name having invalid characters
    @Test
    void insertDesignationWrongDetailsNameWithInvalidCharacters() throws Exception {
        Designation designation = new Designation(14, "loki@ojha");
        String input = mapToJson(designation);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/designations")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(input);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }


    //post method. for inserting wrong details as no data at all
    @Test
    void insertDesignationWrongDetailsNoData() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/designations");
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }


    //delete method. deleting right designation
    @Test
    void deleteRightDesignation() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/designations/7");
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent());
    }

    //delete method. deleting wrong designation as designationId doesnt exist
    @Test
    void deleteWrongDesignationIdNotExist() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/designations/568");
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound());
    }

    //delete method. deleting wrong designation as designationId is negative
    @Test
    void deleteWrongDesignationIdNegative() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/designations/-45");
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }

    private String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }
}
