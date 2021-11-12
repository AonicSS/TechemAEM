package com.techem.core.models;

import com.google.common.base.Splitter;
import com.techem.core.selectors.YextCredentialsImpl;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class YextCredentialsHelper {

    @PostConstruct
    protected void init() {
        this.credentials = this.yextCredentials.getCredentials();
        this.url = this.yextCredentials.getRedirectURL();
    }

    @Inject
    private YextCredentialsImpl yextCredentials;

    private String credentials;
    private String url;

    public Map<String, String> getCredentials() {
        String tempResult = this.credentials.replaceAll("\\{|\\}|\"", "");
        Map<String,String> map = Splitter.on(",").withKeyValueSeparator(":").split(tempResult);
        return map;
    }

    public String getRedirectURL() {
        return this.url;
    }
}