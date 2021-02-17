package com.techem.core.models;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Model(adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Footer {

    private Logger logger = LoggerFactory.getLogger(Footer.class);

    private static final String FOOTER_ITEMS = "footerItems";

    @ChildResource(name = "categories")
    private Resource categories;

    @ChildResource(name = "bottomLinks")
    private Resource bottomLinks;

    @ValueMapValue(name = "copyright")
    private String copyright;

    @ValueMapValue(name = "logoLink")
    private String logoLink;

    private Map<FooterCategory, List<FooterLink>> footerItems;

    private LinkedList<FooterLink> bottomItems;

    @PostConstruct
    protected void init() {
        setFooterItems();
        setBottomLinks();
    }

    private void setBottomLinks() {
        if(Objects.nonNull(bottomLinks)) {
            final List<Resource> children =  Lists.newArrayList(bottomLinks.getChildren().iterator());

            if(CollectionUtils.isNotEmpty(children)) {
                bottomItems = children.stream().map(item -> item.adaptTo(FooterLink.class)).
                              collect(Collectors.toCollection(LinkedList::new));
            } else {
                logger.info("The bottomLinks is null or empty {}");
            }
        }
    }

    private void setFooterItems() {
        if(Objects.nonNull(categories)) {
           final List<Resource> children =  Lists.newArrayList(categories.getChildren().iterator());

           if(CollectionUtils.isNotEmpty(children)) {
               footerItems = children.stream().collect(Collectors.toMap(
                item -> item.adaptTo(FooterCategory.class),
                categoryItem ->   Objects.isNull(categoryItem.getChild(FOOTER_ITEMS))
                                  ? Collections.emptyList()
                                  : Lists.newArrayList(categoryItem.getChild(FOOTER_ITEMS).
                                          getChildren().iterator()).stream().map(footerItems ->
                                          footerItems.adaptTo(FooterLink.class)).collect(Collectors.toList()),
                                          (oldValue, newValue) -> oldValue, LinkedHashMap::new));
           }
        } else {
            logger.info("The categories is null or empty {}");
        }
    }

    public Map<FooterCategory, List<FooterLink>> getFooterItems() {
        return footerItems;
    }

    public List<FooterLink> getBottomItems() {
        return bottomItems;
    }

    public String getCopyright() {
        return copyright;
    }

    public String getLogoLink() {return  logoLink;}
}
