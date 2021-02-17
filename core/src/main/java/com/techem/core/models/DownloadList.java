package com.techem.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.inject.Inject;


@Model(adaptables = Resource.class, adapters = DownloadList.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,
        resourceType = "techem/components/download-list")
public class DownloadList {

    /**
     * The current resource.
     */
    @Inject
    private Resource resource;

    @ValueMapValue(name="headline")
    @Default(values = "Download List Headline")
    private String headline;

    @ValueMapValue(name="description")
    private String description;

    public String getHeadline() {
        return headline;
    }

    public String getDescription() {
        return description;
    }
}
