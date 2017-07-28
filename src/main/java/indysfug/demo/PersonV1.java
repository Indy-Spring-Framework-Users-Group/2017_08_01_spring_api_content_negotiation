package indysfug.demo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.RandomStringUtils;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PersonV1 {
    private UUID id;
    @NotNull
    private String fullName;

    public static PersonV1 randomFixture() {
        return new PersonV1()
                .setId(UUID.randomUUID())
                .setFullName(RandomStringUtils.randomAlphabetic(8) + " " + RandomStringUtils.randomAlphabetic(10));
    }
}
