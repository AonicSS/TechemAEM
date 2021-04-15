package com.techem.core.servlets;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageFilter;

public class SitemapPageFilter extends PageFilter
{
    private static final Logger LOG = LoggerFactory.getLogger(SitemapPageFilter.class);
    private ValueMap properties;
    private final HashSet<String> excludeFromSiteMapHashSet;

    public SitemapPageFilter(final boolean includeInvalid, final boolean includeHidden, final String[] excludeFromSiteMapProperties)
    {
        super(includeInvalid, includeHidden);
        this.excludeFromSiteMapHashSet = new HashSet<>(Arrays.asList(excludeFromSiteMapProperties));
    }

    public boolean isValidPageForSiteMap(final Page page)
    {
        this.properties = page.getProperties();
        return (this.includes(page) && !this.isExcludedFromSiteMap(page));
    }

    private boolean isExcludedFromSiteMap(final Page page)
    {
        boolean isExcludedFromSiteMap = false;

        try
        {
            final List<Map.Entry<String, Object>> excludeFromSiteMapCollection = this.properties.entrySet().stream().filter(entry -> this.excludeFromSiteMapHashSet.contains(entry.getKey())).collect(Collectors.toList());

            for (final Map.Entry<String, Object> currentFilter : excludeFromSiteMapCollection)
            {
                if (BooleanUtils.isFalse(BooleanUtils.toBoolean(currentFilter.getValue().toString())))
                {
                    isExcludedFromSiteMap = true;
                    break;
                }
            }
        }
        catch (final Exception e)
        {
            LOG.error(" in method isExcludedFromSiteMap: {0} - for Page: {1}", e.getMessage(), page.getPath());
        }
        return isExcludedFromSiteMap;
    }

}
