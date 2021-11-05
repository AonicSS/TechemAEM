package com.techem.core.models;

import com.techem.core.selectors.TrengoChat;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class TrengoChatHelper {

    @PostConstruct
    protected void init() {
        this.apiKey = this.trengoChat.getAPIKey();
    }

    @Inject
    private TrengoChat trengoChat;

    private String apiKey;

    public String getApiKey() {
        return this.apiKey;
    }
}