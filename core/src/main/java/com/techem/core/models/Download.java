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

    @ValueMapValue(name="size")
    private String size;

    @ValueMapValue(name="linkURL2")
    private String linkURL2;

    @ValueMapValue(name="fileReference2")
    private String fileReference2;

    public String getText() {
        return text;
    }

    public String getIconFormatFormat() {
        return iconFormat;
    }

    public String getSize() {
        return size;
    }

    public String getLinkURL2() {
        return linkURL2;
    }

    public String getFileReference2() {
        return fileReference2;
    }


}
