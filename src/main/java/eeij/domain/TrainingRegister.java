package eeij.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TrainingRegister.
 */
@Entity
@Table(name = "training_register")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TrainingRegister implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_attendance")
    private Float userAttendance;

    @Column(name = "cerificate")
    private Boolean cerificate;

    @ManyToOne
    private Training training;

    @ManyToOne
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TrainingRegister id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getUserAttendance() {
        return this.userAttendance;
    }

    public TrainingRegister userAttendance(Float userAttendance) {
        this.setUserAttendance(userAttendance);
        return this;
    }

    public void setUserAttendance(Float userAttendance) {
        this.userAttendance = userAttendance;
    }

    public Boolean getCerificate() {
        return this.cerificate;
    }

    public TrainingRegister cerificate(Boolean cerificate) {
        this.setCerificate(cerificate);
        return this;
    }

    public void setCerificate(Boolean cerificate) {
        this.cerificate = cerificate;
    }

    public Training getTraining() {
        return this.training;
    }

    public void setTraining(Training training) {
        this.training = training;
    }

    public TrainingRegister training(Training training) {
        this.setTraining(training);
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public TrainingRegister user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TrainingRegister)) {
            return false;
        }
        return id != null && id.equals(((TrainingRegister) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TrainingRegister{" +
            "id=" + getId() +
            ", userAttendance=" + getUserAttendance() +
            ", cerificate='" + getCerificate() + "'" +
            "}";
    }
}
