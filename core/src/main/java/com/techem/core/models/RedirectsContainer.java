package com.techem.core.models;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.techem.core.services.RedirectsManagerService;

/**
    Model used for the list of redirects within the AEM Console. 
*/
@Model(adaptables = SlingHttpServletRequest.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class RedirectsContainer {

    @Inject
    private RedirectsManagerService redirectManager;

    @SlingObject
    private SlingHttpServletRequest request;

    @Getter
    @ValueMapValue(name="redirects")
    private List<RedirectRule> redirects;

    @ValueMapValue(name="enabled")
    private boolean enabled;

    @ValueMapValue(name="cfgPID")
    private String cfgPID;

    private int offset = 0;
    private int limit = 0;

    @PostConstruct
    protected void init() {
        if(request == null || redirectManager == null) { return; }

        enabled = redirectManager.isEnabled();
        cfgPID = redirectManager.getPID();
        
        String[] selectors = request.getRequestPathInfo().getSelectors();

        if(selectors.length >= 2) {
            /* We get the filter prop from the referer as that's the easiest way to get it from the datasource. If it's stupid but it works it ain't stupid right?? */
            String filterBy = request.getHeader("Referer") != null && request.getHeader("Referer").contains("filterBy") ? getFilter(request.getHeader("Referer")) : null;
            List<RedirectRule> rulesList = (filterBy != null && !StringUtils.isBlank(filterBy)) ? redirectManager.getRules(filterBy) : redirectManager.getRules();
            redirects = new ArrayList<RedirectRule>();
           
            try{
                offset = Integer.parseInt(selectors[0]);
                limit = Integer.parseInt(selectors[1]);
            }catch(NumberFormatException ex) { /* Selectors weren't appropriate. Not cool. */ }

            if(rulesList.size() > 0) { 
                redirects = rulesList.subList(offset, rulesList.size()).stream().limit(limit).collect(Collectors.toList());
            }
        }
    }

    public boolean getEnabled() {
        return enabled;
    }

    public String getCfgPID() {
        return cfgPID;
    }

    private String getFilter(String qString) {

        String[] qsSplit = qString.split("\\?");
        
        if(qsSplit != null && qsSplit.length >= 1) {
            String[] qsSplit2 = qsSplit[1].split("\\&");
            
            for(String str : qsSplit2) {
                if(str.substring(0, str.indexOf("=")).equals("filterBy")) {
                    return str.substring(str.indexOf("=")+1, str.length());
                }
            }
        }
        return null;
    }
}