package indysfug.demo;

import com.google.common.collect.ImmutableList;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/people", produces = { MediaType.APPLICATION_JSON_VALUE, PersonV2Controller.MEDIA_TYPE_PERSON_V2_JSON })
public class PersonV2Controller {
    static final String MEDIA_TYPE_PERSON_V2_JSON = "application/vnd.indysfug.person.v2+json";

    @GetMapping
    List<PersonV2> listPeople() {
        return ImmutableList.of(PersonV2.randomFixture(), PersonV2.randomFixture());
    }

    @GetMapping("/{id}")
    PersonV2 find(@PathVariable("id") UUID id) {
        return PersonV2.randomFixture().setId(id);
    }

    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MEDIA_TYPE_PERSON_V2_JSON })
    @ResponseStatus(HttpStatus.CREATED)
    PersonV2 create(@Valid @RequestBody PersonV2 person) {
        return person.setId(UUID.randomUUID());
    }
}
