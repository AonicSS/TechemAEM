package com.techem.core.models;

import com.day.crx.JcrConstants;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import javax.annotation.PostConstruct;
import java.util.Objects;

// Custom XF logic for header and footer
// If Header/Footer XF is configured in Page Properties, use that
// Else get Header/Footer XF from Page Properties of parent market page (eg. /de/de/)
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ExperienceFragment {

    protected static final String CONTENT_ROOT_PATH = "/content/techem/";

    @Self
    private Resource resource;

    @SlingObject
    private ResourceResolver resourceResolver;

    @Self
    private Resource currentPage;

    private String target;

    private String marketLang;

    private String marketLangHome;

    private String xfNodeName;

    private String xfHeader;

    private String xfFooter;

    private String currentPageXfHeader;

    private String currentPageXfFooter;

    @PostConstruct
    protected void init() {
        if (Objects.nonNull(resource)) {
            xfNodeName = resource.getName();
            currentPage = resource.getParent();
            if (currentPage != null) {
                target = currentPage.getPath().replace(CONTENT_ROOT_PATH, "");
                marketLang = target.substring(0, target.indexOf("/", target.indexOf("/") + 1));
                marketLangHome = CONTENT_ROOT_PATH + marketLang + "/" + JcrConstants.JCR_CONTENT;
                currentPageXfHeader = currentPage.getValueMap().get("xfHeader", String.class);
                currentPageXfFooter = currentPage.getValueMap().get("xfFooter", String.class);
                xfHeader = currentPageXfHeader != null ? currentPageXfHeader : getMarketParentXf("xfHeader", marketLangHome);
                xfFooter = currentPageXfFooter != null ? currentPageXfFooter : getMarketParentXf("xfFooter", marketLangHome);
            }
        }
    }

    private String getMarketParentXf(String valueProp, String marketLangHome) {
        Resource pathResource = resourceResolver.getResource(marketLangHome);
        if (pathResource != null) {
            return pathResource.getValueMap().get(valueProp, String.class);
        } else {
            return null;
        }
    }

    public String getXfNodeName() {
        return xfNodeName;
    }

    public String getXfHeader() {
        return xfHeader;
    }

    public String getXfFooter() {
        return xfFooter;
    }
}
