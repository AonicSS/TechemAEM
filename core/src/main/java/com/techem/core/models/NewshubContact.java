package com.techem.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

@Model(adaptables = Resource.class, adapters = NewshubContact.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,
        resourceType = "techem/components/newshub-contact")
public class NewshubContact {

    @Inject
    @Named("contactItems")
    private List<NewshubContactItem> contacts;

    @ValueMapValue(name="headline")
    private String headline;
    
    @ValueMapValue(name="backgroundColor")
    private String backgroundColor;

    private Boolean isVariation = false;

    @PostConstruct
    protected void init() {
        if(contacts != null) {
            if(contacts.size() > 2) {
                isVariation = false;
            } else {
                isVariation = true;
            }
        }
    }

    public List<NewshubContactItem> getContacts() {
        return contacts;
    }

    public String getHeadline() {
        return headline;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public Boolean getIsVariation() {
        return isVariation;
    }
}