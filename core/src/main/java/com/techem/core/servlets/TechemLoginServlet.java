package com.techem.core.servlets;


import com.day.crx.security.token.TokenUtil;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.auth.core.spi.AuthenticationInfo;
import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.framework.Constants;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=Custom login", "sling.servlet.methods="+ HttpConstants.METHOD_POST, "sling.servlet.paths=/eu/techem/techemLogin" })
public class TechemLoginServlet extends SlingAllMethodsServlet {

    private static final Logger log = LoggerFactory.getLogger(TechemLoginServlet.class);

    @Reference
    private SlingRepository repository;

    @Activate
    public void activate(ComponentContext context){
        log.info("%%%%%%%%%%%%%%$$$$$$$$$$$%%%%%%%**************:   "+ "activate TechemLoginServlet");
    }

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException
    {

        SimpleCredentials credentials = new SimpleCredentials(request.getParameter("Username"), request.getParameter("Password").toCharArray());
        AuthenticationInfo authenticationInfo = null;
        Session session = null;
        try {
            session = this.repository.login(credentials);
            if(session != null) {
                authenticationInfo = createAuthenticationInfo(request, response, credentials.getUserID());
                if (session.isLive()) {
                    response.setStatus(200);
                    return;
                }
            }
        } catch (RepositoryException e) {
            log.error(this.getClass().getName() + " extractCredentials(..) Failed to log user in" + e.getMessage() + "; user id  -> " + credentials.getUserID(), e);
            if (e.getMessage() == "UserId/Password mismatch."){
                response.setStatus(401);
            }
            else {
                response.setStatus(403);
            }
            e.printStackTrace();
        }
    }

    private AuthenticationInfo createAuthenticationInfo(SlingHttpServletRequest request, SlingHttpServletResponse response,
                                                        String userId) throws RepositoryException {
        AuthenticationInfo authinfo = TokenUtil.createCredentials(request, response, this.repository, userId, true);
        return authinfo;
    }

}