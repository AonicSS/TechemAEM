package com.techem.core.models;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.google.gson.Gson;
import com.techem.core.services.FriendlyCaptchaService;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = { "techem/components/form", "techem/components/form/friendly-captcha" })
public class FriendlyCaptcha {

    @Inject
    private FriendlyCaptchaService fCaptchaService;

    @ValueMapValue(name = "endpointEmail")
    private String endpointEmail;

    @ValueMapValue(name = "siteKey")
    private String siteKey;

    @ValueMapValue(name = "fcLanguage")
    private String fcLanguage;

    @ValueMapValue(name = "langInit")
    private String fcLangInit;
    
    @ValueMapValue(name = "langReady")
    private String fclangReady;

    @ValueMapValue(name = "langButtonReady")
    private String fcLangButtonReady;

    @ValueMapValue(name = "langFetching")
    private String fcLangFetching;

    @ValueMapValue(name = "langSolving")
    private String fcLangSolving;

    @ValueMapValue(name = "langCompleted")
    private String fcLangCompleted;

    @ValueMapValue(name = "langExpired")
    private String fcLangExpired;

    @ValueMapValue(name = "langExpiredButton")
    private String fcLangExpiredButton;

    @ValueMapValue(name = "langError")
    private String fcLangError;

    @ValueMapValue(name = "langErrorButton")
    private String fcLangErrorButton;

    @PostConstruct
    protected void init() {
        if(fCaptchaService != null) {
            siteKey = fCaptchaService.getConfig().apiSiteKey();
        }
    }

    public String getEndpointEmail() {
        return endpointEmail;
    }

    public String getSiteKey() {
        return siteKey;
    }

    public String getFcLanguage() {

        if(StringUtils.isNotBlank(fcLanguage) && fcLanguage.equals("custom")) {
            HashMap<String, String> langMap = new HashMap<>();
            langMap.put("text_init", fcLangInit);
            langMap.put("text_ready", fclangReady);
            langMap.put("button_start", fcLangButtonReady);
            langMap.put("text_fetching", fcLangFetching);
            langMap.put("text_solving", fcLangSolving);
            langMap.put("text_completed", fcLangCompleted);
            langMap.put("text_expired", fcLangExpired);
            langMap.put("button_restart", fcLangExpiredButton);
            langMap.put("text_error", fcLangError);
            langMap.put("button_retry", fcLangErrorButton);
            fcLanguage = new Gson().toJson(langMap);
        }

        return fcLanguage;
    }
}