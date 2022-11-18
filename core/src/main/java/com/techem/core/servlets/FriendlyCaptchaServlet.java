package com.techem.core.servlets;

import com.adobe.granite.ui.components.ds.DataSource;
import com.adobe.granite.ui.components.ds.SimpleDataSource;
import com.adobe.granite.ui.components.ds.ValueMapResource;
import com.day.cq.commons.jcr.JcrConstants;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.techem.core.services.FriendlyCaptchaService;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;

import javax.jcr.Node;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component(service = Servlet.class, property = { "sling.servlet.methods={ " + HttpConstants.METHOD_POST + "}", "sling.servlet.paths=/eu/techem/friendlycaptcha" })
@ServiceDescription("FriendlyCaptcha Validation Servlet")
public class FriendlyCaptchaServlet extends SlingAllMethodsServlet {

    @Reference
    private transient FriendlyCaptchaService fCaptchaService;

    @Override
    protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp) throws ServletException, IOException {
        if(fCaptchaService == null) { return; }
        
        Resource reqRes = req.getResource();
        boolean isLangList = reqRes.getValueMap().get(FriendlyCaptchaService.FC_LANG_LIST) != null && reqRes.getValueMap().get(FriendlyCaptchaService.FC_LANG_LIST).equals("true");

        if(!isLangList) { return; }

        ResourceResolver resResolver = req.getResourceResolver();
        Resource jsonRes = resResolver.getResource(FriendlyCaptchaService.FC_LANG_PATH);

        if(jsonRes == null) { return; }

        try {
            String jsonData = jsonRes.getChild(JcrConstants.JCR_CONTENT).adaptTo(Node.class).getProperty(JcrConstants.JCR_DATA).getString();
            JsonArray jsonObject = new Gson().fromJson(jsonData, JsonElement.class).getAsJsonArray();
            List<Resource> values = new ArrayList<Resource>();

            for(JsonElement el : jsonObject) {
                ValueMap vm = new ValueMapDecorator(new HashMap<String, Object>());
                vm.put("text", el.getAsJsonObject().getAsJsonPrimitive("text").getAsString());
                vm.put("value", el.getAsJsonObject().getAsJsonPrimitive("value").getAsString());
                values.add(new ValueMapResource(req.getResourceResolver(), jsonRes.getPath(), jsonRes.getResourceType(), vm));
            }

            DataSource ds = new SimpleDataSource(values.iterator());
            req.setAttribute(DataSource.class.getName(), ds);
        } catch (Exception e) {}
    }

    @Override
    protected void doPost(SlingHttpServletRequest req, SlingHttpServletResponse resp) throws ServletException, IOException {
        
        if(fCaptchaService == null) { return; }

        String solData = req.getParameter(FriendlyCaptchaService.FCC_SOLUTION);
        boolean isValidated = false;

        if(StringUtils.isNotBlank(solData)) {
            isValidated = fCaptchaService.validateCaptcha(solData);
        }

        resp.setStatus(isValidated ? 200 : 302);
    }
}
