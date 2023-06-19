package hac.repo;
import jakarta.persistence.*;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.UniqueElements;

import java.io.Serializable;

@Entity
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotEmpty(message = "Name is mandatory")
    private String userName;

    @NotEmpty(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    @Column(unique = true)
    private String email;

    @NotEmpty(message = "Password is mandatory")
    private String password;

    public User() {}

    public User(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public void setId(long id) {
        this.id = id;
    }
    public long getId() {
        return id;
    }

    public void setUserName(String userName) {
        // Annotation may not be enough
        // you can also perform your own validation inside setters
        // exception should be caught by service/controller
        if (userName.length() > 32)
            throw new IllegalArgumentException("Name cannot exceed 32 characters");
        this.userName = userName;
    }
    public String getUserName() {return userName;}

    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getEmail() { return email; }
    public String getPassword() { return password; }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", userName=" + userName + ", email=" + email + '}';
    }
}
