package com.techem.core.servlets;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageFilter;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.*;

@Component(service = Servlet.class)
@SlingServletResourceTypes(
        resourceTypes="techem/components/page",
        methods= "GET",
        extensions="html",
        selectors="hreflang")
public class HrefLangServlet extends SlingAllMethodsServlet {
    private static final Logger log = LoggerFactory.getLogger(HrefLangServlet.class);

    private String resultHTML = "";

    @Activate
    public void activate(ComponentContext context){
        log.info("%%%%%%%%%%%%%%$$$$$$$$$$$%%%%%%%**************: activated");
    }

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        final Resource resource = request.getResource();
        response.setContentType("text/plain");
//        response.getWriter().write("In DoGet");
        resultHTML = "";


        ResourceResolver res = resource.getResourceResolver();
        PageManager pageManager = resource.getResourceResolver().adaptTo(PageManager.class);
        Page currentPage = pageManager.getContainingPage(resource);
        String currentPagePath = currentPage.getPath();


        List<String> translatedPathPieces = getTranslatedPathPieces(currentPage, currentPagePath, pageManager);

        if (translatedPathPieces == null) {
            return;
        }

        //Parse through all countries
        Iterator<Page> childCountries = currentPage.getAbsoluteParent(1).listChildren(new PageFilter());
        while (childCountries.hasNext()) {
            Page childCountry = childCountries.next();
            Iterator<Page> childLanguages = childCountry.listChildren(new PageFilter());
            //Parse through all languages under a country
            while (childLanguages.hasNext()){
                Page childLanguage = childLanguages.next();
                Page currentRootPage = childLanguage;
                Iterator<Page> currentSearchIterator;
                //check if page exists under the same level with same translationIDs
                for (int i=0; i<translatedPathPieces.size(); i++) {

                    currentSearchIterator = currentRootPage.listChildren(new PageFilter());
                    while (currentSearchIterator != null && currentSearchIterator.hasNext()) {
                        Page currentSearch = currentSearchIterator.next();
                        Boolean hasTranslationID = getTranslationID(currentSearch) != null;
                        if (hasTranslationID && getTranslationID(currentSearch).equals(translatedPathPieces.get(i)) ){
                            currentRootPage = currentSearch;
                            if (i == translatedPathPieces.size()-1){
                                resultHTML = resultHTML.concat("<link rel='alternate' hreflang='" + setKeyHreflang(currentSearch) +"' href='" + setURL(currentSearch) + "'/>");
                            }
                            break;
                        }
                    }

                }
            }
        }
        response.getWriter().write(resultHTML);
    }

    private String getRegion (Page page) {
        return page.getPath().split("/")[3];
    }

    private String getLang (Page page) {
        return page.getPath().split("/")[4];
    }

    private String setKeyHreflang (Page page){
        if(getRegion(page).equals("corp"))
            return "x-default";
        else
            return getLang(page) + "-" + getRegion(page);
    }

    private String setURL (Page page) {
        String url = "https://techem.com/" + page.getPath().replaceAll("/content/techem/", "");;
        return url;
    }

    private List<String> getTranslatedPathPieces(Page currentPage, String currentPagePath, PageManager pageManager) {

        List<String> translatedPathPieces = new ArrayList<>();
        List<String> pathPiecesToGetTranslationIDs = Arrays.asList(Arrays.copyOfRange(currentPagePath.split("/"), 5, currentPagePath.split("/").length));

        String absoluteParent = currentPage.getAbsoluteParent(3).getPath(); // content/techem/tara/limba
        String provisoryPath = absoluteParent;

        for (String piece : pathPiecesToGetTranslationIDs) {
            provisoryPath = provisoryPath + "/" + piece;
            Page provisoryPage = pageManager.getPage(provisoryPath);

            if (getTranslationID(provisoryPage)!= null) {
                translatedPathPieces.add(getTranslationID(provisoryPage));
            }
            else {
                return null;
            }
        }

        return translatedPathPieces;
    }

    private String getTranslationID (Page page) {
        Object translationID = page.getProperties().get("translationID");
        if (translationID != null)
            return translationID.toString();
        else
            return null;
    }

}