package indysfug.demo;

import com.google.common.collect.ImmutableList;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/people")
public class PersonController {
    private static final String MEDIA_TYPE_PERSON_V1_JSON = "application/vnd.indysfug.person.v1+json";

    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MEDIA_TYPE_PERSON_V1_JSON })
    List<PersonV1> listPersonV1DTO() {
        return ImmutableList.of(PersonV1.randomFixture(), PersonV1.randomFixture());
    }

    @GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE, MEDIA_TYPE_PERSON_V1_JSON })
    PersonV1 findPersonV1DTO(@PathVariable("id") UUID id) {
        return PersonV1.randomFixture().setId(id);
    }

    @PostMapping(
        consumes = { MediaType.APPLICATION_JSON_VALUE, MEDIA_TYPE_PERSON_V1_JSON },
        produces = { MediaType.APPLICATION_JSON_VALUE, MEDIA_TYPE_PERSON_V1_JSON })
    @ResponseStatus(HttpStatus.CREATED)
    PersonV1 createPersonV1DTO(@Valid @RequestBody PersonV1 personV1) {
        return personV1.setId(UUID.randomUUID());
    }
}
