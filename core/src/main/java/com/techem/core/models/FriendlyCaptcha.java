package com.techem.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = { "techem/components/form-container-frc" })
public class FriendlyCaptcha {

    @ValueMapValue(name = "endpointEmail")
    private String endpointEmail;
    
    @PostConstruct
    protected void init() { }

    public String getEndpointEmail() {
        return endpointEmail;
    }
}