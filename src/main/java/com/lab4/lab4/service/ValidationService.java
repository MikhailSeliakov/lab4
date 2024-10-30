package com.lab4.lab4.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import com.lab4.lab4.exception.ValidationFailedException;

@Service
public interface ValidationService {
    void isValid(BindingResult bindingResult) throws ValidationFailedException;
}
