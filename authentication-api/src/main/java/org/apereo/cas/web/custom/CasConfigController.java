package org.apereo.cas.web.custom;


import org.apereo.cas.web.custom.common.BaseController;
import org.apereo.cas.web.custom.common.ResponseWrapper;
import org.apereo.cas.web.custom.dto.PasswordPolicyPatternDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/password")
public class CasConfigController  extends BaseController{
    @Value("${cas.authn.pm.core.password-policy-pattern}")
    private String passwordPolicyPattern;
    @Value("${password.policy.desc}")
    private String passwordPolicyDesc;
    @GetMapping("/test")
    public  ResponseWrapper<String> test() {
        return new ResponseWrapper<>(passwordPolicyPattern);
    }
    @GetMapping(value = "/protected/policy/pattern" , produces = "application/json")
    @ResponseBody
    public ResponseWrapper<PasswordPolicyPatternDto> getPasswordPolicyPattern() {
        PasswordPolicyPatternDto passwordPolicyPatternDto =new PasswordPolicyPatternDto();
        passwordPolicyPatternDto.setPattern(passwordPolicyPattern);
        passwordPolicyPatternDto.setPatternDesc(passwordPolicyDesc);
        return new ResponseWrapper<>(passwordPolicyPatternDto);
    }
}

