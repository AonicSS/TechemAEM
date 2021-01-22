package com.techem.core.models;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Navigation {

    private Logger logger = LoggerFactory.getLogger(Navigation.class);

    private static final String PRIMARY_TYPE = "jcr:primaryType";
    private static final String PAGE = "cq:Page";
    private static final String HIDDEN_PROPERTY = "hideInNav";
    private static final String CONTENT = "jcr:content";
    private static final String RIGHT = "right";
    private static final String LEFT = "left";

    @ValueMapValue(name = "navigationRoot")
    private String navigationRoot;

    @ValueMapValue(name = "buttonLink")
    private String button_link;

    @ValueMapValue(name = "label")
    private String label;

    @ValueMapValue(name = "logoLink")
    private String logoLink;

    @ChildResource(name = "image")
    private Resource image;

    @SlingObject
    private ResourceResolver resourceResolver;

    private Map<Header, Map<String, List<NavigationDetails>>> navigationItems;

    private String logoImage;

    @PostConstruct
    protected void init() {
        if(StringUtils.isNotBlank(navigationRoot)) {
            final Resource resourceRoot = resourceResolver.getResource(navigationRoot);

            if(Objects.nonNull(resourceRoot)) {
                final List<Resource> children =  Lists.newArrayList(resourceRoot.getChildren().iterator());

                if(Objects.nonNull(children) && children.size() > 0) {
                    final Map<Header, List<NavigationDetails>> items = getItems(children);

                    logger.info("All navigation item as map {}", items);

                    if(Objects.nonNull(items) && items.size() > 0) {
                        setNavigationItems(items);
                    }
                }
            }
        } else {
            logger.info("The navigationRoot is null or empty {}");
        }

        setLogoImage();
    }

    public Map<Header, Map<String, List<NavigationDetails>>> getNavigationItems() {
        return navigationItems;
    }

    public String getLabel() { return label; }

    public String getButton_link() { return button_link; }

    public String getLogoLink() { return logoLink; }

    public String getLogoImage() { return logoImage; }

    private void setLogoImage() {
        if(Objects.nonNull(image)) {
            ValueMap valueMap = image.adaptTo(ValueMap.class);
            logoImage = valueMap.get("fileReference", String.class);
        } else {
            logger.info("The logoImage is null or empty {}");
        }
    }

    private void setNavigationItems(Map<Header,List<NavigationDetails>> items) {
        navigationItems = new LinkedHashMap<>();

        items.entrySet().stream().forEachOrdered(item -> {
            Map<String, List<NavigationDetails>>  map= new LinkedHashMap<>();
            map.put(LEFT, item.getValue());
            map.put(RIGHT, item.getValue());
            navigationItems.put(item.getKey(),map);
        });
    }

    private Map<Header, List<NavigationDetails>> getItems(List<Resource> children) {
        return children.stream().filter(isPage()).filter(isNotNavHidden()).collect(Collectors.toMap(
                resource -> resource.getChild(CONTENT).adaptTo(Header.class),
                resourcePage -> Lists.newArrayList(resourcePage.getChildren().iterator()).
                        stream().filter(isPage()).filter(isNotNavHidden()).map(page -> page.getChild(CONTENT).adaptTo(NavigationDetails.class))
                        .collect(Collectors.toList()), (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }

    private Predicate<Resource> isPage() {
        return r -> r.getValueMap().get(PRIMARY_TYPE, String.class).equals(PAGE);
    }

    private Predicate<Resource> isNotNavHidden() {
        return r ->  Objects.isNull((r.getChild(CONTENT).getValueMap().get(HIDDEN_PROPERTY, Boolean.class)));
    }
}