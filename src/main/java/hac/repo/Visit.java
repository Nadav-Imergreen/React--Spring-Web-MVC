package hac.repo;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;
/**
 * The Visit class represents a user visit and stores information such as the email of the user and the last visit date.
 * It is an entity class that is persisted in the database.
 */
@Entity
public class Visit implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private long id;

    @NotEmpty(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    private String email;

    private Date lastVisit;

    /**
     * Constructs a new Visit object.
     */
    public Visit() {}

    /**
     * Constructs a new Visit object with the specified email and last visit date.
     *
     * @param email     The email of the user associated with the visit.
     * @param lastVisit The date of the last visit.
     */
    public Visit(String email, Date lastVisit) {
        this.email = email;
        this.lastVisit = lastVisit;
    }

    /**
     * Sets the ID of the visit.
     *
     * @param id The ID of the visit.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Retrieves the ID of the visit.
     *
     * @return The ID of the visit.
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the email of the user associated with the visit.
     *
     * @param email The email of the user.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Sets the date of the last visit.
     *
     * @param lastVisit The date of the last visit.
     */
    public void setLastVisit(Date lastVisit) {
        this.lastVisit = lastVisit;
    }

    /**
     * Retrieves the email of the user associated with the visit.
     *
     * @return The email of the user.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Retrieves the date of the last visit.
     *
     * @return The date of the last visit.
     */
    public Date getLastVisit() {
        return lastVisit;
    }

    @Override
    public String toString() {
        return "Visit{" + "id=" + id + ", email=" + email + ", lastVisit=" + lastVisit + '}';
    }
}
