package com.techem.core.services.impl;

import com.adobe.granite.omnisearch.api.suggestion.PredicateSuggestion;
import com.adobe.granite.omnisearch.spi.core.OmniSearchHandler;
import com.day.cq.i18n.I18n;
import com.day.cq.search.result.SearchResult;
import com.techem.core.services.RedirectsManagerService;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.osgi.service.component.propertytypes.ServiceRanking;

import javax.jcr.query.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
    OmniSearch service for the {@link RedirectsManagerService}. <p>
    Integrates the Redirects Manager into the AEM Search Functionality (OmniSearch), 
    enabling the search bar of the AEM Console to be used for finding Redirect Rules.
    @see RedirectsManagerServiceImpl
*/
@Component(service = OmniSearchHandler.class)
@ServiceDescription("Redirects Manager Omnisearch Service")
@ServiceRanking(0)
public final class RedirectRulesOmnisearchServiceImpl implements OmniSearchHandler {

    @Reference
    private RedirectsManagerService redirectsManager;

    private static final String OMNISEARCH_CFG_MODULE = "/apps/techem/components/redirects-manager/redirects-omnisearch";

    @Override
    public List<PredicateSuggestion> getPredicateSuggestions(ResourceResolver resourceResolver, I18n i18n, String term) {
        return null;
    }

    @Override
    public Query getSpellCheckQuery(ResourceResolver resourceResolver, String term) {
        return null;
    }

    @Override
    public Query getSuggestionQuery(ResourceResolver resourceResolver, String term) {
        return null;
    }

    @Override
    public Resource getModuleConfig(ResourceResolver resourceResolver) {
        return resourceResolver.getResource(OMNISEARCH_CFG_MODULE);
    }
    
    /**
        Executes a predicates-based query looking for the searched term within the properties of the redirect rules.
        @return <code>SearchResult</code> containing the result set of the query.
    */
    @Override
    public SearchResult getResults(ResourceResolver resourceResolver, Map<String, Object> predicateParameters, long limit, long offset) {
        String searchedRule = "%"+ ((String[])predicateParameters.get("fulltext"))[0] + "%";

        Map<String, String> predicates = new HashMap<String, String>();
        predicates.put("group.p.or", "true");
        predicates.put("group.1_property", RedirectsManagerService.REDIRECT_FROM);
        predicates.put("group.1_property.value", searchedRule);
        predicates.put("group.1_property.operation", "like");
        predicates.put("group.2_property", RedirectsManagerService.REDIRECT_TO);
        predicates.put("group.2_property.value", searchedRule);
        predicates.put("group.2_property.operation", "like");
        predicates.put("group.3_property", RedirectsManagerService.REDIRECT_CODE);
        predicates.put("group.3_property.value", searchedRule);
        predicates.put("group.3_property.operation", "like");
        predicates.put("group.4_property", RedirectsManagerService.REDIRECT_UNTIL);
        predicates.put("group.4_property.operation", "like");
        predicates.put("group.4_property.value", searchedRule);

        if(limit > 0) {
            predicates.put("p.limit", String.valueOf(limit));
        }

        if(offset > 0) {
            predicates.put("p.offset", String.valueOf(offset));
        }

        return redirectsManager.findRules(resourceResolver, predicates);
    }
    
    @Override
    public String getID() {
        return "redirects-omnisearch";
    }
}