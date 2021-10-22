package com.techem.core.servlets;

import org.apache.jackrabbit.api.security.user.User;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session;
import javax.jcr.Value;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

import org.osgi.framework.Constants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.servlets.HttpConstants;

@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=Change password utility.", "sling.servlet.methods="+ HttpConstants.METHOD_POST, "sling.servlet.paths=/eu/techem/changepw" })
public class ChangePasswordServlet extends SlingAllMethodsServlet {

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    private static final long serialVersionUID = 1L;
    private static final int maxHoursBeforeExpire = 24;
    private Logger log = LoggerFactory.getLogger(ChangePasswordServlet.class);
    private Session session;
    private UserManager userMngr;

    @Override
    protected void doPost(final SlingHttpServletRequest req, final SlingHttpServletResponse resp) throws ServletException, IOException {
        ResourceResolver resResolver = ResourceResolverUtil.getResolver(resourceResolverFactory);
        String authToken = req.getParameter("authToken");
        String chkTkn = req.getParameter("checkTkn");
        String newPass = req.getParameter("password");
        session = resResolver.adaptTo(Session.class);
        userMngr = resResolver.adaptTo(UserManager.class);

        if(chkTkn != null && chkTkn.equals("true")) {
            if(checkToken(authToken) != null) {
                resp.setStatus(200);
            }else {
                resp.setStatus(302);
            }
            return;
        }

        if(changeUserPassword(authToken, newPass)) {
            resp.setStatus(200);
        }else{
            resp.setStatus(302);
        }
    }

    private boolean changeUserPassword(String token, String newPW) {
        try {
            User usr = checkToken(token);

            if(usr != null) {
                usr.removeProperty("./profile/authToken");
                usr.changePassword(newPW);
                session.save();
                log.info("User password for " + usr.getID() + " was changed.");
                return true;
            }
        }catch(Exception e) {
            log.error("ex: ", e);
        }
        return false;
    }

    private User checkToken(String reqToken) {
        if(reqToken != null && reqToken.length() > 0) {
            try {
                String eMail = reqToken.substring(reqToken.indexOf("e:") + 2, reqToken.indexOf("t:"));
                User usr = (User) userMngr.getAuthorizable(eMail);
                Value[] usrToken = usr.getProperty("./profile/authToken");

                if(usr != null && usrToken != null && usrToken[0].getString().equals(reqToken)) {
                    String usrTokenStr = usrToken[0].getString();
                    String creationDate = usrTokenStr.split("t:")[1];
                    if((System.currentTimeMillis() - Long.parseLong(creationDate))/(60 * 60 * 1000) <= maxHoursBeforeExpire) {
                        return usr;
                    }
                }
            }catch(Exception e) {
                log.error("ex: ", e);
            }
        }
        return null;
    }
}
