package com.techem.core.servlets;

import java.util.HashMap;
import java.util.Map;

import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceResolverUtil {

    public static String SERVICE_USER = "userutilityservice";
    private static Logger log = LoggerFactory.getLogger(ResourceResolverUtil.class);

    public static ResourceResolver getResolver(ResourceResolverFactory rFactory) {
        try {
            final Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put(ResourceResolverFactory.SUBSERVICE, SERVICE_USER);
            ResourceResolver resolver = rFactory.getServiceResourceResolver(paramMap);
            return resolver;
        }catch(Exception e) {
            log.error("ex: ", e);
            return null;
        }

    }
}