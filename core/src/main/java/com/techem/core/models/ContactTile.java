package com.techem.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.inject.Inject;

@Model(adaptables = Resource.class, adapters = ContactTile.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,
        resourceType = "techem/components/contact-tile")
public class ContactTile {

    /**
     * The current resource.
     */
    @Inject
    private Resource resource;

    @ValueMapValue(name="headline")
    @Default(values = "Headline for Contact Tile")
    private String headline;

    @ValueMapValue(name="subline")
    private String subline;

    @ValueMapValue(name="phone")
    private String phone;

    @ValueMapValue(name="email")
    private String email;

    public String getHeadline() {
        return headline;
    }

    public String getSubline() {
        return subline;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }
}
