package hac.repo;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

@Entity
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private long id;

    @NotEmpty(message = "Name is mandatory")
    private String userName;

    @NotEmpty(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    @Column(unique = true)
    private String email;

    @NotEmpty(message = "Password is mandatory")
    @Size(min = 3, message = "password should have at least 3 characters")
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Visit> visits = new ArrayList<>();

    public User() {}

    public User(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public void setId(long id) {
        this.id = id;
    }
    public long getId() {
        return id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
        // exception should be caught by service/controller
//        if (userName.length() > 32)
//            throw new IllegalArgumentException("Name cannot exceed 32 characters");

    public String getUserName() {return userName;}

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getEmail() { return email; }
    public String getPassword() { return password; }

    public List<Visit> getVisits() {
        return visits;
    }

    public void setVisits(List<Visit> visits) {
        this.visits = visits;
    }

    // Convenience method to add a visit to the user
    public void addVisit(Visit visit) {
        visits.add(visit);
        visit.setUser(this);
    }

    // Convenience method to remove a visit from the user
    public void removeVisit(Visit visit) {
        visits.remove(visit);
        visit.setUser(null);
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", userName=" + userName + ", email=" + email + '}';
    }
}
