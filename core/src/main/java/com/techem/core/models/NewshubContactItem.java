package com.techem.core.models;

import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import java.util.List;

@Getter
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class NewshubContactItem {

    @ChildResource
    private List<NewshubContactDownloadItem> downloadItems;
    
    @ValueMapValue
    private String contactName;

    @ValueMapValue
    private String contactDescription;

    @ValueMapValue
    private String contactImageReference;

}