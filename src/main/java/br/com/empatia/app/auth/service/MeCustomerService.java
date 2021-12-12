package br.com.empatia.app.auth.service;

import br.com.empatia.app.exceptions.ResourceNotFoundException;
import br.com.empatia.app.models.Customer;
import br.com.empatia.app.models.User;
import br.com.empatia.app.repositories.CustomerRepository;
import br.com.empatia.app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MeCustomerService {
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public MeCustomerService(UserRepository userRepository, CustomerRepository customerRepository) {
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
    }

    public User getUser(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public Customer me(UUID id) {
        User user = this.getUser(id);

        return customerRepository.findByUserId(user.getId()).orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
    }

    public Customer update(UUID id, Customer data) {
        Customer customer = this.me(id);

        customer.setBirthdate(data.getBirthdate());
        customer.setSex(data.getSex());
        customer.setFreeTimeHabits(data.getFreeTimeHabits());
        customer.setFreeTime(data.getFreeTime());
        customer.setPreferredMedias(data.getPreferredMedias());

        return customerRepository.save(customer);
    }
}
