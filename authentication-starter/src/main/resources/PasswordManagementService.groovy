import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.ContentType
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClients
import org.apereo.cas.authentication.Credential
import org.apereo.cas.pm.*

import java.nio.charset.StandardCharsets

def change(Object[] args) {


    def passwordChangeBean = args[0] as PasswordChangeRequest
    def logger = args[1]
    def username = passwordChangeBean.username
    def newPassword = passwordChangeBean.toConfirmedPassword()
    def httpClient = HttpClients.createDefault()
    //def httpPost = new HttpPost("http://172.17.8.227:8990/deploy/user/password/update")
    def httpPost = new HttpPost("http://rg-anka-deploy.ruijie-sourceid.svc/deploy/user/password/update")
    httpPost.addHeader("Content-Type","application/json")
    def json = new ObjectMapper().writeValueAsString([username: username, password: newPassword])
    def stringEntity = new StringEntity(json, ContentType.create(ContentType.APPLICATION_JSON.getMimeType(), StandardCharsets.UTF_8))
    httpPost.setEntity(stringEntity)

    def response = httpClient.execute(httpPost) as CloseableHttpResponse
    try {
        if (response.getStatusLine().getStatusCode() == 200) {
            logger.info("Password change request succeeded. Response status: ${response.getStatusLine().getStatusCode()}")
            responseBody = response.getEntity().getContent().getText()
            logger.info("Response body: ${responseBody}")

            if(responseBody.contains("412") || responseBody.contains("300")){
                return false;
            }
            return true
        } else {
            logger.info("Password change request failed. Response status: ${response.getStatusLine().getStatusCode()}")
            responseBody = response.getEntity().getContent().getText()
            logger.info("Response body: ${responseBody}")
            // Handle failed response...
            return false
        }
    } finally {
        response.close()
    }

}

def findEmail(Object[] args) {
    def username = (args[0] as PasswordManagementQuery).username
    def logger = args[1]
    if (username.equals("none")) {
        return null
    }
    return "cas@example.org"
}

def getSecurityQuestions(Object[] args) {
    def username = (args[0] as PasswordManagementQuery).username
    def logger = args[1]
    if (username.equals("noquestions")) {
        return [:]
    }
    return [securityQuestion1: "securityAnswer1"]
}

def findPhone(Object[] args) {
    def username = (args[0] as PasswordManagementQuery).username
    def logger = args[1]
    if (username.equals("none")) {
        return null
    }
    return "3477463421"
}

def findUsername(Object[] args) {
    def email = (args[0] as PasswordManagementQuery).email
    def logger = args[1]
    if (email.contains("@baddomain")) {
        return null
    }
    return "casuser"
}

def updateSecurityQuestions(Object[] args) {
    def query = args[0] as PasswordManagementQuery
    def logger = args[1]
    // Execute update...
}

def unlockAccount(Object[] args) {
    def query = args[0] as Credential
    def logger = args[1]
    // Execute unlock...
    return true
}
