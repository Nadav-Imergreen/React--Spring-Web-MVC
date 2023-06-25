package hac.repo;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
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

    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Visit> visits;

    /**
     * Setter method to add a visit to the user's visits list.
     *
     * @param visit the visit to be added
     */
    public void setVisits(Visit visit) {
        this.visits.add(visit);
    }

    /**
     * Getter method for retrieving the user's visits list.
     *
     * @return the user's visits list
     */
    public List<Visit> getVisits(){
        return this.visits;
    }

    /**
     * Default constructor.
     */
    public User() {}

    /**
     * Constructor to initialize the user with provided attributes.
     *
     * @param userName the username of the user
     * @param email    the email of the user
     * @param password the password of the user
     * @param visits   the visits list of the user
     */
    public User(String userName, String email, String password, List<Visit> visits) {
        this.userName = userName;
        this.email = email;
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
        this.visits = visits;
    }

    /**
     * Setter method to set the user's ID.
     *
     * @param id the ID to be set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Getter method to retrieve the user's ID.
     *
     * @return the user's ID
     */
    public long getId() {
        return id;
    }

    /**
     * Setter method to set the user's username.
     *
     * @param userName the username to be set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Getter method to retrieve the user's username.
     *
     * @return the user's username
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Setter method to set the user's email.
     *
     * @param email the email to be set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Setter method to set the user's password.
     *
     * @param password the password to be set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Getter method to retrieve the user's email.
     *
     * @return the user's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Getter method to retrieve the user's password.
     *
     * @return the user's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Overrides the default toString() method to provide a string representation of the User object.
     *
     * @return a string representation of the User object
     */
    @Override
    public String toString() {
        return "User{" + "id=" + id + ", userName=" + userName + ", email=" + email + '}';
    }
}
