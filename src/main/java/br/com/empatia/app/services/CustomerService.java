package br.com.empatia.app.services;

import br.com.empatia.app.exceptions.ResourceNotFoundException;
import br.com.empatia.app.models.Customer;
import br.com.empatia.app.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }


    public List<Customer> index() {
        return customerRepository.findAll();
    }


    public Customer store(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer show(UUID id) {
        return customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
    }

    public void destroy(UUID id) {
        this.show(id);

        customerRepository.deleteById(id);
    }
}
