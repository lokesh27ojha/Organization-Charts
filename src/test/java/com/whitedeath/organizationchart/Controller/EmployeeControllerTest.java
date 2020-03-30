package com.whitedeath.organizationchart.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.whitedeath.organizationchart.Model.Employee;
import com.whitedeath.organizationchart.Model.PutEmployee;
import com.whitedeath.organizationchart.Repository.DesignationRepo;
import com.whitedeath.organizationchart.Repository.EmployeeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testng.annotations.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc

public class EmployeeControllerTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private DesignationRepo designationRepo;

    //get method for getting all employee's data
    @Test
    void showAllEmployeeDetails() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/employees");
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
    }

    //get method for particular employees. checking for right employee
    @Test
    void testShowDetailsRight() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/employees/2");
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
    }

    //get method for particular employees. checking for wrong employee having employee id negative
    @Test
    void testShowDetailsWrongEmployeeIdNegative() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/employees/-2");
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }

    //get method for particular employees. checking for wrong employee ID
    @Test
    void testShowDetailsWrong() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/employees/43");
        // MvcResult mvcResult =
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound());
        //.andReturn();

        // String content = mvcResult.getResponse().getContentAsString();
        //assertEquals("Not Valid Employee ID", content);
    }

    //post method. inserting the right employee
    @Test
    void addDetailsRight() throws Exception {
        Employee employee = new Employee("Lokesh", "Intern", 4);
        String input = mapToJson(employee);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/employees")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(input);

        mockMvc.perform(requestBuilder).andExpect(status().isCreated());
    }

    //post method. inserting the wrong job title
    @Test
    void addDetailsWrongJobTitle() throws Exception {
        Employee employee = new Employee("Sohil", "CEO", 1);
        String input = mapToJson(employee);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/employees")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(input);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andExpect(status().isBadRequest())
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("Can Not Insert Record, Not a Valid Job Title", content);
    }

    //post method. inserting the wrong employee as director. trying to insert intern with no manager
    @Test
    void addDetailsWrongManager() throws Exception {
        Employee employee = new Employee("Ojha", "Intern", -1);
        String input = mapToJson(employee);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/employees")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(input);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andExpect(status().isBadRequest())
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("Can Not Insert Record, Director Can't Have any Manager / Invalid Designation", content);
    }

    //post method. inserting the wrong employee as director. Director cant have any manager
    @Test
    void addDetailsWrongDirector() throws Exception {
        Employee employee = new Employee("Ramesh", "Director", 4);
        String input = mapToJson(employee);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/employees")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(input);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andExpect(status().isBadRequest())
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("Can Not Insert Record, Director Can't Have any Manager / Invalid Designation", content);
    }

    //post method. inserting wrong manager ID with QA
    @Test
    void addDetailsWrongManagerID() throws Exception {
        Employee employee = new Employee("Mohan", "QA", 342);
        String input = mapToJson(employee);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/employees")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(input);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andExpect(status().isBadRequest())
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("Can Not Insert Record, Not a Valid Manager", content);
    }

    //post method. inserting wrong manager ID with Director
    @Test
    void addDetailsWrongManagerIdWithDirector() throws Exception {
        Employee employee = new Employee("Mohan", "Director", 335);
        String input = mapToJson(employee);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/employees")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(input);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andExpect(status().isBadRequest())
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("Can Not Insert Record, Not a Valid Manager", content);
    }

    //post method. inserting right record as director without manager
    @Test
    void addDetailsRightDirectorWithoutManager() throws Exception {
        Employee employee = new Employee("Lokesh", "Director", -1);
        String input = mapToJson(employee);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/employees")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(input);

        mockMvc.perform(requestBuilder).andExpect(status().isCreated());
    }

    //post method. inserting wrong record as name empty
    @Test
    void addDetailsWrongNameEmpty() throws Exception {
        Employee employee = new Employee("", "Developer", 4);
        String input = mapToJson(employee);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/employees")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(input);

        mockMvc.perform(requestBuilder).andExpect(status().isBadRequest());
    }

    //post method. inserting wrong record as jobtitle empty
    @Test
    void addDetailsWrongJobTitleEmpty() throws Exception {
        Employee employee = new Employee("Suresh", "", 4);
        String input = mapToJson(employee);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/employees")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(input);

        mockMvc.perform(requestBuilder).andExpect(status().isBadRequest());
    }

    //post method. inserting wrong record as name with numbers
    @Test
    void addDetailsWrongNameWithNumbers() throws Exception {
        Employee employee = new Employee("Suresh123", "DevOps", 1);
        String input = mapToJson(employee);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/employees")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(input);

        mockMvc.perform(requestBuilder).andExpect(status().isBadRequest());
    }

    //post method. inserting wrong record as name with invalid symbols
    @Test
    void addDetailsWrongNameWithSymbols() throws Exception {
        Employee employee = new Employee("jaya@ojha", "DevOps", 1);
        String input = mapToJson(employee);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/employees")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(input);

        mockMvc.perform(requestBuilder).andExpect(status().isBadRequest());
    }

    //post method. inserting wrong record as we are sending nothing
    @Test
    void addDetailsWrongWithoutData() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/employees");

        mockMvc.perform(requestBuilder).andExpect(status().isBadRequest());
    }

    //post method. inserting wrong record as name with jobtitle null
    @Test
    void addDetailsWrongJobTitleNull() throws Exception {
        Employee employee = new Employee("Manna", null, 3);
        String input = mapToJson(employee);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/employees")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(input);

        mockMvc.perform(requestBuilder).andExpect(status().isBadRequest());
    }

    //post method. inserting wrong record as name with name null
    @Test
    void addDetailsWrongNameNull() throws Exception {
        Employee employee = new Employee(null, "Intern", 3);
        String input = mapToJson(employee);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/employees")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(input);

        mockMvc.perform(requestBuilder).andExpect(status().isBadRequest());
    }

    //post method. inserting wrong record as name with manager id null
    @Test
    void addDetailsWrongManagerIdNull() throws Exception {
        Employee employee = new Employee("Mukesh", "Intern", null);
        String input = mapToJson(employee);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/employees")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(input);

        mockMvc.perform(requestBuilder).andExpect(status().isBadRequest());
    }

    //put method, updating right details with replace as false
    @Test
    void putEmployeeRightReplaceFalse() throws Exception {
        PutEmployee employee = new PutEmployee("Lokesh", "Manager", 1, false);
        String input = mapToJson(employee);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/employees/2")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(input);

        mockMvc.perform(requestBuilder).andExpect(status().isCreated());
    }

    //put method, updating right details with replace as true
    @Test
    void putEmployeeRightReplaceTrue() throws Exception {
        PutEmployee employee = new PutEmployee("Modi", "Manager", 1, true);
        String input = mapToJson(employee);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/employees/2")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(input);

        mockMvc.perform(requestBuilder).andExpect(status().isCreated());
    }

    //put method, updating wrong details with no data at all
    @Test
    void putEmployeeWrongNoData() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/employees/4");
        mockMvc.perform(requestBuilder).andExpect(status().isBadRequest());
    }

    //put method, updating wrong details name as null
    @Test
    void putEmployeeWrongNameNull() throws Exception {
        PutEmployee employee = new PutEmployee(null, "Manager", 1, true);
        String input = mapToJson(employee);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/employees/3")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(input);
        mockMvc.perform(requestBuilder).andExpect(status().isBadRequest());
    }

    //put method, updating wrong details job title as null
    @Test
    void putEmployeeWrongJobTitleNull() throws Exception {
        PutEmployee employee = new PutEmployee("Ojha", null, 1, true);
        String input = mapToJson(employee);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/employees/4")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(input);
        mockMvc.perform(requestBuilder).andExpect(status().isBadRequest());
    }

    //put method, updating wrong details managerID as null
    @Test
    void putEmployeeWrongManagerIdNull() throws Exception {
        PutEmployee employee = new PutEmployee("Loki", "Manager", null, true);
        String input = mapToJson(employee);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/employees/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(input);
        mockMvc.perform(requestBuilder).andExpect(status().isBadRequest());
    }

    //put method, updating wrong details replace as null
    @Test
    void putEmployeeWrongReplaceNull() throws Exception {
        PutEmployee employee = new PutEmployee("Sourav", "Manager", 1, null);
        String input = mapToJson(employee);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/employees/6")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(input);
        mockMvc.perform(requestBuilder).andExpect(status().isBadRequest());
    }

    //put method, updating wrong details as wrong jobtitle
    @Test
    void putEmployeeWrongJobTitle() throws Exception {
        PutEmployee employee = new PutEmployee("Sourav", "CEO", -1, true);
        String input = mapToJson(employee);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/employees/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(input);
        mockMvc.perform(requestBuilder).andExpect(status().isBadRequest());
    }

    //put method, updating wrong details as name empty
    @Test
    void putEmployeeWrongNameEmpty() throws Exception {
        PutEmployee employee = new PutEmployee("", "CEO", 1, true);
        String input = mapToJson(employee);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/employees/5")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(input);
        mockMvc.perform(requestBuilder).andExpect(status().isBadRequest());
    }

    //put method, updating wrong details as job title empty
    @Test
    void putEmployeeWrongJobTitleEmpty() throws Exception {
        PutEmployee employee = new PutEmployee("Rajesh", "", 1, false);
        String input = mapToJson(employee);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/employees/5")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(input);
        mockMvc.perform(requestBuilder).andExpect(status().isBadRequest());
    }

    //put method, updating wrong details as wrong name
    @Test
    void putEmployeeWrongName() throws Exception {
        PutEmployee employee = new PutEmployee("raja123ojha", "DevOps", 2, true);
        String input = mapToJson(employee);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/employees/5")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(input);
        mockMvc.perform(requestBuilder).andExpect(status().isBadRequest());
    }

    //put method, updating wrong details as name with invalid characters
    @Test
    void putEmployeeWrongNameWithCharacter() throws Exception {
        PutEmployee employee = new PutEmployee("Umesh@soni", "DevOps", 1, false);
        String input = mapToJson(employee);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/employees/5")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(input);
        mockMvc.perform(requestBuilder).andExpect(status().isBadRequest());
    }

    //put method, updating wrong details as manager having low designation
    @Test
    void putEmployeeWrongManagerLowDesignation() throws Exception {
        PutEmployee employee = new PutEmployee("Akash", "Manager", 2, false);
        String input = mapToJson(employee);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/employees/6")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(input);
        mockMvc.perform(requestBuilder).andExpect(status().isBadRequest());
    }

    //put method, updating wrong details as subordinates having high designation
    @Test
    void putEmployeeWrongSubordinateHighDesignation() throws Exception {
        PutEmployee employee = new PutEmployee("Priyanka", "Intern", 1, true);
        String input = mapToJson(employee);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/employees/4")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(input);
        mockMvc.perform(requestBuilder).andExpect(status().isBadRequest());
    }

    //delete method. for deleting right employee
    @Test
    void deletePlayerRight() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/employees/3");
        mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent());
    }

    //delete method. for deleting wrong employee
    @Test
    void deletePlayerWrongId() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/employees/567");
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("No Record Exist for such ID", content);
    }

    //delete method. for deleting the Director having subordinates
    @Test
    void deletePlayerWrongManagerWithSubordinates() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/employees/1");
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("Can Not Delete Employee as He is Director Having Subordinates", content);
    }

    //delete method. for deleting wrong employee having id as negative
    @Test
    void deletePlayerWrongEmployeeIdNegative() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/employees/-3");
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }


    private String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    private <T> T mapFromJson(String json, Class<T> clazz) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, clazz);
    }
}