package com.techem.core.models;


import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.inject.Inject;

@Model(adaptables = Resource.class, adapters = IconNumberTile.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,
        resourceType = "techem/components/icon-number-tile")
public class IconNumberTile {

    /**
     * The current resource.
     */
    @Inject
    private Resource resource;

    @ValueMapValue(name="number")
    private String number;

    @ValueMapValue(name="title")
    @Default(values = "Title of Icon-Number Tile")
    private String title;

    @ValueMapValue(name="body")
    private String body;

    public String getNumber() {
        return number;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }
}