package com.lab4.lab4.service;

import com.lab4.lab4.model.Request;
import org.springframework.stereotype.Service;

@Service
public interface ModifyRequestService {
    void modify(Request request);
}
