package com.cache.application.services.customerService;

import com.cache.application.entities.Customer;
import org.springframework.http.HttpStatus;

import java.util.Map;
import java.util.Optional;

/**
 * Created by Diluni on 25/08/20.
 */
public interface CustomerApplicationService {

    Map<Long, Optional<Customer>> getCustomerByCustomerId(Long customerId);

    HttpStatus addCustomer(Customer customer);

}
