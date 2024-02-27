package ca.mcgill.ecse321.SportCenterManager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.mcgill.ecse321.SportCenterManager.dao.CustomerAccountRepository;
import ca.mcgill.ecse321.SportCenterManager.dao.InstructorAccountRepository;

@Service
public class CustomerAccountController {
    @Autowired
    private CustomerAccountRepository customerRepo;
    @Autowired
    private InstructorAccountRepository instructorRepo;
    
    
}
