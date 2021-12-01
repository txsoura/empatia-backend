package br.com.empatia.app.controllers;


import br.com.empatia.app.resources.CustomerResource;
import br.com.empatia.app.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("v1/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<CustomerResource>> index() {
        var customers = customerService.index();
        return ResponseEntity.ok(CustomerResource.collection(customers));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResource> show(@PathVariable UUID id) {
        var customer = customerService.show(id);
        return ResponseEntity.ok(new CustomerResource(customer));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> destroy(@PathVariable UUID id) {
        customerService.destroy(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
