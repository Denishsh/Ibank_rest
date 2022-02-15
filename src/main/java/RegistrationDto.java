import lombok.Data;

@Data
public class RegistrationDto {
    private final String login;
    private final String password;
    private final String status;

}
