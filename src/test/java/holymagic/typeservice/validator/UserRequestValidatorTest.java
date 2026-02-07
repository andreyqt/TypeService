package holymagic.typeservice.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserRequestValidatorTest {

    private UserRequestValidator userRequestValidator;

    @BeforeEach
    public void setUp() {
        userRequestValidator = new UserRequestValidator();
    }

}
