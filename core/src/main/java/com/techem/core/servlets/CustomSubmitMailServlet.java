package com.techem.core.servlets;

import com.day.cq.mailer.MessageGateway;
import com.day.cq.mailer.MessageGatewayService;
import com.techem.core.services.FriendlyCaptchaService;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestParameter;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = Servlet.class, property = {"sling.servlet.methods={" + HttpConstants.METHOD_POST + "}", "sling.servlet.paths=" + "/eu/techem/customsubmitmail" })
public class CustomSubmitMailServlet extends SlingAllMethodsServlet {

    private static final Logger log = LoggerFactory.getLogger(CustomSubmitMailServlet.class);

    @Reference
    private transient MessageGatewayService messageGatewayService;
    
    @Reference
    private transient FriendlyCaptchaService fCaptchaService;

    private static final String MAIL_FROM = "noreply@techem.com";
    private static final String MAIL_SUBJ = "Techem Contact Form Submission";

    @Override
    protected void doPost(final SlingHttpServletRequest req, final SlingHttpServletResponse resp) throws ServletException, IOException {
        if(fCaptchaService == null) { return; }

        boolean friendlyCaptchaEnabled = StringUtils.isNotBlank(req.getParameter(FriendlyCaptchaService.FC_ENABLED)) && req.getParameter(FriendlyCaptchaService.FC_ENABLED).equals("true");

        if(friendlyCaptchaEnabled) {
            String solData = req.getParameter(FriendlyCaptchaService.FC_SOLUTION_PARAM);
            String captchaToken = req.getParameter(FriendlyCaptchaService.FC_TOKEN);
            
            if(StringUtils.isBlank(solData) || StringUtils.isBlank(captchaToken) || !fCaptchaService.validateCaptchaToken(solData, captchaToken)) {
                resp.sendError(302);
                return;
            }
        }

        String emailEndpoint = req.getParameter(":emailData");

        if(StringUtils.isBlank(emailEndpoint)) { return; }
        
        List<RequestParameter> paramList = req.getRequestParameterList().stream().filter(p -> !p.getName().startsWith(":")).collect(Collectors.toList());
        String formData = "You've received a new form based mail from: " +  req.getHeader("Referer") + "\n\n" + "Values: " + "\n\n";
        
        if(paramList.isEmpty()) { log.info("Form has no parameters, skipping sending E-mail."); return; }

        for(RequestParameter param : paramList) {
            formData += (param.getName() + ": " + param.getString()).replaceAll("\\<.*?>","") + "\n";
        }
  
        if(emailEndpoint != null) {
            try {
                String[] recipients = { emailEndpoint };
                sendEmail(MAIL_SUBJ, formData, recipients);
            } catch (Exception e) {
                log.error("Mail servlet exception: ", e);
                resp.sendError(302);
                return;
            }
        }

        resp.setStatus(200);
    }

    private void sendEmail(String subjectLine, String msgBody, String[] recipients) throws EmailException {
        Email email = new HtmlEmail();

        for (String recipient : recipients) {
            email.addTo(recipient, recipient);
        }

        email.setSubject(subjectLine);
        email.setMsg(msgBody);
        email.setCharset(StandardCharsets.UTF_8.name());
        email.setFrom(MAIL_FROM);
        MessageGateway<Email> messageGateway = messageGatewayService.getGateway(HtmlEmail.class);

        if (messageGateway != null) {
            messageGateway.send(email);
        } else {
            log.error("The message gateway could not be retrieved.");
        }
    }
}