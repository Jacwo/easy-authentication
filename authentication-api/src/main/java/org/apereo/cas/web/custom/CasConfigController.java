package org.apereo.cas.web.custom;


import jakarta.annotation.PostConstruct;
import org.apereo.cas.web.custom.common.BaseController;
import org.apereo.cas.web.custom.common.ResponseWrapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/password")
public class CasConfigController  extends BaseController{
    @Value("${cas.authn.pm.core.password-policy-pattern}")
    private String passwordPolicyPattern;

    @GetMapping("/test")
    @ResponseBody
    public String test() {
        return "ok";
    }
    @GetMapping("/protected/policy/pattern")
    @ResponseBody
    public ResponseWrapper<Boolean> getPasswordPolicyPattern() {
        return new ResponseWrapper<>(Boolean.valueOf(true));
    }
}

