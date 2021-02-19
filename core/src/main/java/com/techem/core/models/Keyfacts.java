package com.techem.core.models;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toCollection;

@Model(adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Keyfacts {

    @ChildResource(name = "keyfactsItems")
    private Resource keyfactsItems;

    @ValueMapValue(name = "headline")
    private String headline;

    @ValueMapValue(name = "text")
    private String text;

    private List<KeyfactItem> keyfactItemList = Collections.emptyList();

    @PostConstruct
    protected void init() {
         if(Objects.nonNull(keyfactsItems)) {
             final List<Resource> keyfactsList = Lists.newArrayList(keyfactsItems.getChildren().iterator());

             if (CollectionUtils.isNotEmpty(keyfactsList)) {
                 keyfactItemList = keyfactsList.stream().map(item -> item.adaptTo(KeyfactItem.class))
                         .collect(toCollection(LinkedList::new));
             }
         }
    }

    public String getHeadline() {
        return headline;
    }

    public String getText() {
        return text;
    }

    public List<KeyfactItem> getKeyfactItemList() {
        return keyfactItemList;
    }
}
