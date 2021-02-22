package com.techem.core.models;

import com.techem.core.selectors.EnvironmentSelector;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class EnvironmentHelper {

    @PostConstruct
    protected void init() {
        this.env = this.environmentSelector.getEnvironment();
    }

    @Inject
    private EnvironmentSelector environmentSelector;

    private String env;

    public String getEnv() {
        return this.env;
    }
}