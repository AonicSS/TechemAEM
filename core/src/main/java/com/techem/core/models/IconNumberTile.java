package com.techem.core.models;


import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

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

    @Inject
    @Default(values = "1")
    private String number;

    @Inject
    @Default(values = "Headline")
    private String title;

    @Inject
    @Default(values = "left")
    private String body;

    public String getNumber() {
        if (this.number == null) {
            this.number = this.resource.getValueMap().get("number", String.class);
        }
        return number;
    }

    public String getTitle() {
        if (this.title == null) {
            this.title = this.resource.getValueMap().get("title", String.class);
        }
        return title;
    }

    public String getBody() {
        if (this.body == null) {
            this.body = this.resource.getValueMap().get("body", String.class);
        }
        return body;
    }
}