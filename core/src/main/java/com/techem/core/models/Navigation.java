package com.techem.core.models;

import com.day.crx.JcrConstants;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
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

    protected static final String PAGE = "cq:Page";
    protected static final String HIDDEN_PROPERTY = "hideInNav";
    protected static final String RIGHT = "right";
    protected static final String LEFT = "left";

    @ValueMapValue(name = "navigationRoot")
    private String navigationRoot;

    @ValueMapValue(name = "buttonLink")
    private String buttonLink;

    @ValueMapValue(name = "label")
    private String label;

    @ValueMapValue(name = "logoLink")
    private String logoLink;

    @ValueMapValue(name = "backButtonText")
    private String backButtonText;

    @ValueMapValue(name = "fileReference")
    private String logoImage;

    @SlingObject
    private ResourceResolver resourceResolver;

    private Map<Header, Map<String, List<NavigationDetails>>> navigationItems;

    @PostConstruct
    protected void init() {
        if(StringUtils.isNotBlank(navigationRoot)) {
            final Resource resourceRoot = resourceResolver.getResource(navigationRoot);

            if(Objects.nonNull(resourceRoot)) {
                final List<Resource> children =  Lists.newArrayList(resourceRoot.getChildren().iterator());

                if(CollectionUtils.isNotEmpty(children)) {
                    final Map<Header, List<NavigationDetails>> items = getItems(children);

                    logger.info("All navigation item as map {}", items);

                    if(!items.isEmpty()) {
                        setNavigationItems(items);
                    }
                }
            }
        } else {
            logger.info("The navigationRoot is null or empty {}");
        }
    }

    public Map<Header, Map<String, List<NavigationDetails>>> getNavigationItems() {
        return navigationItems;
    }

    public String getLabel() { return label; }

    public String getButtonLink() { return buttonLink; }

    public String getLogoLink() { return logoLink; }

    public String getLogoImage() { return logoImage; }

    public String getBackButtonText() { return backButtonText; }

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
                resource -> resource.getChild(JcrConstants.JCR_CONTENT).adaptTo(Header.class),
                resourcePage -> Lists.newArrayList(resourcePage.getChildren().iterator()).
                        stream().filter(isPage()).
                        filter(isNotNavHidden()).map(page ->
                                            page.getChild(JcrConstants.JCR_CONTENT).adaptTo(NavigationDetails.class))
                        .collect(Collectors.toList()), (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }

    private Predicate<Resource> isPage() {
        return r -> r.getValueMap().get(JcrConstants.JCR_PRIMARYTYPE, String.class).equals(PAGE);
    }

    private Predicate<Resource> isNotNavHidden() {
        return r ->  Objects.isNull((r.getChild(JcrConstants.JCR_CONTENT).getValueMap().get(HIDDEN_PROPERTY, Boolean.class)));
    }
}