package org.apereo.cas.web.custom;


import jakarta.annotation.PostConstruct;
import org.apereo.cas.web.custom.common.ResponseWrapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/auth")
public class CasConfigController {

    @PostConstruct
    public void init(){
        System.out.println("ssssss");
    }
    @GetMapping("/test")
    @ResponseBody
    public ResponseWrapper<Boolean> initialize() {
        return new ResponseWrapper<>(Boolean.valueOf(true));
    }
}

