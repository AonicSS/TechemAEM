package com.techem.core.servlets;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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

@Slf4j
@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=Change password utility.", "sling.servlet.methods="+ HttpConstants.METHOD_POST, "sling.servlet.paths=/eu/techem/changepw" })
public class ChangePasswordServlet extends SlingAllMethodsServlet {

    @Reference
    private transient ResourceResolverFactory resourceResolverFactory;

    private static final long serialVersionUID = 1L;
    private static final int maxHoursBeforeExpire = 24;

    @Override
    protected void doPost(final SlingHttpServletRequest req, final SlingHttpServletResponse resp) throws ServletException, IOException {
        ResourceResolver resResolver = ResourceResolverUtil.getResolver(resourceResolverFactory);
        String authToken = req.getParameter("authToken");
        String chkTkn = req.getParameter("checkTkn");
        String newPass = req.getParameter("password");

        if(chkTkn != null && chkTkn.equals("true")) {
            if(checkToken(authToken, resResolver) != null) {
                resp.setStatus(200);
            }else {
                resp.setStatus(302);
            }
            return;
        }

        if(changeUserPassword(authToken, newPass, resResolver)) {
            resp.setStatus(200);
        }else{
            resp.setStatus(302);
        }
    }

    private boolean changeUserPassword(String token, String newPW, ResourceResolver resolver) {
        try {
            User usr = checkToken(token, resolver);
            Session session = resolver.adaptTo(Session.class);
            if(usr != null && session != null) {
                usr.removeProperty("./profile/authToken");
                usr.changePassword(newPW);
                session.save();
                LOGGER.info("User password for {} was changed.", usr.getID());
                return true;
            }
        }catch (Exception e) {
            LOGGER.error("ex: ", e);
        }
        return false;
    }

    private User checkToken(String reqToken, ResourceResolver resResolver) {
        UserManager userMngr = resResolver.adaptTo(UserManager.class);
        if (StringUtils.isNotBlank(reqToken) && userMngr != null) {
            try {
                String eMail = reqToken.substring(reqToken.indexOf("e:") + 2, reqToken.indexOf("t:"));

                User usr = (User) userMngr.getAuthorizable(eMail);
                if (usr != null) {
                    Value[] usrToken = usr.getProperty("./profile/authToken");
                    if (usrToken != null && usrToken[0].getString().equals(reqToken)) {
                        String usrTokenStr = usrToken[0].getString();
                        String creationDate = usrTokenStr.split("t:")[1];
                        if ((System.currentTimeMillis() - Long.parseLong(creationDate)) / (60 * 60 * 1000) <= maxHoursBeforeExpire) {
                            return usr;
                        }
                    }
                }
            }catch(Exception e) {
                LOGGER.error("ex: ", e);
            }
        }
        return null;
    }
}
