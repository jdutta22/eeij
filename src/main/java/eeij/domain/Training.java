package eeij.domain;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Training.
 */
@Entity
@Table(name = "training")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Training implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "training_name")
    private String trainingName;

    @Column(name = "training_mode")
    private String trainingMode;

    @Column(name = "training_type")
    private String trainingType;

    @Column(name = "training_start_date")
    private LocalDate trainingStartDate;

    @Column(name = "traing_end_date")
    private LocalDate traingEndDate;

    @Column(name = "training_year")
    private String trainingYear;

    @Column(name = "training_registration")
    private Boolean trainingRegistration;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Training id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTrainingName() {
        return this.trainingName;
    }

    public Training trainingName(String trainingName) {
        this.setTrainingName(trainingName);
        return this;
    }

    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }

    public String getTrainingMode() {
        return this.trainingMode;
    }

    public Training trainingMode(String trainingMode) {
        this.setTrainingMode(trainingMode);
        return this;
    }

    public void setTrainingMode(String trainingMode) {
        this.trainingMode = trainingMode;
    }

    public String getTrainingType() {
        return this.trainingType;
    }

    public Training trainingType(String trainingType) {
        this.setTrainingType(trainingType);
        return this;
    }

    public void setTrainingType(String trainingType) {
        this.trainingType = trainingType;
    }

    public LocalDate getTrainingStartDate() {
        return this.trainingStartDate;
    }

    public Training trainingStartDate(LocalDate trainingStartDate) {
        this.setTrainingStartDate(trainingStartDate);
        return this;
    }

    public void setTrainingStartDate(LocalDate trainingStartDate) {
        this.trainingStartDate = trainingStartDate;
    }

    public LocalDate getTraingEndDate() {
        return this.traingEndDate;
    }

    public Training traingEndDate(LocalDate traingEndDate) {
        this.setTraingEndDate(traingEndDate);
        return this;
    }

    public void setTraingEndDate(LocalDate traingEndDate) {
        this.traingEndDate = traingEndDate;
    }

    public String getTrainingYear() {
        return this.trainingYear;
    }

    public Training trainingYear(String trainingYear) {
        this.setTrainingYear(trainingYear);
        return this;
    }

    public void setTrainingYear(String trainingYear) {
        this.trainingYear = trainingYear;
    }

    public Boolean getTrainingRegistration() {
        return this.trainingRegistration;
    }

    public Training trainingRegistration(Boolean trainingRegistration) {
        this.setTrainingRegistration(trainingRegistration);
        return this;
    }

    public void setTrainingRegistration(Boolean trainingRegistration) {
        this.trainingRegistration = trainingRegistration;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Training)) {
            return false;
        }
        return id != null && id.equals(((Training) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Training{" +
            "id=" + getId() +
            ", trainingName='" + getTrainingName() + "'" +
            ", trainingMode='" + getTrainingMode() + "'" +
            ", trainingType='" + getTrainingType() + "'" +
            ", trainingStartDate='" + getTrainingStartDate() + "'" +
            ", traingEndDate='" + getTraingEndDate() + "'" +
            ", trainingYear='" + getTrainingYear() + "'" +
            ", trainingRegistration='" + getTrainingRegistration() + "'" +
            "}";
    }
}
