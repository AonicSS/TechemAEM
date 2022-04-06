package com.techem.core.services.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import javax.jcr.Session;
import javax.servlet.ServletException;
import javax.servlet.http.Part;

import com.adobe.granite.ui.components.ds.ValueMapResource;
import com.day.cq.commons.Externalizer;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationException;
import com.day.cq.replication.ReplicationOptions;
import com.day.cq.replication.Replicator;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.api.NameConstants;
import com.techem.core.models.RedirectRule;
import com.techem.core.servlets.ResourceResolverUtil;
import com.techem.core.services.RedirectsManagerService;
import com.techem.core.services.URLsFilterService;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
    The implementation of the {@link RedirectsManagerService}. <p>
    The Redirects Manager Service offers a wide array of functionality, from creating and editing, to finding, filtering and removing rules.
    @see RedirectsManagerService
*/
@Component(service = RedirectsManagerService.class, immediate = true)
@Designate(ocd = RedirectsManagerService.RedirectsManagerServiceConfig.class)
public class RedirectsManagerServiceImpl implements RedirectsManagerService {

	private RedirectsManagerService.RedirectsManagerServiceConfig redirectsCfg;

    private String REDIRECTS_LOCATION;

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Reference 
    private ResourceResolverFactory resourceResolverFactory; 

    @Reference
	private Replicator replicator;

	@Activate
	protected void activate(RedirectsManagerService.RedirectsManagerServiceConfig configuration) {
		redirectsCfg = configuration;
        REDIRECTS_LOCATION = (redirectsCfg.path() == null ? REDIRECTS_ROOT_DEFAULT : redirectsCfg.path());
	}

    @Modified
    protected void modified(RedirectsManagerService.RedirectsManagerServiceConfig configuration) {
        redirectsCfg = configuration;
        REDIRECTS_LOCATION = (redirectsCfg.path() == null ? REDIRECTS_ROOT_DEFAULT : redirectsCfg.path());
    }

    @Override
	public void createBatchRules(List<RedirectRule> rules, boolean checkRules) throws PersistenceException, UnsupportedEncodingException {
        
        ResourceResolver resResolver = ResourceResolverUtil.getResolver(resourceResolverFactory);

        if(resResolver == null) { return; }

        Resource parentRedirects = ResourceUtil.getOrCreateResource(resResolver, REDIRECTS_LOCATION, "sling:OrderedFolder", "sling:OrderedFolder", true);

        for (RedirectRule rule : rules) {

            String ruleFrom = rule.getFrom();
            String ruleName = "rule_" + URLEncoder.encode(ruleFrom.replaceAll("(\\?.*|#.*)", ""), "UTF-8").replaceAll("[[*]]+", "%2A");
            String ruleTo = rule.getTo();
            Date ruleUntil = rule.getUntil();
            String ruleMarket = rule.getMarket();
            boolean ruleKeepQS = rule.getKeepQS();
            int ruleCode = rule.getCode();

            Resource childRedirect = parentRedirects.getChild(ruleName);

            if(childRedirect != null) {
                resResolver.delete(childRedirect);
            }

            Map<String, Object> ruleProps = new HashMap<>();
            ruleProps.put(REDIRECT_FROM, ruleFrom);
            ruleProps.put(REDIRECT_TO, ruleTo);
            ruleProps.put(REDIRECT_CODE, ruleCode);
            ruleProps.put(REDIRECT_MARKET, ruleMarket);
            ruleProps.put(REDIRECT_KEEP_QS, ruleKeepQS);

            if(ruleUntil != null) {
                ruleProps.put(REDIRECT_UNTIL, ruleUntil);
            }

            ruleProps.put("sling:resourceType", REDIRECT_ENTRY_RES_TYPE);
            ruleProps.put(JcrConstants.JCR_PRIMARYTYPE, JcrConstants.NT_UNSTRUCTURED);
            ruleProps.put(JcrConstants.JCR_LASTMODIFIED, System.currentTimeMillis());
            ruleProps.entrySet().removeIf(entry -> entry.getValue() == null);
    
            resResolver.create(parentRedirects, ruleName, ruleProps);
        }
        resResolver.commit();
        if(checkRules) {
            try {
                checkRules(rules);
            }catch(Exception e) { logger.error("Could not check rules. E: ", e); }
        }
	}

	@Override
	public void createRule(SlingHttpServletRequest req) throws PersistenceException, UnsupportedEncodingException {

        ResourceResolver resResolver = req.getResourceResolver();

        if(resResolver == null) { return; }
        
        Resource parentRedirects = ResourceUtil.getOrCreateResource(resResolver, REDIRECTS_LOCATION, "sling:OrderedFolder", "sling:OrderedFolder", true);

        String ruleFrom = req.getParameter(REDIRECT_FROM);
        String ruleName = "rule_" + URLEncoder.encode(ruleFrom.replaceAll("(\\?.*|#.*)", ""), "UTF-8").replaceAll("[[*]]+", "%2A");
        String ruleTo = req.getParameter(REDIRECT_TO);
        String ruleUntil = req.getParameter(REDIRECT_UNTIL);
        String ruleMarket = req.getParameter(REDIRECT_MARKET);
        boolean ruleKeepQS = Boolean.parseBoolean(req.getParameter(REDIRECT_KEEP_QS));
        boolean checkRule = Boolean.parseBoolean(req.getParameter("checkRuleEdit"));
        int ruleCode = Integer.parseInt(req.getParameter(REDIRECT_CODE));

        Resource childRedirect = parentRedirects.getChild(ruleName);

        if(childRedirect != null) {
            resResolver.delete(childRedirect);
        }
    
        Map<String, Object> ruleProps = new HashMap<>();
        ruleProps.put(REDIRECT_FROM, ruleFrom);
        ruleProps.put(REDIRECT_TO, ruleTo);
        ruleProps.put(REDIRECT_CODE, ruleCode);
        ruleProps.put(REDIRECT_MARKET, ruleMarket);
        ruleProps.put(REDIRECT_KEEP_QS, ruleKeepQS);

        if(ruleUntil.length() > 0) {
            ruleProps.put(REDIRECT_UNTIL, ruleUntil);
        }

        ruleProps.put("sling:resourceType", REDIRECT_ENTRY_RES_TYPE);
        ruleProps.put(JcrConstants.JCR_PRIMARYTYPE, JcrConstants.NT_UNSTRUCTURED);
        ruleProps.put(JcrConstants.JCR_LASTMODIFIED, System.currentTimeMillis());
        ruleProps.put(JcrConstants.JCR_LAST_MODIFIED_BY, resResolver.getUserID());
        ruleProps.entrySet().removeIf(entry -> entry.getValue() == null);

        Resource ruleRes = resResolver.create(parentRedirects, ruleName, ruleProps);
        resResolver.commit();

        if(checkRule) {
            checkRuleURL(ruleRes.getPath());
        }
	}

	@Override
	public List<Integer> importRules(SlingHttpServletRequest req) throws IOException, ServletException {
        boolean checkRules = Boolean.parseBoolean(req.getParameter("importCheckRules"));
        Part filePart = req.getPart("importRulesData"); 
        InputStream fileContent = filePart.getInputStream();
        XSSFWorkbook workBook = new XSSFWorkbook(fileContent);
        XSSFSheet sheet = workBook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.iterator(); 
        List<RedirectRule> rules = new ArrayList<RedirectRule>();
        int countRow = 0;
        List<Integer> res = new ArrayList<Integer>();

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            countRow++;
            
            if(countRow == 1) { continue; }

            if(countRow > 1) {
                RedirectRule rule = new RedirectRule();
                Cell fromCell = row.getCell(0);
                Cell toCell = row.getCell(1);
                Cell codeCell = row.getCell(2);
                Cell dateCell = row.getCell(3);
                Cell marketCell = row.getCell(4);
                Cell keepQsCell = row.getCell(5);
                Date date = null;

                if (DateUtil.isCellDateFormatted(dateCell)) {
                    date = dateCell.getDateCellValue();
                }

                boolean isEmptyRow = false;
                int cErrors = 0;

                for (int i = 0; i < 3; i++) {
                    Cell cell = row.getCell(i);

                    if(cell != null && cell.getCellType() != CellType.BLANK) {
                        CellType cType = cell.getCellType();

                        /* Check if the 'to' and 'from' cells have values. If we get i = 2 here ('code' cell), then thats not ok either! */ 
                        if(cType == CellType.STRING && i != 2) {
                            String cVal = cell.getStringCellValue();

                            if(StringUtils.isNotBlank(cVal)) {
                                continue;
                            }
                        }

                        if(cType == CellType.NUMERIC) {
                            int cCode = (int)cell.getNumericCellValue();

                            if(cCode == 301 || cCode == 302 || cCode == 410) {
                                continue;
                            }
                        }

                    }

                    /* Allow empty 'to' cell if code is set to 410 */
                    if(i == 1) {
                        int code = codeCell != null ? (int)codeCell.getNumericCellValue() : 0;

                        if(code == 410) {
                            continue;
                        }
                    }

                    isEmptyRow = true;
                    cErrors++;
                }

                if(isEmptyRow) {
                    /* Dont report EMPTY rows */
                    if(cErrors < 3) {
                        logger.warn("Rule malformed, skipping. Row #{}", row.getRowNum()+1);
                        res.add(row.getRowNum()+1);
                    }
                    continue;
                }

                int ruleCode = (int)codeCell.getNumericCellValue();
                rule.setFrom(fromCell.getStringCellValue());
                rule.setTo(ruleCode == 410 ? "" : toCell.getStringCellValue());
                rule.setCode(ruleCode);
                rule.setUntil(date);

                if(marketCell != null) {
                    rule.setMarket(marketCell.getStringCellValue());
                }

                if(keepQsCell != null && keepQsCell.getCellType() != CellType.BOOLEAN) {
                    keepQsCell.setCellType(CellType.BOOLEAN);
                }

                rule.setKeepQS(keepQsCell != null ? keepQsCell.getBooleanCellValue() : false);
                rules.add(rule);
            }
        }

        workBook.close();

        if(!rules.isEmpty()) { createBatchRules(rules, checkRules); }

        return res;
	}

	@Override
	public void editRule(SlingHttpServletRequest req) throws PersistenceException, UnsupportedEncodingException {
        String path = req.getParameter("path");
        ResourceResolver resResolver = req.getResourceResolver();
        Resource ruleRes = resResolver.getResource(path);
        Resource rulesParent = resResolver.getResource(REDIRECTS_LOCATION);

        if(ruleRes == null) { return; }

        ModifiableValueMap ruleProps = ruleRes.adaptTo(ModifiableValueMap.class);
        String newFrom = req.getParameter(RedirectsManagerService.REDIRECT_FROM);
        String newName = "rule_" + URLEncoder.encode(newFrom.replaceAll("(\\?.*|#.*)", ""), "UTF-8").replaceAll("[[*]]+", "%2A");
        String newTo = req.getParameter(RedirectsManagerService.REDIRECT_TO);
        String newUntil = req.getParameter(RedirectsManagerService.REDIRECT_UNTIL);
        String newMarket = req.getParameter(RedirectsManagerService.REDIRECT_MARKET);
        boolean newKeepQS = Boolean.parseBoolean(req.getParameter(REDIRECT_KEEP_QS));
        boolean checkRule = Boolean.parseBoolean(req.getParameter("checkRuleEdit"));
        int newCode = Integer.parseInt(req.getParameter(RedirectsManagerService.REDIRECT_CODE));

        ruleProps.put(REDIRECT_FROM, newFrom);
        ruleProps.put(REDIRECT_TO, newTo == null ? "" : newTo);
        ruleProps.put(REDIRECT_CODE, newCode);

        if(newUntil != null && !newUntil.isEmpty()) {
            ruleProps.put(REDIRECT_UNTIL, newUntil);
        }else{
            ruleProps.remove(REDIRECT_UNTIL);
        }

        ruleProps.put(REDIRECT_MARKET, newMarket);
        ruleProps.put(REDIRECT_KEEP_QS, newKeepQS);

        ruleProps.put(JcrConstants.JCR_LASTMODIFIED, System.currentTimeMillis());
        ruleProps.put(JcrConstants.JCR_LAST_MODIFIED_BY, resResolver.getUserID());

        resResolver.delete(ruleRes);
        Resource editedRes = resResolver.create(rulesParent, newName, ruleProps);
        resResolver.commit();
        
        if(checkRule) {
            checkRuleURL(editedRes.getPath());
        }
	}

	@Override
	public void deleteRules(SlingHttpServletRequest req) throws PersistenceException {
        ResourceResolver resResolver = req.getResourceResolver();
        String pathsData = req.getParameter("paths");

        if(pathsData == null) { return; }

        String[] pathsSplit = pathsData.split(",");
        for(String path : pathsSplit) {
            Resource ruleRes = resResolver.getResource(path);

            if(ruleRes == null) { continue; }
            resResolver.delete(ruleRes);
        }
        resResolver.commit();
	}
    
    @Override
    public boolean isEnabled() {
        if(redirectsCfg != null) {
            return redirectsCfg.enabled();
        }
        return false;
    }

    @Override
    public String getPID() {
        if(redirectsCfg != null) {
            return this.getClass().getName();
        }
        return null;
    }

	@Override
	public List<RedirectRule> getRules() {

        ResourceResolver resourceResolver = ResourceResolverUtil.getResolver(resourceResolverFactory);
        
        Resource resParent = resourceResolver.getResource(REDIRECTS_LOCATION);
        if(resParent != null && resParent.hasChildren()) {
            List<RedirectRule> redirsList = new ArrayList<>();
            
            for(Resource res : resParent.getChildren()) {
                redirsList.add(res.adaptTo(RedirectRule.class));
            }

            return redirsList;
        }
        return Collections.emptyList();
	}

    @Override
	public List<RedirectRule> getRules(String filter) {

        ResourceResolver resourceResolver = ResourceResolverUtil.getResolver(resourceResolverFactory);
        
        Resource resParent = resourceResolver.getResource(REDIRECTS_LOCATION);
        if(resParent != null && resParent.hasChildren() && filter != null && !StringUtils.isBlank(filter)) {
            List<RedirectRule> redirsList = new ArrayList<>();
            
            for(Resource res : resParent.getChildren()) {
                RedirectRule rule = res.adaptTo(RedirectRule.class);
                
                if(rule.getMarket() != null && rule.getMarket().equals(filter)) {
                    redirsList.add(rule);
                }
            }

            return redirsList;
        }
        return Collections.emptyList();
	}

	@Override
	public void replicateRules(ReplicationActionType type, SlingHttpServletRequest req) throws ReplicationException {
        ResourceResolver resResolver = req.getResourceResolver();
        Session session = resResolver.adaptTo(Session.class);
        String pathsData = req.getParameter("paths");

        if(pathsData == null) { return; }

        String[] paths = pathsData.split(",");        
        replicator.replicate(session, type, paths, new ReplicationOptions());
	}

    @Override
    public XSSFWorkbook exportRules(SlingHttpServletRequest req) {
        String pathsData = req.getParameter("paths");

        if(pathsData == null) { return null; }

        ResourceResolver resResolver = req.getResourceResolver();
        List<RedirectRule> ruleList = new ArrayList<RedirectRule>();

        if(pathsData.equals("all")) {
            ruleList = getRules();
        } else {
            String[] paths = pathsData.split(",");
        
            for(String path : paths) {
                Resource ruleRes = resResolver.getResource(path);
    
                if(ruleRes == null) { continue; }
    
                ruleList.add(ruleRes.adaptTo(RedirectRule.class));
            }
        }

        if(ruleList.size() > 0) {
            XSSFWorkbook wb = new XSSFWorkbook();
            XSSFCellStyle headerStyle = wb.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            Font headerFont = wb.createFont();
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFont(headerFont);
    
            CellStyle dateStyle = wb.createCellStyle();
            dateStyle.setDataFormat(wb.createDataFormat().getFormat("mmm d, yyyy"));

            Row headerRow;
            int rownum = 0;
            Sheet sheet = wb.createSheet("Redirects");
            headerRow = sheet.createRow(rownum++);
            headerRow.createCell(0).setCellValue("From");
            headerRow.createCell(1).setCellValue("To");
            headerRow.createCell(2).setCellValue("Code");
            headerRow.createCell(3).setCellValue("Until");
            headerRow.createCell(4).setCellValue("Market");
            headerRow.createCell(5).setCellValue("Keep Query String");

            for (Cell cell : headerRow) {
                cell.setCellStyle(headerStyle);
            }
    
            for (RedirectRule rule : ruleList) {
                Row row = sheet.createRow(rownum++);
                row.createCell(0).setCellValue(rule.getFrom());
                row.createCell(1).setCellValue(rule.getTo());
                row.createCell(2).setCellValue(rule.getCode());
                Date date = rule.getUntil();

                if (date != null) {
                    Cell cell = row.createCell(3);
                    cell.setCellValue(date);
                    cell.setCellStyle(dateStyle);
                }
                row.createCell(4).setCellValue(rule.getMarket());
                row.createCell(5).setCellValue(rule.getKeepQS());
            }

            sheet.setAutoFilter(new CellRangeAddress(0, rownum - 1, 0, 2));
            sheet.setColumnWidth(0, 256 * 50);
            sheet.setColumnWidth(1, 256 * 50);
            sheet.setColumnWidth(2, 256 * 15);
            sheet.setColumnWidth(3, 256 * 12);
            sheet.setColumnWidth(4, 256 * 12);
            sheet.setColumnWidth(5, 256 * 18);

            return wb;
        }
        return null;
    }

    @Override
    public SearchResult findRules(ResourceResolver resResolver, Map<String, String> predicateMap) {
        QueryBuilder queryBuilder = resResolver.adaptTo(QueryBuilder.class);
        Session session = resResolver.adaptTo(Session.class);

        predicateMap.put("path", REDIRECTS_LOCATION);
        predicateMap.put("p.traversal", "ok");

        Query query = queryBuilder.createQuery(PredicateGroup.create(predicateMap), session);
        return query.getResult();
    }

    @Override
    public RedirectsManagerServiceConfig getConfig() {
        return redirectsCfg;
    }

    @Override
    public RedirectRule getRule(String relPath) throws UnsupportedEncodingException {
        ResourceResolver resourceResolver = ResourceResolverUtil.getResolver(resourceResolverFactory);

        Resource res = resourceResolver.getResource(REDIRECTS_LOCATION + "/" + relPath);
        if(res != null) {
            return res.adaptTo(RedirectRule.class);
        }
        return null;
    }

    @Override
    public void checkRules(SlingHttpServletRequest req) throws IOException {
        String pathsData = req.getParameter("paths");
        
        if(pathsData == null) { return; }

        String[] paths = pathsData.split(",");
        List<CompletableFuture<Void>> urlCheckers = new ArrayList<CompletableFuture<Void>>();

        for(String path : paths) {
            urlCheckers.add(CompletableFuture.runAsync(() -> {
                checkRuleURL(path);
            }));
        }
        urlCheckers.forEach(CompletableFuture::join); // Wait for all threads to finish before returning success msg to end user. 
    }

    /**
        Starts the Async check of the destination property of multiple rules.
    */
    private void checkRules(List<RedirectRule> rules) throws IOException {
        
        if(rules == null) { return; }

        List<CompletableFuture<Void>> urlCheckers = new ArrayList<CompletableFuture<Void>>();

        for(RedirectRule rule : rules) {
            urlCheckers.add(CompletableFuture.runAsync(() -> {
                try {
                    checkRuleURL(REDIRECTS_ROOT_DEFAULT + "/rule_" + URLEncoder.encode(rule.getFrom(), "UTF-8").replaceAll("[[*]]+", "%2A"));
                } catch (Exception e) {}
            }));
        }
        urlCheckers.forEach(CompletableFuture::join); // Wait for all threads to finish before returning success msg to end user. 
    }

    /**
        Checks the destination property of the given rule. <p>
        The property is checked by:

        <ul>
            <li>If the property is already a valid URL, validate the given URL</li>
            <li>If the property is a path and is <b>published</b>, generate a publisher URL and validate</li>
            <li>If the property is a path and is <b>NOT published</b>, check if there is any existing content on the given path</li>
        </ul>
        
        The result of the validation is saved as properties of the rule itself.
        @param path The absolute path to the rule, eg: /etc/redirects/rule_test.
        @see RedirectRule
        @return <code>RedirectRule</code> from the given path.
    */
    private void checkRuleURL(String path) {
        ResourceResolver resResolver = ResourceResolverUtil.getResolver(resourceResolverFactory);
        Resource ruleRes = resResolver.getResource(path);
        
        if(ruleRes == null) { return; }

        ModifiableValueMap ruleResVMap = ruleRes.adaptTo(ModifiableValueMap.class);

        if(ruleResVMap != null) {
            String rulePublished = (String) ruleResVMap.get(NameConstants.PN_PAGE_LAST_REPLICATION_ACTION);
            String ruleTo = (String) ruleResVMap.get(REDIRECT_TO);
            URL ruleURL = null;
            boolean isPublished = rulePublished != null && rulePublished.equals("Activate");
            int statusResponse = 0;

            try {
                if(ruleTo.startsWith("https://") || ruleTo.startsWith("http://")) {
                    ruleURL = new URL(ruleTo);
                }else if(isPublished) {
                    Externalizer externalizer = resResolver.adaptTo(Externalizer.class);
                    ruleURL = new URL(externalizer.publishLink(resResolver, ruleTo));
                }

                if(ruleURL != null) {
                    HttpURLConnection testConn = (HttpURLConnection) ruleURL.openConnection();
                    testConn.setRequestMethod("HEAD");
                    statusResponse = testConn.getResponseCode();
                }else {
                    boolean hasResource = resResolver.getResource(ruleTo) != null || resResolver.getResource(URLsFilterService.CONTENT_ROOT + ruleTo) != null;
                    statusResponse = hasResource ? 200 : 404;
                }
            }catch(Exception e) {
                logger.info("Rule URL malformed. Check failed. Rule ID: {}, Rule Destination: {}", path, ruleTo);
                statusResponse = 410;
            }

            if(statusResponse > 0) {
                ruleResVMap.put(REDIRECT_STATUS, statusResponse);
                ruleResVMap.put(REDIRECT_STATUS_LAST_CHECKED, new Date());

                try {
                    resResolver.commit();
                }catch(Exception e) {
                    logger.error("Could not save check data. Ex:", e);
                }

            }
        }
    }

    @Override
    public List<String> getMarkets(List<RedirectRule> rulesList) {
        if(rulesList.isEmpty()) { return Collections.emptyList(); }

        List<String> mktList = new ArrayList<>();
        for(RedirectRule rule : rulesList) {
            if(rule.getMarket() != null && !StringUtils.isBlank(rule.getMarket())) {
                if(!mktList.contains(rule.getMarket())) {
                    mktList.add(rule.getMarket());
                }
            }
        }
        return mktList;
    }

    @Override
    public List<Resource> getMarketsIterator(SlingHttpServletRequest req, List<RedirectRule> rulesList) {
        if(rulesList.isEmpty()) { return Collections.emptyList(); }

        ResourceResolver resResolver = req.getResourceResolver();
        List<Resource> marketsValues = new ArrayList<Resource>();
        for(RedirectRule rule : rulesList) {

            if(rule.getMarket() != null && !StringUtils.isBlank(rule.getMarket())) {
                ValueMap vm = new ValueMapDecorator(new HashMap<String, Object>());

                boolean isInList = marketsValues.stream().anyMatch(p -> p.getValueMap().get("text").equals(rule.getMarket()));
                if(!isInList) {
                    vm.put("text", rule.getMarket());
                    vm.put("value", rule.getMarket());
                    marketsValues.add(new ValueMapResource(resResolver, rule.getPath(), JcrConstants.NT_UNSTRUCTURED, vm));
                }
            }
        }
        return marketsValues;
    }
}