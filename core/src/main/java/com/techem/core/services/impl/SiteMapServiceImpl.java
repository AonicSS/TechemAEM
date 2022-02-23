package com.techem.core.services.impl;

import com.techem.core.services.SiteMapService;
import com.techem.core.services.SiteMapService.SiteMapServiceConfig;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;

@Component(service = SiteMapService.class, immediate = true)
@Designate(ocd = SiteMapServiceConfig.class)
public class SiteMapServiceImpl implements SiteMapService {

    private SiteMapServiceConfig siteMapConfig;

    @Activate
    @Modified
    protected void activate(SiteMapServiceConfig configuration) {
        siteMapConfig = configuration;
    }

    @Override
    public SiteMapServiceConfig getConfig() {
        return siteMapConfig;
    }

}