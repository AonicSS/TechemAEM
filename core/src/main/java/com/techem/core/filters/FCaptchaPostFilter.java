package com.techem.core.filters;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

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

    @Reference
    private FriendlyCaptchaService fCaptchaService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        SlingHttpServletRequest slingRequest = (SlingHttpServletRequest) request;
        SlingHttpServletResponse slingResponse = (SlingHttpServletResponse) response;
        String solData = slingRequest.getParameter(FriendlyCaptchaService.FC_SOLUTION_PARAM);
        String captchaToken = slingRequest.getParameter(FriendlyCaptchaService.FC_TOKEN);
        boolean hasFriendlyCaptcha = slingRequest.getParameter(FriendlyCaptchaService.FC_ENABLED) != null && slingRequest.getParameter(FriendlyCaptchaService.FC_ENABLED).equals("true");
        boolean hasFCParams = StringUtils.isNotBlank(solData) && StringUtils.isNotBlank(captchaToken);

        if((hasFriendlyCaptcha || hasFCParams) && !fCaptchaService.validateCaptchaToken(solData, captchaToken)) {
            slingResponse.sendError(403);
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {}

    @Override
    public void init(FilterConfig arg0) throws ServletException {}
    
}