package com.cache.application.services.customerService.impl;

import com.cache.application.entities.Customer;
import com.cache.application.repositories.CustomerRepository;
import com.cache.application.services.customerService.CustomerApplicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Diluni on 25/08/20.
 */
@Service
public class CustomerApplicationServiceImpl implements CustomerApplicationService {
    private static final Logger logger = LoggerFactory.getLogger(CustomerApplicationServiceImpl.class);

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public Map<Long, Optional<Customer>> getCustomerByCustomerId(Long customerId) {
        logger.info("Enter into getCustomerByCustomerId method in Customer Application Service Impl. CustomerId: " + customerId.toString());
        Map<Long, Optional<Customer>> customersMap = new HashMap<>();
        Optional<Customer> customer = customerRepository.findById(customerId);
        customersMap.put(customerId, customer);

        logger.info("Return from getCustomerByCustomerId: " + customersMap.toString());
        return customersMap;
    }

    @Override
    public HttpStatus addCustomer(Customer customer) {
        customerRepository.save(customer);
        return HttpStatus.OK;
    }

}
