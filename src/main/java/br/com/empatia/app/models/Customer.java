package br.com.empatia.app.models;

import br.com.empatia.app.enums.CustomerSex;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Builder
@AllArgsConstructor
@Table(name = "customers")
@NoArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private int points;

    private LocalDate birthdate;
    private CustomerSex sex;
    private String freeTimeHabits;
    private String preferredMedias;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Challenge> challenges = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private User user;

    @Column
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Customer(int points, LocalDate birthdate, CustomerSex sex, String freeTimeHabits, String preferredMedias, User user) {
        this.points = points;
        this.birthdate = birthdate;
        this.sex = sex;
        this.freeTimeHabits = freeTimeHabits;
        this.preferredMedias = preferredMedias;
        this.user = user;
    }

    public Customer(LocalDate birthdate, CustomerSex sex, String freeTimeHabits, String preferredMedias) {
        this.birthdate = birthdate;
        this.sex = sex;
        this.freeTimeHabits = freeTimeHabits;
        this.preferredMedias = preferredMedias;
    }

    public Customer(User user) {
        this.user = user;
        this.points = 0;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public CustomerSex getSex() {
        return sex;
    }

    public void setSex(CustomerSex sex) {
        this.sex = sex;
    }

    public String getFreeTimeHabits() {
        return freeTimeHabits;
    }

    public void setFreeTimeHabits(String freeTimeHabits) {
        this.freeTimeHabits = freeTimeHabits;
    }

    public String getPreferredMedias() {
        return preferredMedias;
    }

    public void setPreferredMedias(String preferredMedias) {
        this.preferredMedias = preferredMedias;
    }

    public User getUser() {
        return user;
    }

    public List<Challenge> getChallenges() {
        return challenges;
    }

    public void setChallenges(List<Challenge> challenges) {
        this.challenges = challenges;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
