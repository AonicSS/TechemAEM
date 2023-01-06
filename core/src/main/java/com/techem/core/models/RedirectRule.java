package com.techem.core.models;

import lombok.Getter;
import lombok.Setter;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.adobe.granite.security.user.util.AuthorizableUtil;
import com.day.cq.wcm.api.NameConstants;
import com.techem.core.services.RedirectsManagerService;

/**
    Class representing a Redirect Rule. It holds infromation about the rule, such as the source, destination etc.
*/
@Model(adaptables = { Resource.class, RedirectsContainer.class, ResourceResolver.class, SlingHttpServletRequest.class }, adapters = RedirectRule.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class RedirectRule {

    @Inject
    private Resource resource;

    @SlingObject
    private ResourceResolver resResolver;

    @ValueMapValue(name = RedirectsManagerService.REDIRECT_FROM)
    private String from;

    @ValueMapValue(name = RedirectsManagerService.REDIRECT_TO)
    private String to;

    @ValueMapValue(name = RedirectsManagerService.REDIRECT_CODE)
    private int code;

    @Getter
    @Setter
    @ValueMapValue(name = RedirectsManagerService.REDIRECT_UNTIL)
    private Date until;

    @ValueMapValue(name = NameConstants.PN_PAGE_LAST_REPLICATION_ACTION)
    private String published;

    @Getter
    @ValueMapValue(name = NameConstants.PN_PAGE_LAST_REPLICATED)
    private Date publishDate;

    @ValueMapValue(name = NameConstants.PN_PAGE_LAST_REPLICATED_BY)
    private String publishedBy;

    @ValueMapValue(name = RedirectsManagerService.REDIRECT_STATUS)
    private int statusCode;

    @Getter
    @ValueMapValue(name = RedirectsManagerService.REDIRECT_STATUS_LAST_CHECKED)
    private Date statusDate;

    @ValueMapValue(name = RedirectsManagerService.REDIRECT_MARKET)
    private String market;

    @ValueMapValue(name = RedirectsManagerService.REDIRECT_KEEP_QS)
    private boolean keepQS;

    @ValueMapValue(name = "path")
    private String path;

    @PostConstruct
    protected void init() {
        if(resource != null) {
            path = resource.getPath();
        }
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getMarket() {
        return market;
    }

    public int getCode() {
        return code;
    }

    public String getPath() {
        return path;
    }

    public void setFrom(String url) {
        from = url;
    }

    public void setTo(String destination) {
        to = destination;
    }

    public void setCode(int c) {
        code = c;
    }

    public void setMarket(String mkt) {
        market = mkt;
    }

    public void setKeepQS(boolean kqs) {
        keepQS = kqs;
    }

    public String getPublished() {
        return published;
    }

    public String getPublishedBy() {
        if(resResolver != null && publishedBy != null) {
            return AuthorizableUtil.getFormattedName(resResolver, publishedBy);
        }
        return publishedBy;
    }

    public int getStatusCode() {
        return statusCode;
    }


    public boolean getKeepQS() {
        return keepQS;
    }
}