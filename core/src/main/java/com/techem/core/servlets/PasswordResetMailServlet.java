package com.techem.core.servlets;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.jackrabbit.api.security.user.User;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.jackrabbit.api.JackrabbitSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.day.cq.commons.mail.MailTemplate;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.text.StrLookup;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

import com.day.cq.mailer.MessageGateway;
import com.day.cq.mailer.MessageGatewayService;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import java.util.ArrayList;

import org.osgi.framework.Constants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.servlets.HttpConstants;

@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=Utility that generates and sends a password reset E-mail to valid users.", "sling.servlet.methods="+ HttpConstants.METHOD_POST, "sling.servlet.paths=/eu/techem/sendmail" })
public class PasswordResetMailServlet extends SlingAllMethodsServlet {

    private static final long serialVersionUID = 1L;
    private static final String EMAIL_TEMPLATE = "/conf/techem/settings/wcm/templates/Email_template";
    private Logger log = LoggerFactory.getLogger(PasswordResetMailServlet.class);
    private Random random = new Random();
    private Session session;
    private UserManager userMngr;

    @Reference
    private MessageGatewayService msgGateWayService;

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @Override
    protected void doPost(final SlingHttpServletRequest req, final SlingHttpServletResponse resp) throws ServletException, IOException {
        try {
            ResourceResolver resResolver = ResourceResolverUtil.getResolver(resourceResolverFactory);
            Resource templateResource = null;
            Node templateNode = null;

            String receiver = req.getParameter("email");
            String link = req.getParameter("pwRedir");

            session = resResolver.adaptTo(Session.class);
            userMngr = ((JackrabbitSession) session).getUserManager();
            String token = generateToken(receiver);

            if(receiver != null && token != null) {
                log.info("Token generated, user exists.");

                templateResource = resResolver.getResource(EMAIL_TEMPLATE);
                templateNode = templateResource.adaptTo(Node.class);
                sendEmail(receiver, templateNode, token, link);
            }
        } catch (Exception e) {
            log.error("Mail servlet exception: ", e);
            resp.setStatus(302);
            return;
        }
        resp.setStatus(200);
    }

    protected void sendEmail(String receiver, Node templateNode, String token, String url) throws IOException, MessagingException, EmailException, RepositoryException {
        final Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("username", receiver);
        parameters.put("link", url + "?authToken=" + token);

        final MailTemplate mailTemplate = MailTemplate.create(EMAIL_TEMPLATE, templateNode.getSession());
        HtmlEmail mail = mailTemplate.getEmail(StrLookup.mapLookup(parameters), HtmlEmail.class);

        ArrayList<InternetAddress> mailReceiver = new ArrayList<InternetAddress>();
        mailReceiver.add(new InternetAddress(receiver));

        mail.setCharset("UTF-8");
        mail.setTo(mailReceiver);
        mail.setFrom("noreply@techem.com");
        mail.setSubject("Techem Investor Relations – Password reset");

        MessageGateway<HtmlEmail> msgGateWay = this.msgGateWayService.getGateway(HtmlEmail.class);
        msgGateWay.send(mail);

        log.info("Mail to " + receiver + " was sent");
    }

    private String generateToken(String email) {
        try {
            User usr = (User) userMngr.getAuthorizable(email);

            if(usr != null) {
                String tkn = DigestUtils.sha256Hex("techemAuthToken" + random.nextInt(10000) + "xibjc20zje") + "e:" + email + "t:" + System.currentTimeMillis();
                usr.setProperty("./profile/authToken", session.getValueFactory().createValue(tkn));
                session.save();
                return tkn;
            }
            log.warn("Cannot find user by email: " + email);
        } catch (Exception e) {
            log.error("Exception: ", e);
        }
        return null;
    }
}
