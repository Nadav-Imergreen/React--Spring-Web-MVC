package hac.repo;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;

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

    public Visit() {}

    public Visit(String email, Date lastVisit) {
        this.email = email;
        this.lastVisit = lastVisit;
    }

    public void setId(long id) {
        this.id = id;
    }
    public long getId() {
        return id;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setLastVisit(Date lastVisit) {this.lastVisit = lastVisit;}
    public String getEmail() { return email; }
    public Date getLastVisit() { return lastVisit; }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", email=" + email + ", lastVisit=" + lastVisit + '}';
    }
}
