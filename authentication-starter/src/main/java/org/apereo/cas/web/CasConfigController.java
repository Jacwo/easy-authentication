
package org.apereo.cas.web;

import org.apereo.cas.common.BaseController;
import org.apereo.cas.common.ResponseWrapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class CasConfigController extends BaseController {
    @GetMapping(path = "test")
    public ResponseWrapper<Boolean> initialize() {
        return new ResponseWrapper<>(Boolean.valueOf(true));
    }
}

