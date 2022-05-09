package com.techem.core.filters;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.day.cq.wcm.api.WCMMode;
import com.techem.core.models.RedirectRule;
import com.techem.core.services.RedirectsManagerService;
import com.techem.core.services.URLsFilterService;
import com.techem.core.servlets.ResourceResolverUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.osgi.service.component.propertytypes.ServiceRanking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.sling.servlets.annotations.SlingServletFilterScope;
import org.apache.sling.servlets.annotations.SlingServletFilter;
import org.apache.sling.api.servlets.HttpConstants;

/**
    Sling filter for the Redirect Manager. Filters requests and matches them to any existing rule. <p>
    This task can be configured by OSGi config (PID: <code>com.techem.core.services.impl.RedirectsManagerServiceImpl</code>) and is <b>enabled</b> by default.
*/
@Component(service = Filter.class)
@SlingServletFilter(
    scope = { SlingServletFilterScope.REQUEST },
    pattern = RedirectsManagerService.REDIRECT_CONTENT_PATH + "/.*|" + URLsFilterService.CONTENT_ROOT + "/.*",
    methods = { HttpConstants.METHOD_GET, HttpConstants.METHOD_HEAD }
)
@ServiceDescription("Redirects Manager Filter")
@ServiceRanking(1901)
public class RedirectsFilter implements Filter {

    @Reference 
    private ResourceResolverFactory resourceResolverFactory; 

    @Reference
    private RedirectsManagerService redirectsManager;
    
    private ResourceResolver resResolver = null;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Override
    public void init(FilterConfig arg0) throws ServletException { }
    
    /**
       Filters requests and matches them to any existing rule, if no valid resource was found before hand on the given path.<p>
    */
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        SlingHttpServletRequest slingReq = (SlingHttpServletRequest)req;
        SlingHttpServletResponse slingResp = (SlingHttpServletResponse)resp;
        resResolver = ResourceResolverUtil.getResolver(resourceResolverFactory); 
        
        if(redirectsManager == null || resResolver == null || !redirectsManager.getConfig().enabled()) {
            chain.doFilter(req, resp); 
            return; 
        }

        long t0 = System.currentTimeMillis();
        RedirectRule ruleFromReq = matchRuleToRequest(slingReq);

        if(ruleFromReq == null) { chain.doFilter(req, resp); return; }

        String ruleTo = ruleFromReq.getKeepQS() ? keepQueryString(ruleFromReq.getTo(), slingReq.getQueryString()) : ruleFromReq.getTo();
        
        if(ruleFromReq.getCode() == 301 || ruleFromReq.getCode() == 302) {
            slingResp.setHeader("Location", ruleTo);
        }

        slingResp.setStatus(ruleFromReq.getCode());
        slingResp.setHeader("Cache-Control", "no-cache");
        logger.info("Redirected from {} to {} in {} ms.", slingReq.getRequestURL(), ruleFromReq.getTo(), System.currentTimeMillis() - t0);
    }

    @Override
    public void destroy() { }

    /**
       Validates the request before looking for any matching redirect rule.<p>
       The request is valid if and only if:
        <ul>
            <li><b>WCMMode</b> is set to <code>DISABLED</code> (only redirect on publish).</li>
            <li><b>Path</b> is not part of the exceptions defined in the config.</li>
        </ul>

        @return <code>boolean</code> <code>true</code> if the request was valid,.<code>false</code> otherwise.
    */
    private boolean validateRequest(SlingHttpServletRequest req) {
        if(req == null) { return false; }

        WCMMode wcmMode = WCMMode.fromRequest(req);

        if (wcmMode != null && wcmMode != WCMMode.DISABLED) {
            return false;
        }

        String[] exceptions = redirectsManager.getConfig().exceptions();
        String reqResPath = req.getRequestPathInfo().getResourcePath().replaceAll(RedirectsManagerService.REDIRECT_CONTENT_PATH + "|" + URLsFilterService.CONTENT_ROOT, "");
        boolean matchesException = Arrays.stream(exceptions).anyMatch(p -> ruleRegex(reqResPath, p));

        if(exceptions.length > 0 && matchesException) {
            return false;
        }

        return true;
    }

    /**
        Matches the request to a rule. The request <b>MUST</b> be first validated in order for the rule to be matched. <p>
        This method tries to math rules both based on the current domain and the current path. <p>
        NOTE: If the rule has expired, <code>null</code> will be returned.
        @return <code>RedirectRule</code> if any rule has matched to the request, <code>null</code> otherwise.
    */
    private RedirectRule matchRuleToRequest(SlingHttpServletRequest req) throws UnsupportedEncodingException {

        if(req == null || !validateRequest(req)) { return null; }
        boolean isReqContent = !req.getPathInfo().startsWith(RedirectsManagerService.REDIRECT_CONTENT_PATH);
        boolean isForeignURL = StringUtils.equals(req.getPathInfo(), RedirectsManagerService.REDIRECT_GLOBAL_IDENTIFIER) && StringUtils.contains(req.getQueryString(), RedirectsManagerService.REDIRECT_GLOBAL_IDENTIFIER_QS);
        String reqURLTmp = isForeignURL ? getQStringVal(req.getQueryString(), RedirectsManagerService.REDIRECT_GLOBAL_IDENTIFIER_QS) : req.getRequestURL().toString();
        String reqURL = reqURLTmp.replaceAll(RedirectsManagerService.REDIRECT_CONTENT_PATH + "|" + URLsFilterService.CONTENT_ROOT, "");
        String reqPath = req.getPathInfo().replaceAll(RedirectsManagerService.REDIRECT_CONTENT_PATH + "|" + URLsFilterService.CONTENT_ROOT, "");

        if(isReqContent) {
            reqURL = reqURL.replaceAll(".html", "");
            reqPath = reqPath.replaceAll(".html", "");
        }
        
        String ruleNameLocal = "rule_" + URLEncoder.encode(reqPath, "UTF-8").replaceAll("[[*]]+", "%2A");
        String ruleNameGlobal = "rule_" + URLEncoder.encode(reqURL, "UTF-8").replaceAll("[[*]]+", "%2A");

        RedirectRule redirRule = redirectsManager.getRule(ruleNameLocal) == null ? redirectsManager.getRule(ruleNameGlobal) : redirectsManager.getRule(ruleNameLocal);

        if(redirRule != null) {
            Date ruleExpiry = redirRule.getUntil();
            
            if(ruleExpiry != null && ruleExpiry.before(new Date())) {
                return null;
            }
            return redirRule;
        }

        List<RedirectRule> redirects = redirectsManager.getRules();
        
        for(RedirectRule rule : redirects) {
            String fromRule = rule.getFrom();
            Date ruleExpiry = rule.getUntil();
            
            if(ruleExpiry != null && ruleExpiry.before(new Date())) {
                continue;
            }
            
            if(ruleRegex(reqURL, fromRule) || ruleRegex(reqPath, fromRule)) {
                return rule;
            }
        }

        if(isForeignURL) {
            try {
                RedirectRule tmpRule = new RedirectRule();
                tmpRule.setTo(RedirectsManagerService.REDIRECT_GLOBAL_LOCATION + new URL(reqURLTmp).getPath());
                tmpRule.setCode(302);
                tmpRule.setKeepQS(true);
                return tmpRule;
            }catch(Exception e) { }
        }

        return null;
    }

    /**
        Rules can contain RegExs. This method tries to match rules based on the request and the rule's regex.<p>
        The "string start" (<code>^</code>) and the "end string" (<code>$</code>) characters 
        will be added automatically if not already present in the regex.

        @return <code>boolean</code> if the rule regex matches the request path, <code>false</code> otherwise.
    */
    private boolean ruleRegex(String fromRequest, String fromRule) {
        try {
            String fromRuleFormatted = (fromRule.startsWith("^") ? "" : "^") + fromRule + (fromRule.endsWith("$") ? "" : "$");
            return Pattern.compile(fromRuleFormatted).matcher(fromRequest).find();
        } catch (PatternSyntaxException ex) {
            logger.error("Rule has invalid regex: {}. Ignoring rule.", fromRule);
        }
        return false;
    }

    /**
        Preserves the query string part of the request. Filters out the <code>wcmmode</code> part.

        @return <code>String</code> the query string of the request.
    */
    private String keepQueryString(String ruleTo, String queryStr) {
        if(queryStr == null) { return ruleTo; }
        
        queryStr = queryStr.replaceAll("(&?wcmmode=\\w+&?)|(&?" + RedirectsManagerService.REDIRECT_GLOBAL_IDENTIFIER_QS + "=(?:.+&|.+$))", "");
        
        if(queryStr.length() > 0) {
            return ruleTo + "?" + queryStr;
        }
        
        return ruleTo;
    }
    
    private String getQStringVal(String qString, String qKey) {

        String[] qsSplit = qString.split("\\&");
        
        if(qsSplit != null && qsSplit.length >= 1) {
            
            for(String str : qsSplit) {
                if(str.substring(0, str.indexOf("=")).equals(qKey)) {
                    return str.substring(str.indexOf("=")+1, str.length());
                }
            }
        }
        return null;
    }
}