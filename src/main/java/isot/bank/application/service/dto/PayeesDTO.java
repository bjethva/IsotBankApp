package isot.bank.application.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link isot.bank.application.domain.Payees} entity.
 */
public class PayeesDTO implements Serializable {

    private Long id;

    private Integer payeeID;

    @NotNull
    @Pattern(regexp = "^[_.@A-Za-z0-9-]*$")
    private String email;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    private String telephone;


    private Long userTOPayeeId;

    private String userTOPayeeLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPayeeID() {
        return payeeID;
    }

    public void setPayeeID(Integer payeeID) {
        this.payeeID = payeeID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Long getUserTOPayeeId() {
        return userTOPayeeId;
    }

    public void setUserTOPayeeId(Long userId) {
        this.userTOPayeeId = userId;
    }

    public String getUserTOPayeeLogin() {
        return userTOPayeeLogin;
    }

    public void setUserTOPayeeLogin(String userLogin) {
        this.userTOPayeeLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PayeesDTO payeesDTO = (PayeesDTO) o;
        if (payeesDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), payeesDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PayeesDTO{" +
            "id=" + getId() +
            ", payeeID=" + getPayeeID() +
            ", email='" + getEmail() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", telephone='" + getTelephone() + "'" +
            ", userTOPayee=" + getUserTOPayeeId() +
            ", userTOPayee='" + getUserTOPayeeLogin() + "'" +
            "}";
    }
}
