package br.com.empatia.app.resources;

import br.com.empatia.app.enums.CustomerSex;
import br.com.empatia.app.models.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResource {
    private UUID id;
    private LocalDate birthdate;
    private CustomerSex sex;
    private UUID userId;
    private String freeTimeHabits;
    private int points;
    private String preferredMedias;
    private int challenges;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CustomerResource(Customer customer) {
        this.id = customer.getId();
        this.birthdate = customer.getBirthdate();
        this.sex = customer.getSex();
        this.userId = customer.getUser().getId();
        this.freeTimeHabits = customer.getFreeTimeHabits();
        this.points = customer.getPoints();
        this.preferredMedias = customer.getPreferredMedias();
        this.challenges = customer.getChallenges().size();
        this.createdAt = customer.getCreatedAt();
        this.updatedAt = customer.getUpdatedAt();
    }

    public static List<CustomerResource> collection(List<Customer> customers) {
        return customers.stream().map(CustomerResource::new).collect(Collectors.toList());
    }
}
