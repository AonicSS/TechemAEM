package com.techem.core.services.impl;

import java.util.Arrays;

import com.techem.core.services.URLsFilterService;
import com.techem.core.services.URLsFilterService.URLsFilterConfig;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;

import org.apache.commons.lang3.StringUtils;

@Component(service = URLsFilterService.class, immediate = true)
@Designate(ocd = URLsFilterConfig.class)
public class URLsFilterServiceImpl implements URLsFilterService {
    private URLsFilterConfig urlFilterConfig;

    @Activate
    @Modified
    protected void activate(URLsFilterConfig configuration) {
        urlFilterConfig = configuration;
    }

    @Override
    public URLsFilterConfig getConfig() {
        return urlFilterConfig;
    }

    @Override
    public boolean isAllowedExt(String ext) {
        if(urlFilterConfig.extensions() != null && urlFilterConfig.extensions().length > 0 && StringUtils.isNotBlank(ext)) {
            return !Arrays.asList(urlFilterConfig.extensions()).contains(ext);
        }
        return true;
    }

    @Override
    public boolean isAllowedPath(String path) {
        if(urlFilterConfig.paths() != null && urlFilterConfig.paths().length > 0 && StringUtils.isNotBlank(path)) {
            return !Arrays.asList(urlFilterConfig.paths()).contains(path);
        }
        return true;
    }
}