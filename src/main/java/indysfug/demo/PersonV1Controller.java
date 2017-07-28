package indysfug.demo;

import com.google.common.collect.ImmutableList;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/people", produces = { PersonV1Controller.MEDIA_TYPE_PERSON_V1_JSON })
public class PersonV1Controller {
    static final String MEDIA_TYPE_PERSON_V1_JSON = "application/vnd.indysfug.person.v1+json";

    @GetMapping
    List<PersonV1> listPeople() {
        return ImmutableList.of(PersonV1.randomFixture(), PersonV1.randomFixture());
    }

    @GetMapping("/{id}")
    PersonV1 find(@PathVariable("id") UUID id) {
        return PersonV1.randomFixture().setId(id);
    }

    @PostMapping(consumes = { MEDIA_TYPE_PERSON_V1_JSON })
    @ResponseStatus(HttpStatus.CREATED)
    PersonV1 create(@Valid @RequestBody PersonV1 person) {
        return person.setId(UUID.randomUUID());
    }
}
