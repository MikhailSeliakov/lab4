package com.lab4.lab4.controller;

import com.lab4.lab4.exception.ValidationFailedException;
import com.lab4.lab4.model.*;
import com.lab4.lab4.service.ModifyRequestService;
import com.lab4.lab4.service.ModifyResponseService;
import com.lab4.lab4.service.ValidationService;
import com.lab4.lab4.util.DateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;

@Slf4j
@RestController
public class MyController {

    private final ValidationService validationService;
    private final ModifyRequestService modifyRequestService;
    private final ModifyResponseService modifyResponseService;

    @Autowired
    public MyController(ValidationService validationService, ModifyRequestService modifyRequestService, @Qualifier("ModifySystemTimeResponseService") ModifyResponseService modifyResponseService) {
        this.validationService = validationService;
        this.modifyRequestService = modifyRequestService;
        this.modifyResponseService = modifyResponseService;

    }

    @PostMapping(value = "/feedback")
    public ResponseEntity<Response> feedback(@Valid @RequestBody Request request, BindingResult bindingResult) {

        log.info("request: {}", request);

        Response response = Response.builder()
                .uid(request.getUid())
                .operationUid(request.getOperationUid())
                .systemTime(DateTimeUtil.getCustomFormat().format(new Date()))
                .code(Codes.SUCCESS)
                .errorCode(ErrorCodes.EMPTY)
                .errorMessage(ErrorMessage.EMPTY)
                .build();
        try {
            validationService.isValid(bindingResult);
        } catch (ValidationFailedException e) {
            log.error("validation failed", e);
            response.setCode(Codes.SUCCESS);
            response.setErrorCode(ErrorCodes.VALIDATION_EXCEPTION);
            response.setErrorMessage(ErrorMessage.VALIDATION);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("exception", e);
            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.UNKNOWN_EXCEPTION);
            response.setErrorMessage(ErrorMessage.UNKNOWN);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        modifyResponseService.modify(response);
        modifyRequestService.modify(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
