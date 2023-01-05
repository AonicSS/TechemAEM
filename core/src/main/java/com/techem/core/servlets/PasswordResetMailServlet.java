package com.techem.core.servlets;

import lombok.extern.slf4j.Slf4j;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.jackrabbit.api.security.user.User;
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

@Slf4j
@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=Utility that generates and sends a password reset E-mail to valid users.", "sling.servlet.methods="+ HttpConstants.METHOD_POST, "sling.servlet.paths=/eu/techem/sendmail" })
public class PasswordResetMailServlet extends SlingAllMethodsServlet {

    private static final long serialVersionUID = 1L;
    private static final String EMAIL_TEMPLATE = "/conf/techem/settings/wcm/templates/Email_template";
    private final Random random = new Random();

    @Reference
    private transient MessageGatewayService msgGateWayService;

    @Reference
    private transient ResourceResolverFactory resourceResolverFactory;

    @Override
    protected void doPost(final SlingHttpServletRequest req, final SlingHttpServletResponse resp) throws ServletException, IOException {
        try {
            ResourceResolver resResolver = ResourceResolverUtil.getResolver(resourceResolverFactory);
            Resource templateResource = null;
            Node templateNode = null;

            String receiver = req.getParameter("email");
            String link = req.getParameter("pwRedir");

            String token = generateToken(receiver, resResolver);

            if(receiver != null && token != null) {
                LOGGER.info("Token generated, user exists.");

                templateResource = resResolver.getResource(EMAIL_TEMPLATE);
                templateNode = templateResource.adaptTo(Node.class);
                sendEmail(receiver, templateNode, token, link);
            }
        } catch (Exception e) {
            LOGGER.error("Mail servlet exception: ", e);
            resp.setStatus(302);
            return;
        }
        resp.setStatus(200);
    }

    protected void sendEmail(String receiver, Node templateNode, String token, String url) throws IOException, MessagingException, EmailException, RepositoryException {
        final Map<String, String> parameters = new HashMap<>();
        parameters.put("username", receiver);
        parameters.put("link", url + "?authToken=" + token);

        final MailTemplate mailTemplate = MailTemplate.create(EMAIL_TEMPLATE, templateNode.getSession());
        HtmlEmail mail = mailTemplate.getEmail(StrLookup.mapLookup(parameters), HtmlEmail.class);

        ArrayList<InternetAddress> mailReceiver = new ArrayList<>();
        mailReceiver.add(new InternetAddress(receiver));

        mail.setCharset("UTF-8");
        mail.setTo(mailReceiver);
        mail.setFrom("noreply@techem.com");
        mail.setSubject("Techem Investor Relations – Password reset");

        MessageGateway<HtmlEmail> msgGateWay = this.msgGateWayService.getGateway(HtmlEmail.class);
        msgGateWay.send(mail);

        LOGGER.info("Mail to {} was sent", receiver);
    }

    private String generateToken(String email, ResourceResolver resResolver) {

        try {
            Session session = resResolver.adaptTo(Session.class);
            if (session == null) {
                return null;
            }
            UserManager userMngr = ((JackrabbitSession) session).getUserManager();
            User usr = (User) userMngr.getAuthorizable(email);

            if(usr != null) {
                String tkn = DigestUtils.sha256Hex("techemAuthToken" + random.nextInt(10000) + "xibjc20zje") + "e:" + email + "t:" + System.currentTimeMillis();
                usr.setProperty("./profile/authToken", session.getValueFactory().createValue(tkn));
                session.save();
                return tkn;
            }
            LOGGER.warn("Cannot find user by email: {}", email);
        } catch (Exception e) {
            LOGGER.error("Exception: ", e);
        }
        return null;
    }
}
