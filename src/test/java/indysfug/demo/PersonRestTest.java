package indysfug.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.StreamUtils;

import java.nio.charset.Charset;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest({ PersonV1Controller.class, PersonV2Controller.class })
public class PersonRestTest {
    @Autowired
    private MockMvc mvc;

    @Test
    public void testList() throws Exception {
        this.mvc.perform(get("/people").accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$[0].id", notNullValue()))
                .andExpect(jsonPath("$[0].firstName", notNullValue()))
                .andExpect(jsonPath("$[0].lastName", notNullValue()))
                .andExpect(jsonPath("$[0].email", notNullValue()))
                .andExpect(jsonPath("$[1].id", notNullValue()))
                .andExpect(jsonPath("$[1].firstName", notNullValue()))
                .andExpect(jsonPath("$[1].lastName", notNullValue()))
                .andExpect(jsonPath("$[1].email", notNullValue()));
    }

    @Test
    public void testListRejectsUnknownContentType() throws Exception {
        this.mvc.perform(get("/people").accept("application/vnd.indysfug.person.vFOO+json"))
                .andExpect(status().isNotAcceptable());

        this.mvc.perform(post("/people").accept("application/garbage"))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    public void testListV1() throws Exception {
        this.mvc.perform(get("/people").accept("application/vnd.indysfug.person.v1+json"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/vnd.indysfug.person.v1+json"))
                .andExpect(jsonPath("$[0].id", notNullValue()))
                .andExpect(jsonPath("$[0].fullName", notNullValue()))
                .andExpect(jsonPath("$[1].id", notNullValue()))
                .andExpect(jsonPath("$[1].fullName", notNullValue()));
    }

    @Test
    public void testListV2() throws Exception {
        this.mvc.perform(get("/people").accept("application/vnd.indysfug.person.v2+json"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/vnd.indysfug.person.v2+json"))
                .andExpect(jsonPath("$[0].id", notNullValue()))
                .andExpect(jsonPath("$[0].firstName", notNullValue()))
                .andExpect(jsonPath("$[0].lastName", notNullValue()))
                .andExpect(jsonPath("$[0].email", notNullValue()))
                .andExpect(jsonPath("$[1].id", notNullValue()))
                .andExpect(jsonPath("$[1].firstName", notNullValue()))
                .andExpect(jsonPath("$[1].lastName", notNullValue()))
                .andExpect(jsonPath("$[1].email", notNullValue()));
    }

    @Test
    public void testCreatePerson() throws Exception {
        String jsonRequestBody = readClasspathFileContents("person_v2_create.json");

        this.mvc.perform(post("/people").contentType("application/json").content(jsonRequestBody))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.firstName", is("Randy")))
                .andExpect(jsonPath("$.lastName", is("Marsh")))
                .andExpect(jsonPath("$.email", is("randy@sp.com")));
    }

    @Test
    public void testCreatePersonRejectsV1Body() throws Exception {
        String jsonRequestBody = readClasspathFileContents("person_v1_create.json");

        this.mvc.perform(post("/people").contentType("application/json").content(jsonRequestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreatePersonRejectIncompatibleHeaders() throws Exception {
        String jsonRequestBody = readClasspathFileContents("person_v2_create.json");

        this.mvc.perform(post("/people")
                    .contentType("application/vnd.indysfug.person.v2+json")
                    .accept("application/vnd.indysfug.person.v1+json")
                    .content(jsonRequestBody))
                .andExpect(status().isNotAcceptable());

        this.mvc.perform(post("/people")
                .contentType("application/vnd.indysfug.person.v1+json")
                .accept("application/vnd.indysfug.person.v2+json")
                .content(jsonRequestBody))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    public void testCreatePersonRejectsUnknownContentType() throws Exception {
        String jsonRequestBody = readClasspathFileContents("person_v2_create.json");

        this.mvc.perform(post("/people")
                .contentType("application/vnd.indysfug.person.vFOO+json")
                .accept("application/vnd.indysfug.person.vFOO+json")
                .content(jsonRequestBody))
                .andExpect(status().isUnsupportedMediaType());

        this.mvc.perform(post("/people")
                .contentType("application/garbage")
                .accept("application/garbage")
                .content(jsonRequestBody))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    public void testCreatePersonV1() throws Exception {
        String jsonRequestBody = readClasspathFileContents("person_v1_create.json");

        this.mvc.perform(post("/people").contentType("application/vnd.indysfug.person.v1+json").content(jsonRequestBody))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith("application/vnd.indysfug.person.v1+json"))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.fullName", is("Randy Marsh")));
    }

    @Test
    public void testCreatePersonV1RejectsV2Body() throws Exception {
        String jsonRequestBody = readClasspathFileContents("person_v2_create.json");

        this.mvc.perform(post("/people").contentType("application/vnd.indysfug.person.v1+json").content(jsonRequestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreatePersonV2() throws Exception {
        String jsonRequestBody = readClasspathFileContents("person_v2_create.json");

        this.mvc.perform(post("/people")
                    .contentType("application/vnd.indysfug.person.v2+json")
                    .accept("application/vnd.indysfug.person.v2+json")
                    .content(jsonRequestBody))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith("application/vnd.indysfug.person.v2+json"))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.firstName", is("Randy")))
                .andExpect(jsonPath("$.lastName", is("Marsh")))
                .andExpect(jsonPath("$.email", is("randy@sp.com")));
    }

    @Test
    public void testCreatePersonV2RejectsV1Body() throws Exception {
        String jsonRequestBody = readClasspathFileContents("person_v1_create.json");

        this.mvc.perform(post("/people").contentType("application/vnd.indysfug.person.v2+json").content(jsonRequestBody))
                .andExpect(status().isBadRequest());
    }

    private String readClasspathFileContents(String filePath) throws Exception {
        Resource file = new ClassPathResource(filePath);
        return StreamUtils.copyToString(file.getInputStream(), Charset.defaultCharset());
    }
}
