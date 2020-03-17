package com.kelab.problemcenter.aop;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ModelAndView jsonErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        ModelAndView result = new ModelAndView(new MappingJackson2JsonView());
        result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        String date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
        result.addObject("date", date);
        result.addObject("error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        result.addObject("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        result.addObject("message", e.getMessage());
        return result;
    }

}