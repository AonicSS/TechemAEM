package com.techem.core.models;

import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import java.util.List;

@Getter
@Model(adaptables = Resource.class, adapters = NewshubContact.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,
        resourceType = "techem/components/newshub-contact")
public class NewshubContact {

    @ChildResource(name = "contactItems")
    private List<NewshubContactItem> contacts;

    @ValueMapValue
    private String headline;

    @ValueMapValue
    private String backgroundColor;

    private Boolean isVariation = Boolean.FALSE;

    @PostConstruct
    protected void init() {

        if (contacts != null) {
            if (contacts.size() > 2) {
                isVariation = false;
            } else {
                isVariation = true;
            }
        }
    }
}