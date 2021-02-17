package com.techem.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;

@Model(adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class KeyfactItem {

    @ValueMapValue(name = "number")
    private String number;

    @ValueMapValue(name = "unit")
    private String unit;

    @ValueMapValue(name = "description")
    private String description;

    @PostConstruct
    protected void init() {
    }

    public String getNumber() {
        return number;
    }

    public String getUnit() {
        return unit;
    }

    public String getDescription() {
        return description;
    }
}
