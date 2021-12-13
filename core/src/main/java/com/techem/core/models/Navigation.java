package com.techem.core.models;

import com.day.crx.JcrConstants;
import com.google.common.collect.ImmutableList;
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
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.day.cq.wcm.api.NameConstants.NT_PAGE;
import static java.util.stream.Collectors.toCollection;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Navigation {

    private Logger logger = LoggerFactory.getLogger(Navigation.class);

    protected static final String HIDDEN_PROPERTY = "hideInNav";
    protected static final String RIGHT = "right";
    protected static final String LEFT = "left";

    @ValueMapValue(name = "navigationRoot")
    private String navigationRoot;

    @ValueMapValue(name = "buttonLink")
    private String buttonLink;

    @ValueMapValue(name = "portalBtnLabel")
    private String portalBtnLabel;

    @ValueMapValue(name = "portalItemLabel")
    private String portalItemLabel;

    @ValueMapValue(name = "portalItemLink")
    private String portalItemLink;

    @ValueMapValue(name = "logoLink")
    private String logoLink;

    @ValueMapValue(name = "buttonIcon")
    private String buttonIcon;

    @ValueMapValue(name = "iconOrImage")
    private String iconOrImage;

    @ValueMapValue(name = "buttonImagePath")
    private String buttonImagePath;

    @ValueMapValue(name = "buttonIconPathMobile")
    private String buttonIconPathMobile;

    @ValueMapValue(name = "backButtonText")
    private String backButtonText;

    @ValueMapValue(name = "fileReference")
    private String logoImage;

    @ValueMapValue(name = "hideNavigation")
    private Boolean hideNavigation;
    
    @ValueMapValue(name = "enableSearchbar")
    private Boolean enableSearchbar;

    @ChildResource(name = "portalItems")
    private Resource portalItems;

    private List<PortalItem> portalItemList = Collections.emptyList();

    @SlingObject
    private ResourceResolver resourceResolver;

    private Map<Header, Map<String, List<NavigationDetails>>> navigationItems = Collections.emptyMap();

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

        if(Objects.nonNull(portalItems)) {
            final List<Resource> portalList = org.apache.commons.compress.utils.Lists.newArrayList(portalItems.getChildren().iterator());

            if (CollectionUtils.isNotEmpty(portalList)) {
                portalItemList = portalList.stream().map(item -> item.adaptTo(PortalItem.class))
                        .collect(toCollection(LinkedList::new));
            }
        }
    }

    public Map<Header, Map<String, List<NavigationDetails>>> getNavigationItems() {
        return navigationItems;
    }

    public String getPortalItemLabel() {
        return portalItemLabel;
    }

    public String getPortalItemLink() {
        return portalItemLink;
    }

    public String getPortalBtnLabel() {
        return portalBtnLabel;
    }

    public String getButtonLink() { return buttonLink; }

    public String getLogoLink() {
        if (logoLink != null) {
            Resource pathResource = resourceResolver.getResource(logoLink);
            // check if resource exists and link is internal
            if (pathResource != null) {
                logoLink = logoLink + ".html";
            }
        }
        return logoLink; }

    public String getButtonIcon() { return buttonIcon; }

    public String getIconOrImage() { return iconOrImage; }

    public String getButtonImagePath() { return buttonImagePath; }

    public String getButtonIconPathMobile() { return buttonIconPathMobile; }

    public String getLogoImage() { return logoImage; }

    public String getBackButtonText() { return backButtonText; }

    public List<PortalItem> getPortalItemList() {
        return ImmutableList.copyOf(portalItemList);
    }

    public Boolean getHideNavigation() {
        return hideNavigation;
    }

    public Boolean getEnableSearchbar() {
        return enableSearchbar;
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
                resource -> resource.getChild(JcrConstants.JCR_CONTENT).adaptTo(Header.class),
                resourcePage -> Lists.newArrayList(resourcePage.getChildren().iterator()).
                        stream().filter(isPage()).
                        filter(isNotNavHidden()).map(page ->
                                            page.getChild(JcrConstants.JCR_CONTENT).adaptTo(NavigationDetails.class))
                        .collect(Collectors.toList()), (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }

    private Predicate<Resource> isPage() {
        return r -> r.getValueMap().get(JcrConstants.JCR_PRIMARYTYPE, String.class).equals(NT_PAGE);
    }

    private Predicate<Resource> isNotNavHidden() {
        return r ->  {
            Resource child = r.getChild(JcrConstants.JCR_CONTENT);
            
            /* Check if resource exists before getting the value to avoid a NPE. */
            if(child != null) {
                return child.getValueMap().get(HIDDEN_PROPERTY, Boolean.class) == null;
            }

            return false;
        };
    }
}
