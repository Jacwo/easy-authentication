package org.apereo.cas.web.custom.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;
import org.apereo.cas.web.custom.common.BaseDto;

@Getter
@Setter
public class PasswordPolicyPatternDto extends BaseDto {
    private String pattern;
    private String patternDesc;
}
