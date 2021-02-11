package com.techem.core.models;

import com.drew.lang.annotations.NotNull;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.inject.Inject;

@Model(adaptables = Resource.class, adapters = Download.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,
        resourceType = "techem/components/download")
public class Download {

    /**
     * The current resource.
     */
    @Inject
    private Resource resource;

    @ValueMapValue(name="text")
    @Default(values = "Title of the file")
    private String text;

    @ValueMapValue(name="iconFormat")
    private String iconFormat;

    public String getText() {
        return text;
    }

    public String getIconFormat() {
        return iconFormat;
    }
}
