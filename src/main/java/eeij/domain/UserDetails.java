package eeij.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UserDetails.
 */
@Entity
@Table(name = "user_details")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "designation")
    private String designation;

    @Column(name = "caste")
    private String caste;

    @Column(name = "address")
    private String address;

    @Column(name = "department")
    private String department;

    @Column(name = "discipline")
    private String discipline;

    @Column(name = "state")
    private String state;

    @Column(name = "bank_account")
    private Integer bankAccount;

    @Column(name = "ifsc_code")
    private String ifscCode;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserDetails id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDesignation() {
        return this.designation;
    }

    public UserDetails designation(String designation) {
        this.setDesignation(designation);
        return this;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getCaste() {
        return this.caste;
    }

    public UserDetails caste(String caste) {
        this.setCaste(caste);
        return this;
    }

    public void setCaste(String caste) {
        this.caste = caste;
    }

    public String getAddress() {
        return this.address;
    }

    public UserDetails address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDepartment() {
        return this.department;
    }

    public UserDetails department(String department) {
        this.setDepartment(department);
        return this;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDiscipline() {
        return this.discipline;
    }

    public UserDetails discipline(String discipline) {
        this.setDiscipline(discipline);
        return this;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }

    public String getState() {
        return this.state;
    }

    public UserDetails state(String state) {
        this.setState(state);
        return this;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getBankAccount() {
        return this.bankAccount;
    }

    public UserDetails bankAccount(Integer bankAccount) {
        this.setBankAccount(bankAccount);
        return this;
    }

    public void setBankAccount(Integer bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getIfscCode() {
        return this.ifscCode;
    }

    public UserDetails ifscCode(String ifscCode) {
        this.setIfscCode(ifscCode);
        return this;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserDetails user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserDetails)) {
            return false;
        }
        return id != null && id.equals(((UserDetails) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserDetails{" +
            "id=" + getId() +
            ", designation='" + getDesignation() + "'" +
            ", caste='" + getCaste() + "'" +
            ", address='" + getAddress() + "'" +
            ", department='" + getDepartment() + "'" +
            ", discipline='" + getDiscipline() + "'" +
            ", state='" + getState() + "'" +
            ", bankAccount=" + getBankAccount() +
            ", ifscCode='" + getIfscCode() + "'" +
            "}";
    }
}
