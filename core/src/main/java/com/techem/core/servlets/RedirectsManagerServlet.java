package com.techem.core.servlets;

import lombok.extern.slf4j.Slf4j;
import org.osgi.framework.Constants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestParameter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import com.adobe.granite.ui.components.ds.DataSource;
import com.adobe.granite.ui.components.ds.SimpleDataSource;
import com.adobe.granite.ui.components.ds.ValueMapResource;
import com.day.cq.replication.ReplicationActionType;
import com.google.gson.Gson;
import com.techem.core.models.RedirectRule;
import com.techem.core.services.RedirectsManagerService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.sling.api.servlets.HttpConstants;

/**
    Servlet for the Redirects Manager. <p>
    This is used on the author-side, enabling the functionality of the Redirects Manager AEM console.
*/
@Slf4j
@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=Redirect Manager Utility.", "sling.servlet.methods={"  + HttpConstants.METHOD_GET + ", " + HttpConstants.METHOD_POST + "}", "sling.servlet.paths=/eu/techem/redirmngr" })
public class RedirectsManagerServlet extends SlingAllMethodsServlet {


    @Reference
    private transient RedirectsManagerService redirectManager;

    private static final long serialVersionUID = 1L;

    private static final String EXCELL_CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    @Override
    protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp) throws ServletException, IOException {
        
        if(redirectManager == null) { return; }

        String action = req.getParameter("action");

        /** Generate an Excell file containing all existing redirects. */
        if(action != null && action.equals("export")) {
            XSSFWorkbook redirectsWorkBook = redirectManager.exportRules(req);

            if(redirectsWorkBook != null) {
                resp.setContentType(EXCELL_CONTENT_TYPE);
                resp.setHeader("Content-Disposition", "attachment;filename=\"redirectsTechem.xlsx\"");
                redirectsWorkBook.write(resp.getOutputStream());
            }
            return;
        }
        
        List<RedirectRule> ruleList = redirectManager.getRules();
        if(req.getParameter("markets") != null) {
            List<String> mkts = redirectManager.getMarkets(ruleList);

            if(!mkts.isEmpty()) {
                String respJson = new Gson().toJson(mkts);
                resp.setContentType("application/json");
                resp.getWriter().write(respJson);
            }
            return;
        }

        try {
            Resource rResource = req.getResource();
            boolean isMarketsList = rResource.getValueMap().get("ds_type") != null && rResource.getValueMap().get("ds_type").equals("markets");
            String filterBy = req.getParameter("filterBy");
            List<Resource> values = new ArrayList<Resource>();

            if(isMarketsList) {
                values = redirectManager.getMarketsIterator(req, ruleList);
            }else {
                for(RedirectRule rule : ruleList) {
                
                    if(filterBy != null && !StringUtils.isBlank(filterBy) && (rule.getMarket() == null || rule.getMarket() != null && !rule.getMarket().equals(filterBy))) {
                        continue;
                    }
    
                    ValueMap vm = new ValueMapDecorator(new HashMap<String, Object>());
                    vm.put(RedirectsManagerService.REDIRECT_FROM, rule.getFrom());
                    vm.put(RedirectsManagerService.REDIRECT_TO, rule.getTo());
                    vm.put(RedirectsManagerService.REDIRECT_CODE, rule.getCode());
                    vm.put(RedirectsManagerService.REDIRECT_MARKET, rule.getMarket());
                    vm.put(RedirectsManagerService.REDIRECT_KEEP_QS, rule.getKeepQS());
                    vm.put(RedirectsManagerService.REDIRECT_PUBLISHED, rule.getPublished());
                    vm.put(RedirectsManagerService.REDIRECT_PUBLISHED_BY, rule.getPublishedBy());
                    vm.put(RedirectsManagerService.REDIRECT_PUBLISHED_DATE, rule.getPublishDate());
                    vm.put(RedirectsManagerService.REDIRECT_STATUS, rule.getStatusCode());
                    vm.put(RedirectsManagerService.REDIRECT_STATUS_LAST_CHECKED, rule.getStatusDate());
                    vm.put(RedirectsManagerService.REDIRECT_UNTIL, (rule.getUntil() != null) ? rule.getUntil() : "");
                    vm.put("path", rule.getPath());
                    values.add(new ValueMapResource(req.getResourceResolver(), rule.getPath(), RedirectsManagerService.REDIRECT_ENTRY_RES_TYPE, vm));
                }
            }

            DataSource ds = new SimpleDataSource(values.iterator());
            req.setAttribute(DataSource.class.getName(), ds);
        }catch (Exception e) {
            LOGGER.error("Could not complete GET request: ", e);
        }
    }

    @Override
    protected void doPost(final SlingHttpServletRequest req, final SlingHttpServletResponse resp) throws ServletException, IOException {

        if(redirectManager == null) { return; }

        try {
            switch(req.getParameter("action")) {
                case "delete":
                    redirectManager.deleteRules(req);
                break;
                case "edit":
                    redirectManager.editRule(req);
                break;
                case "create":
                   redirectManager.createRule(req);
                break;
                case "activate":
                    redirectManager.replicateRules(ReplicationActionType.ACTIVATE, req);
                break;
                case "deactivate":
                    redirectManager.replicateRules(ReplicationActionType.DEACTIVATE, req);
                break;
                case "import":
                    boolean multiPart = ServletFileUpload.isMultipartContent(req);
                    
                    RequestParameter file = req.getRequestParameter("importRulesData");
                    String mimeType = FilenameUtils.getExtension(file.getFileName());

                    if (multiPart && mimeType != null && mimeType.equalsIgnoreCase("xlsx")) {
                        List<Integer> res = redirectManager.importRules(req);

                        if(!res.isEmpty()) {
                            String respJson = new Gson().toJson(res);
                            resp.setContentType("application/json");
                            resp.getWriter().write(respJson);
                        }
                    }
                break;
                case "check":
                    redirectManager.checkRules(req);
                break;
                default:
                    resp.setStatus(200);
                break;
            }
        } catch(Exception e) {
            resp.sendError(410);
            LOGGER.error("Could not complete request. Exception follows: ", e);
        }
    }
}