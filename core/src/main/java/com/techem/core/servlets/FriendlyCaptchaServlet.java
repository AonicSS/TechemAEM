package com.techem.core.servlets;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.net.ssl.HttpsURLConnection;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.*;
import java.net.URL;

@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=Servlet for verifying friendly captcha solution", "sling.servlet.methods="+ HttpConstants.METHOD_POST, "sling.servlet.paths=/eu/techem/friendlycaptcha" })
public class FriendlyCaptchaServlet extends SlingAllMethodsServlet {

    private Logger log = LoggerFactory.getLogger(FriendlyCaptchaServlet.class);

    private String postSolution = "false";

    private int responseCode = 500;

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    private String secret = "A19716EFHHG25O3RGTREUTRPLGQ2K5NIVU3OI1P3DOOM85LNHMSI4DBPU8";

    @Override
    protected void doPost( SlingHttpServletRequest req, SlingHttpServletResponse resp) throws ServletException, IOException {
        JsonReader jsonReader = null;

        try {
            String solution = req.getParameter("solution");

            if( req.getPathInfo().equals("/eu/techem/friendlycaptcha")) {
                URL obj = new URL("https://api.friendlycaptcha.com/api/v1/siteverify");
                HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

                // add reuqest header
                con.setRequestMethod("POST");
                con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

                String postParams = "solution=" + solution + "&secret="
                        + secret;

                // Send post request
                con.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(postParams);
                wr.flush();
                wr.close();

                responseCode = con.getResponseCode();

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                jsonReader = Json.createReader(new StringReader(response.toString()));
                JsonObject jsonObject = jsonReader.readObject();
                postSolution = jsonObject.get("success").toString();
                if (postSolution == "false")
                    log.error("FRIENDLY CAPTCHA SOLUTION FALSE: " + jsonObject.getString("details").toString());
            }
            else {
                if(postSolution == "true" || responseCode == 200)
                    resp.setStatus(200);
                else
                    resp.setStatus(500);
            }
        } catch (Exception e) {
            log.error("Post failed: ", e);
            resp.setStatus(302);
            return;
        } finally {
            if(jsonReader != null) {
                jsonReader.close();
            }
        }
    }
}