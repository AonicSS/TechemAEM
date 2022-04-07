package com.techem.core.servlets;

import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.mailer.MessageGateway;
import com.day.cq.mailer.MessageGatewayService;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestParameter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = Servlet.class, property = {"sling.servlet.methods={" + HttpConstants.METHOD_POST + ", " + HttpConstants.METHOD_GET + "}", "sling.servlet.paths=" + "/eu/techem/customsubmitmail" })
public class CustomSubmitMailServlet extends SlingAllMethodsServlet {

    private static final Logger LOG = LoggerFactory.getLogger(CustomSubmitMailServlet.class);

    @Reference
    private MessageGatewayService messageGatewayService;

    @Override
    protected void doGet(final SlingHttpServletRequest req,
                         final SlingHttpServletResponse resp) throws ServletException, IOException {
        LOG.info("STARTED CUSTOM GET");
        System.out.println("STARTED CUSTOM GET");
        final Resource resource = req.getResource();
        resp.setContentType("application/json");
        resp.getWriter().write("Title = " + resource.getValueMap().get(JcrConstants.JCR_TITLE));
    }

    @Override
    protected void doPost(final SlingHttpServletRequest req,
                         final SlingHttpServletResponse resp) throws ServletException, IOException {


        LOG.info("STARTED CUSTOM POST");

        List<RequestParameter> list = req.getRequestParameterList();
        StringBuilder message = new StringBuilder(10000);
        message.append("You've received a new form based mail from: " +  req.getHeader("Referer") + "\n\n" + "Values: " + "\n\n");
        for(int i = 0; i<list.size()-2; i++){
            RequestParameter param = list.get(i);
            
            if(param.getName().equals("emailData")) { continue; }
            message.append(param.getName() + " : " + param.getString() +"\n");
        }

        RequestDispatcher rd = req.getRequestDispatcher("/eu/techem/friendlycaptcha");
        rd.forward(req, resp);
        if (resp.getStatus() == 200) {
            String emailEndpoint = req.getParameter("emailData");

            if(emailEndpoint != null) {
                try {
                    String[] recipients = { emailEndpoint };
                    sendEmail("Techem form submission",
                            message.toString(), recipients);
                    resp.setStatus(200);
                } catch (Exception e) {
                    LOG.error("Mail servlet exception: ", e);
                    resp.setStatus(302);
                    return;
                }
            }

            resp.setStatus(200);
        }

    }
    private void sendEmail(String subjectLine, String msgBody, String[] recipients) throws EmailException {
        Email email = new HtmlEmail();
        for (String recipient : recipients) {
            email.addTo(recipient, recipient);
        }
        email.setSubject(subjectLine);
        email.setMsg(msgBody);
        email.setFrom("noreply@techem.com");
        MessageGateway<Email> messageGateway = messageGatewayService.getGateway(HtmlEmail.class);
        if (messageGateway != null) {
            LOG.debug("sending out email");
            messageGateway.send((Email) email);
        } else {
            LOG.error("The message gateway could not be retrieved.");
        }
    }
}