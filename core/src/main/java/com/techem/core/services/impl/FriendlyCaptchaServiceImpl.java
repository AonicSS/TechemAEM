package com.techem.core.services.impl;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.techem.core.services.FriendlyCaptchaService;
import com.techem.core.services.FriendlyCaptchaService.FriendlyCaptchaConfig;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

@Component(service = FriendlyCaptchaService.class, immediate = true)
@Designate(ocd = FriendlyCaptchaConfig.class)
public class FriendlyCaptchaServiceImpl implements FriendlyCaptchaService {

    private FriendlyCaptchaConfig fCaptchaConfig;
    private Logger log = LoggerFactory.getLogger(this.getClass());
    
	@Activate
    @Modified
	protected void activate(FriendlyCaptchaConfig fConfig) {
		fCaptchaConfig = fConfig;
	}

    @Override
    public FriendlyCaptchaConfig getConfig() {
        return fCaptchaConfig;
    }

    @Override
    public boolean validateCaptcha(String solutionStr) {
        String apiEndpoint = fCaptchaConfig.apiEndpoint();
        String siteKey = fCaptchaConfig.apiSiteKey();
        String secretKey = fCaptchaConfig.apiSecret();

        if(StringUtils.isBlank(apiEndpoint) || StringUtils.isBlank(siteKey) || StringUtils.isBlank(secretKey)) {
            log.error("API Misconfigured. API URL: {}, Secret: {}, SiteKey: {}", apiEndpoint, siteKey, secretKey);
            return false;
        }

        if(StringUtils.isBlank(solutionStr)) { 
            log.error("Solution empty or null: {}", solutionStr);
            return false;
        }

        try(CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(apiEndpoint);
            Map<String, String> postParams = new HashMap<>();
            postParams.put(FCC_SOLUTION, solutionStr);
            postParams.put(FCC_SECRET, secretKey);
            postParams.put(FCC_SITEKEY, siteKey);

            Gson jGson = new Gson();
            String jsonReq = jGson.toJson(postParams);
            StringEntity requestEntity = new StringEntity(jsonReq, ContentType.APPLICATION_JSON);
            httpPost.setEntity(requestEntity);

            HttpResponse postResp = httpClient.execute(httpPost);
            StatusLine respStatus = postResp.getStatusLine();
            HttpEntity respEntity = postResp.getEntity();

            if(respStatus == null || respEntity == null) {
                log.error("Could not get response of request, status was null.");
                return false;
            }

            int respCode = respStatus.getStatusCode();
            String respBody = EntityUtils.toString(respEntity, "UTF-8");

            if(StringUtils.isNotBlank(respBody)) {
                JsonObject jsonResp = jGson.fromJson(respBody, JsonElement.class).getAsJsonObject();
                boolean isSuccess = jsonResp.get(FCC_SUCCESS) != null ? jsonResp.get(FCC_SUCCESS).getAsBoolean() : false;
                
                if(respCode != 200) {
                    log.error("API request failed with status {}. Check your configuration! Will validate anyways as per https://docs.friendlycaptcha.com/#/verification_api?id=verification-best-practices", respCode);
                    return true;
                }
                
                if(!isSuccess) {
                    JsonArray respError = jsonResp.get(FCC_ERRORS).getAsJsonArray();
                    log.error("Validation failed. Success: {}, Errors: {}.", isSuccess, respError);
                }

                return isSuccess;
            }
        } catch (Exception e) {
            log.error("Exceptions occured during request. Exception follows: ", e);
        }

        return false;
    }

    @Override
    public boolean validateCaptchaToken(String soluString, String captchaToken) {

        if(StringUtils.isBlank(soluString) || StringUtils.isBlank(captchaToken)) { return false; }
        
        if(!captchaToken.contains(fCaptchaConfig.apiSiteKey())) { return false; }

        if(!captchaToken.contains(soluString)) { return false; }

        String solutTkn = captchaToken.substring(captchaToken.indexOf(soluString), captchaToken.length());
        String siteKeyTkn = captchaToken.substring(0, captchaToken.indexOf(solutTkn));

        if(solutTkn.equals(soluString) && siteKeyTkn.equals(fCaptchaConfig.apiSiteKey())) { return true; }

        return false;
    }
}