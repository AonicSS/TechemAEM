package com.techem.core.models;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class NewshubContactItem {

    @Inject
    @Named("downloadItems")
    private List<NewshubContactDownloadItem> downloadItems;
    
    @ValueMapValue(name = "contactName")
    private String contactName;

    @ValueMapValue(name = "contactDescription")
    private String contactDescription;

    @ValueMapValue(name = "contactImageReference")
    private String contactImageReference;


    public String getContactName() {
        return contactName;
    }

    public String getContactDescription() {
        return contactDescription;
    }

    public String getContactImageReference() {
        return contactImageReference;
    }

    public List<NewshubContactDownloadItem> getDownloadItems() {
        return downloadItems;
    }
}