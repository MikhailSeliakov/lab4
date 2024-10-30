package com.lab4.lab4.service;

import com.lab4.lab4.model.Response;
import org.springframework.stereotype.Service;

@Service
public interface ModifyResponseService {

    Response modify(Response response);

}
