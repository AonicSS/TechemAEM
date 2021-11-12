package com.techem.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,
        resourceType = "techem/components/yext-search-bar")
public class YextSearchBar {

    @ValueMapValue(name = "resultsPage")
    private String resultsPage;

    @ValueMapValue(name = "searchIcon")
    private String searchIcon;

    public String getResultsPage() {
        return resultsPage;
    };

    public String getSearchIcon() {
        return searchIcon;
    }
}