package com.techem.core.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.day.cq.commons.jcr.JcrConstants;
import com.techem.core.services.URLsFilterService;

import org.apache.jackrabbit.oak.commons.PropertiesUtil;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.NonExistingResource;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.servlets.annotations.SlingServletFilter;
import org.apache.sling.servlets.annotations.SlingServletFilterScope;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.osgi.service.component.propertytypes.ServiceRanking;

@Component(service = Filter.class)
@SlingServletFilter(
        scope = SlingServletFilterScope.ERROR,
        methods = HttpConstants.METHOD_GET,
        resourceTypes = { Resource.RESOURCE_TYPE_NON_EXISTING },
        pattern = ".+.html$"
)
@ServiceDescription("Enables URLs to be case insensitive.")
@ServiceRanking(1900)
public class URLsFilter implements Filter {

    @Reference
    private URLsFilterService urlsFilterService;

    @Override
    public void init(FilterConfig config) throws ServletException { }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        SlingHttpServletRequest slingRequest = (SlingHttpServletRequest) request;
        SlingHttpServletResponse slingResponse = (SlingHttpServletResponse) response;
        ResourceResolver resourceResolver = slingRequest.getResourceResolver();

        if(resourceResolver != null && urlsFilterService != null && urlsFilterService.getConfig().enabled()) {
            String resPath = slingRequest.getPathInfo().replaceAll(".html", "").toLowerCase();
            String resExt = slingRequest.getRequestPathInfo().getExtension();
            boolean isAllowed = urlsFilterService.isAllowedExt(resExt) && urlsFilterService.isAllowedPath(resPath);
            Resource resolvedRes = findResource(resourceResolver, resPath);

            if(resolvedRes != null && !ResourceUtil.isNonExistingResource(resolvedRes) && isAllowed) {
                Resource resolvedResContent = null;
                boolean isRedirectPage = resolvedRes.isResourceType(URLsFilterService.SLING_REDIRECT);

                if(isRedirectPage) {
                    String redirectTarget = PropertiesUtil.toString(resolvedRes.getValueMap().get(URLsFilterService.SLING_TARGET), "").replaceAll(".html", "");
                    Resource redirectTargetRes = resourceResolver.getResource(redirectTarget);

                    if(redirectTargetRes != null) {
                        resolvedResContent = redirectTargetRes.getChild(JcrConstants.JCR_CONTENT);
                    }
                }else {
                    resolvedResContent = resolvedRes.getChild(JcrConstants.JCR_CONTENT);
                }

                boolean isCaseSensitive = resolvedResContent != null && PropertiesUtil.toBoolean(resolvedResContent.getValueMap().get(URLsFilterService.CASE_SENSITIVE), false);

                if(!isCaseSensitive) {
                    RequestDispatcher dp = slingRequest.getRequestDispatcher(resolvedRes);
                    dp.forward(slingRequest, slingResponse);
                    return;
                }
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() { }

    private Resource findResource(ResourceResolver resResolver, String resPath) {
        Resource resFound = resResolver.getResource(resPath);
        if(resFound != null) { return resFound; }

        String[] split = resPath.replaceAll(URLsFilterService.CONTENT_ROOT + "/", "").split("/");
        resFound = resResolver.getResource(URLsFilterService.CONTENT_ROOT);

        for(String p : split) {
            if(p.trim().length() == 0) { continue; }

            Iterable<Resource> kids = resFound.getChildren();

            for(Resource kidRes : kids) {
                String resName = kidRes.getName().toLowerCase();
                String path = p.toLowerCase();

                if(resName.equals(JcrConstants.JCR_CONTENT)) {
                    continue;
                }

                if(resName.equals(path)) {
                    resFound = kidRes;
                    break;
                }
            }
        }
        return resFound.getPath().toLowerCase().equals(resPath) ? resFound : new NonExistingResource(resResolver, resPath);
    }

}