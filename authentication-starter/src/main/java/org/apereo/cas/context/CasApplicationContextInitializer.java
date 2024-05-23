package org.apereo.cas.context;

import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apereo.cas.configuration.CasConfigurationPropertiesValidator;
import org.apereo.cas.configuration.api.CasConfigurationPropertiesSourceLocator;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;

/**
 * This is {@link CasApplicationContextInitializer}.
 *
 * @author Misagh Moayyed
 * @since 6.5.0
 */
@Slf4j
public class CasApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    /**
     * System property to indicate whether configuration status has passed validation.
     */
    public static final String SYSTEM_PROPERTY_CONFIG_VALIDATION_STATUS = "CONFIG_VALIDATION_STATUS";

    @Override
    public void initialize(@Nonnull final ConfigurableApplicationContext applicationContext) {
        val activeProfiles = List.of(applicationContext.getEnvironment().getActiveProfiles());
        if (!activeProfiles.contains(CasConfigurationPropertiesSourceLocator.PROFILE_NATIVE)) {
            log.debug("Initializing application context [{}] for active profiles [{}]",
                applicationContext.getDisplayName(), activeProfiles);
            val validator = new CasConfigurationPropertiesValidator(applicationContext);
            val results = validator.validate();
            System.setProperty(SYSTEM_PROPERTY_CONFIG_VALIDATION_STATUS, Boolean.toString(results.isEmpty()));
        }
    }
}
