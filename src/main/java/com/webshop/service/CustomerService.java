package com.webshop.service;

import com.webshop.dto.CustomerDTO;
import com.webshop.model.Customer;
import com.webshop.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Optional<CustomerDTO> getCustomerById(Long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        return customer.map(this::convertToDTO);
    }

    private CustomerDTO convertToDTO(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(customer.getId());
        customerDTO.setFirstName(customer.getFirstName());
        customerDTO.setLastName(customer.getLastName());
        customerDTO.setEmail(customer.getEmail());
        customerDTO.setPhone(customer.getPhone());
        customerDTO.setAddress1(customer.getAddress1());
        customerDTO.setAddress2(customer.getAddress2());
        customerDTO.setCity(customer.getCity());
        customerDTO.setPostalCode(customer.getPostalCode());
        return customerDTO;
    }

    public Customer getCustomerByEmail(String email) {
        Customer customer = customerRepository.findByEmail(email);
        customer.setOrders(null);
        return customer;
    }

    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }
}
