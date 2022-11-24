package com.techem.core.filters;

import com.techem.core.selectors.EnvironmentSelector;
import com.techem.core.services.FriendlyCaptchaService;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.servlets.annotations.SlingServletFilter;
import org.apache.sling.servlets.annotations.SlingServletFilterScope;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.osgi.service.component.propertytypes.ServiceRanking;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

@Component(service = Filter.class)
@SlingServletFilter(
    scope = SlingServletFilterScope.REQUEST,
    methods = HttpConstants.METHOD_POST
)
@ServiceDescription("Filters POST requests from forms containing Friendly Captcha and validates them.")
/* 
    Run before the OOTB Form servlets:
    com.adobe.cq.wcm.core.components.internal.servlets.CoreFormHandlingServlet -> rank 610
    com.day.cq.wcm.foundation.forms.impl.FormsHandlingServlet -> rank 600
*/
@ServiceRanking(620)
public class FCaptchaPostFilter implements Filter {

    private static final String FORM_START = ":formstart";
    @Reference
    private FriendlyCaptchaService fCaptchaService;

    @Reference
    private EnvironmentSelector environmentSelector;
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        SlingHttpServletRequest slingRequest = (SlingHttpServletRequest) request;
        SlingHttpServletResponse slingResponse = (SlingHttpServletResponse) response;

        //Skip authors and some other conditions
        if (shouldSkipCaptcha(slingRequest)) {
            filterChain.doFilter(request, response);
            return;
        }

        String solData = slingRequest.getParameter(FriendlyCaptchaService.FC_SOLUTION_PARAM);
        String captchaToken = slingRequest.getParameter(FriendlyCaptchaService.FC_TOKEN);

        if (!fCaptchaService.validateCaptchaToken(solData, captchaToken)) {
            slingResponse.sendError(403);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean shouldSkipCaptcha(SlingHttpServletRequest request) {
        return environmentSelector.isAuthor() || request.getRequestParameter(FORM_START) == null ||
                StringUtils.startsWith(request.getRequestPathInfo().getResourcePath(), "/eu/techem");

    }
    @Override
    public void destroy() {
        //Mandatory override
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        //Mandatory override
    }
    
}