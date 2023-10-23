package com.levopravoce.backend.resources.error;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;

@Controller
public class ErrorResource extends BasicErrorController {
    public ErrorResource(ErrorAttributes errorAttributes) {
        super(errorAttributes, new ErrorProperties());
    }

    @Override
    protected ErrorAttributeOptions getErrorAttributeOptions(HttpServletRequest request, MediaType mediaType) {
        ErrorAttributeOptions options = ErrorAttributeOptions.defaults();
        options = options.including(ErrorAttributeOptions.Include.EXCEPTION);
        options = options.including(ErrorAttributeOptions.Include.STACK_TRACE);
        options = options.including(ErrorAttributeOptions.Include.MESSAGE);
        options = options.including(ErrorAttributeOptions.Include.BINDING_ERRORS);
        return options;
    }
}
