package com.techem.core.models;

import org.apache.commons.compress.utils.Lists;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toCollection;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SocialMedia {

    @ValueMapValue(name = "categoryTitle")
    private String categoryTitle;

    @ChildResource(name = "socialItems")
    private Resource socialItems;

    private List<Media> socialMediaList;

    @PostConstruct
    protected void init() {
        if(Objects.nonNull(socialItems)) {
            socialMediaList = Lists.newArrayList(socialItems.getChildren().iterator()).
                    stream().map(socialItem -> socialItem.adaptTo(Media.class))
                    .collect(toCollection(LinkedList::new));
        }
    }

    public String getCategoryTitle() { return categoryTitle; }

    public List<Media> getSocialMediaList() {
        return socialMediaList;
    }
}
