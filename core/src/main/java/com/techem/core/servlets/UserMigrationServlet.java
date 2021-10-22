package com.techem.core.servlets;

import org.osgi.framework.Constants;
import org.apache.jackrabbit.api.security.user.User;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.jackrabbit.api.security.user.Group;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestParameter;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session;
import javax.jcr.ValueFactory;
import javax.jcr.Value;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=Bulk user import utility.", "sling.servlet.methods=POST", "sling.servlet.paths=" + "/bin/techem/bulkimport" })
public class UserMigrationServlet extends SlingAllMethodsServlet {

    private static final long serialVersionUID = 1L;
    private static final String groupID = "cug_fe_investor-relations";
    private Logger logger = LoggerFactory.getLogger(UserMigrationServlet.class);
    private Session session;
    private UserManager userMngr;
    private ValueFactory valueFactory;
    private Random random = new Random();
    private int tally;
    private boolean importErrors;

    @Override
    protected void doPost(final SlingHttpServletRequest req, final SlingHttpServletResponse resp) throws ServletException, IOException {
        ResourceResolver resResolver = req.getResourceResolver();
        int totalEntries = 0;
        session = resResolver.adaptTo(Session.class);
        userMngr = resResolver.adaptTo(UserManager.class);
        tally = 0;
        importErrors = false;
        boolean multiPart = ServletFileUpload.isMultipartContent(req);
        Map<String, RequestParameter[]> params = req.getRequestParameterMap();
        Iterator<Map.Entry<String, RequestParameter[]>> paramsIterator = params.entrySet().iterator();

        Map.Entry<String, RequestParameter[]> pairs = paramsIterator.next();
        RequestParameter[] parameterArray = pairs.getValue();

        RequestParameter file = parameterArray[0];
        String mimeType = FilenameUtils.getExtension(file.getFileName());

        if (multiPart && mimeType != null && mimeType.equalsIgnoreCase("csv")){
            try {
                InputStream fileStream = file.getInputStream();
                BufferedReader csvReader = new BufferedReader(new InputStreamReader(fileStream));
                String row = "";
                boolean headerLine = true;
                List<String> csvHeaders = new ArrayList<String>();
                HashMap<String, Value> userDetails = new HashMap<String, Value>();
                valueFactory = session.getValueFactory();

                while ((row = csvReader.readLine()) != null) {
                    String[] data = row.split(",");
                    if(headerLine) {
                        for(String str : data) {
                            switch(str) {
                                case "first_name": str = "givenName"; break;
                                case "last_name": str = "familyName"; break;
                                case "static_info_country": str = "country"; break;
                                case "telephone": str = "phoneNumber"; break;
                                case "address": str = "street"; break;
                                case "zip": str = "postalCode"; break;
                                default: break;
                            }
                            csvHeaders.add(str);
                        }
                        headerLine = false;
                        continue;
                    }
                    totalEntries++;
                    if(data.length != csvHeaders.size()){
                        logger.warn("Data size does not match header size. Please check CSV! Skipping current iteration.");
                        importErrors = true;
                        continue;
                    }

                    for(int i = 0; i < data.length; i++) {
                        userDetails.put(csvHeaders.get(i), valueFactory.createValue(data[i]));
                    }

                    userDetails.put("isActivated", valueFactory.createValue(false));
                    userDetails.put("isMigrated", valueFactory.createValue(true));

                    createUser(userDetails);
                    userDetails.clear();
                }

                csvReader.close();

            } catch(Exception e){
                logger.error(e.getMessage(), e);
            }

            String msg = tally > 0 ? "Total: " + totalEntries + " users<br/>Successfully imported: " + tally + " users<br/>" + (importErrors == true ? "Import errors: " + (totalEntries - tally) + " users skipped" : "") : "Users already exist or something went wrong. Please check logs!";

            resp.setContentType("application/json");
            resp.setStatus(tally > 0 ? 200 : 302);

            resp.getWriter().write("{\"msg\":\"" + msg + "\"}");
        }else {
            resp.setContentType("application/json");
            resp.setStatus(302);
            resp.getWriter().write("{\"msg\":\"Invalid file.\"}");
        }
    }

    private void createUser(HashMap<String, Value> details) {
        try {
            String eMail = details.get("email").getString();

            if(eMail != null && eMail.length() > 0 && userMngr.getAuthorizable(eMail) == null && userMngr.getAuthorizable(groupID) != null) {
                User user = userMngr.createUser(eMail, "+Q#UNg#hj$:yRV3k" + random.nextInt(1000) + "aem"); // Create the user with a *somewhat* random password

                for (Map.Entry<String, Value> entry : details.entrySet()) {
                    if(entry.getKey().equalsIgnoreCase("gender")){
                        entry.setValue(entry.getValue().getString().equalsIgnoreCase("mr") ? valueFactory.createValue("male") : valueFactory.createValue("female")); // Change the value of the gender to match AEM structure
                    }

                    if(entry.getKey().equalsIgnoreCase("country")) {
                        user.setProperty("./profile/countryCode", entry.getValue());
                        entry.setValue(valueFactory.createValue(new Locale("English", entry.getValue().getString()).getDisplayCountry()));
                    }

                    user.setProperty("./profile/" + entry.getKey(), entry.getValue()); // Set user profile properties
                }

                Group userGroup = (Group) userMngr.getAuthorizable(groupID);
                userGroup.addMember(userMngr.getAuthorizable(eMail)); // Add user to group
                session.save(); // Save user data
                logger.info("Successfully imported user " + eMail);
                tally++;
            }else {
                logger.warn("Could not import user, the user already exists, the groupID " + groupID + " is invalid or invalid user data.");
                importErrors = true;
            }
        }catch(Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

}