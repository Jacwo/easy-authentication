package org.apereo.cas.authentication.bypass;

import jakarta.servlet.http.HttpServletRequest;
import org.apereo.cas.authentication.Authentication;
import org.apereo.cas.authentication.MultifactorAuthenticationProvider;
import org.apereo.cas.services.RegisteredService;

import java.io.Serial;

/**
 * Multifactor Bypass Provider based on Service Multifactor Policy.
 *
 * @author Travis Schmidt
 * @since 6.0
 */
public class RegisteredServiceMultifactorAuthenticationProviderBypassEvaluator extends BaseMultifactorAuthenticationProviderBypassEvaluator {
    @Serial
    private static final long serialVersionUID = -3553888418344342672L;

    public RegisteredServiceMultifactorAuthenticationProviderBypassEvaluator(final String providerId) {
        super(providerId);
    }

    @Override
    public boolean shouldMultifactorAuthenticationProviderExecuteInternal(final Authentication authentication,
                                                                          final RegisteredService registeredService,
                                                                          final MultifactorAuthenticationProvider provider,
                                                                          final HttpServletRequest request) {
        if(registeredService == null || registeredService.getMultifactorAuthenticationPolicy() == null){
            return false;
        }else{
            return !registeredService.getMultifactorAuthenticationPolicy().isBypassEnabled();
        }

    }
}
