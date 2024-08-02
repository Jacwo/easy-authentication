package org.apereo.cas.adaptors.rest;


import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import groovy.json.JsonOutput;
import org.apache.commons.lang3.StringUtils;
import org.apereo.cas.DefaultMessageDescriptor;
import org.apereo.cas.authentication.AuthenticationHandlerExecutionResult;
import org.apereo.cas.authentication.MessageDescriptor;
import org.apereo.cas.authentication.credential.UsernamePasswordCredential;
import org.apereo.cas.authentication.exceptions.AccountDisabledException;
import org.apereo.cas.authentication.exceptions.AccountPasswordMustChangeException;
import org.apereo.cas.authentication.handler.support.AbstractUsernamePasswordAuthenticationHandler;
import org.apereo.cas.authentication.principal.Principal;
import org.apereo.cas.authentication.principal.PrincipalFactory;
import org.apereo.cas.authentication.principal.Service;
import org.apereo.cas.authentication.principal.SimplePrincipal;
import org.apereo.cas.authentication.support.password.PasswordExpiringWarningMessageDescriptor;
import org.apereo.cas.configuration.model.support.rest.RestAuthenticationProperties;
import org.apereo.cas.services.RegisteredService;
import org.apereo.cas.services.RegisteredServiceProperty;
import org.apereo.cas.services.ServicesManager;
import org.apereo.cas.util.DateTimeUtils;
import org.apereo.cas.util.LoggingUtils;
import org.apereo.cas.util.http.HttpClient;
import org.apereo.cas.util.http.HttpExecutionRequest;
import org.apereo.cas.util.http.HttpUtils;
import org.apereo.cas.util.serialization.JacksonObjectMapperFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.io.IOUtils;
import org.apache.hc.core5.http.HttpEntityContainer;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.NameValuePair;
import org.hjson.JsonObject;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import javax.security.auth.login.AccountExpiredException;
import javax.security.auth.login.AccountLockedException;
import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.login.FailedLoginException;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

/**
 * This is {@link RestAuthenticationHandler} that authenticates uid/password against a remote
 * rest endpoint based on the status code received. Credentials are passed via basic authn.
 *
 * @author Misagh Moayyed
 * @since 5.0.0
 */
@Slf4j
public class RestAuthenticationHandler extends AbstractUsernamePasswordAuthenticationHandler {

    /**
     * Header name that explains the password expiration date.
     */
    public static final String HEADER_NAME_CAS_PASSWORD_EXPIRATION_DATE = "X-CAS-PasswordExpirationDate";

    /**
     * Header name that explains the warnings.
     */
    public static final String HEADER_NAME_CAS_WARNING = "X-CAS-Warning";

    private static final ObjectMapper MAPPER = JacksonObjectMapperFactory.builder().defaultTypingEnabled(true)
            .build().toObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(MapperFeature.AUTO_DETECT_CREATORS, true)
            .configure(MapperFeature.AUTO_DETECT_FIELDS, true);;

    private final RestAuthenticationProperties properties;

    private final HttpClient httpClient;

    public RestAuthenticationHandler(final ServicesManager servicesManager,
                                     final PrincipalFactory principalFactory,
                                     final RestAuthenticationProperties properties,
                                     final HttpClient httpClient) {
        super(properties.getName(), servicesManager, principalFactory, properties.getOrder());
        this.properties = properties;
        this.httpClient = httpClient;
    }

    @Override
    protected AuthenticationHandlerExecutionResult authenticateUsernamePasswordInternal(
        final UsernamePasswordCredential credential,
        final String originalPassword) throws Throwable {

        var response = (HttpResponse) null;
        try {
            String restUrl = "";
            Map<String, Object> customFields = credential.getCustomFields();
            Service service = (Service) customFields.get("service");
            if(service!=null){
                RegisteredService serviceBy = getServicesManager().findServiceBy(service);
                Map<String, RegisteredServiceProperty> properties = serviceBy.getProperties();
                if(properties!=null){
                    RegisteredServiceProperty restAuthUrl = properties.get("restAuthUrl");
                    if(restAuthUrl!=null){
                        Set<String> values = restAuthUrl.getValues();
                        if(values!=null){
                            restUrl =values.iterator().next();
                        }
                    }
                }
            }

            Map<String,Object> reqMap =new HashMap<>();
            reqMap.put("username", credential.getUsername());
            reqMap.put("password",new String(credential.getPassword()));
            Map<String, String> headMap = new HashMap<>();
            headMap.put(HttpHeaders.CONTENT_TYPE, "application/json");
            val exec = HttpExecutionRequest
                .builder().entity(JsonOutput.toJson(reqMap)).headers(headMap)
                    .method(HttpMethod.valueOf(properties.getMethod().toUpperCase(Locale.ENGLISH)))
                .url(StringUtils.isEmpty(restUrl) ? properties.getUri(): restUrl)
                .httpClient(httpClient)
                .build();
            response = HttpUtils.execute(exec);
            log.info("rest response {}",JSONObject.toJSONString(response));
            val content = ((HttpEntityContainer) response).getEntity().getContent();
            val result = IOUtils.toString(content, StandardCharsets.UTF_8);
            ResponseWrapper responseWrapper = JSONObject.parseObject(result, ResponseWrapper.class);
            int code = responseWrapper.getCode();
            if(HttpStatus.OK.value() == code){
                Object data = responseWrapper.getData();
                Map map = JSONObject.parseObject(JsonOutput.toJson(data), Map.class);

                Map<String, List<Object>> attributes =new TreeMap<>();
                attributes.put("id",Arrays.asList(map.get("userId")));
                attributes.put("userId",Arrays.asList(map.get("account")));
                attributes.put("tenantId",Arrays.asList(map.get("tenantId")));
                val principal = principalFactory.createPrincipal(credential.getUsername(), attributes);
                return createHandlerResult(credential, principal, getWarnings(response));
            }else{
                throw new FailedLoginException("Could not authenticate account for " + credential.getUsername());
            }
           /* val status = HttpStatus.resolve(Objects.requireNonNull(response).getCode());
            return switch (Objects.requireNonNull(status)) {
                case OK -> buildPrincipalFromResponse(credential, response);
                case FORBIDDEN -> throw new AccountDisabledException("Could not authenticate forbidden account for " + credential.getUsername());
                case UNAUTHORIZED -> throw new FailedLoginException("Could not authenticate account for " + credential.getUsername());
                case NOT_FOUND -> throw new AccountNotFoundException("Could not locate account for " + credential.getUsername());
                case LOCKED -> throw new AccountLockedException("Could not authenticate locked account for " + credential.getUsername());
                case PRECONDITION_FAILED -> throw new AccountExpiredException("Could not authenticate expired account for " + credential.getUsername());
                case PRECONDITION_REQUIRED -> throw new AccountPasswordMustChangeException("Account password must change for " + credential.getUsername());
                default -> throw new FailedLoginException("Rest endpoint returned an unknown status code " + status + " for " + credential.getUsername());
            };*/
        } finally {
            HttpUtils.close(response);
        }
    }

    protected AuthenticationHandlerExecutionResult buildPrincipalFromResponse(
        final UsernamePasswordCredential credential,
        final HttpResponse response) throws Throwable {
        try {
            try (val content = ((HttpEntityContainer) response).getEntity().getContent()) {
                val result = IOUtils.toString(content, StandardCharsets.UTF_8);
                log.debug("REST authentication response received: [{}]", result);

                val principalFromRest = MAPPER.readValue(result, Principal.class);
                val principal = principalFactory.createPrincipal(principalFromRest.getId(), principalFromRest.getAttributes());
                return createHandlerResult(credential, principal, getWarnings(response));
            }
        } catch (final Throwable e) {
            LoggingUtils.error(log, e);
            throw new FailedLoginException("Unable to detect the authentication principal for " + credential.getUsername());
        }
    }

    /**
     * Resolve {@link MessageDescriptor warnings} from the response.
     *
     * @param authenticationResponse The response sent by the REST authentication endpoint
     * @return The warnings for the created {@link AuthenticationHandlerExecutionResult}
     */
    protected List<MessageDescriptor> getWarnings(final HttpResponse authenticationResponse) {
        val messageDescriptors = new ArrayList<MessageDescriptor>(2);

        val passwordExpirationDate = authenticationResponse.getFirstHeader(HEADER_NAME_CAS_PASSWORD_EXPIRATION_DATE);
        if (passwordExpirationDate != null) {
            val days = Duration.between(Instant.now(Clock.systemUTC()), DateTimeUtils.convertToZonedDateTime(passwordExpirationDate.getValue())).toDays();
            messageDescriptors.add(new PasswordExpiringWarningMessageDescriptor(null, days));
        }

        val warnings = authenticationResponse.getHeaders(HEADER_NAME_CAS_WARNING);
        if (warnings != null) {
            Arrays.stream(warnings)
                .map(NameValuePair::getValue)
                .map(DefaultMessageDescriptor::new)
                .forEach(messageDescriptors::add);
        }

        return messageDescriptors;
    }
}




