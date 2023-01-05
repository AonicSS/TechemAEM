package com.techem.core.servlets;

import lombok.extern.slf4j.Slf4j;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ResourceResolverUtil {

    private ResourceResolverUtil() {
        //Private constructor
    }
    public static final String SERVICE_USER = "userutilityservice";

    public static ResourceResolver getResolver(ResourceResolverFactory rFactory) {
        try {
            final Map<String, Object> paramMap = new HashMap<>();
            paramMap.put(ResourceResolverFactory.SUBSERVICE, SERVICE_USER);
            return rFactory.getServiceResourceResolver(paramMap);
        }catch(Exception e) {
            LOGGER.error("ex: ", e);
            return null;
        }

    }
}