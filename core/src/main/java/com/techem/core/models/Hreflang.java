package com.techem.core.models;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageFilter;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import java.util.*;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Hreflang {

    @SlingObject
    private Resource resource;

    @SlingObject
    private ResourceResolver resourceResolver;

    private Map<String, String> hreflangDetails;

    public void setHreflangLink(){

        hreflangDetails = new HashMap<>();

        String currentPagePath = resource.getPath().replaceAll("/jcr:content","");
        PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
        Page currentPage = pageManager.getPage(currentPagePath);

        /*
            Avoid getting a 500 response while editing the header experience fragment.
            Returning nothing while editing the fragment itself should be fine -> editors will see a preview until they add the fragment to a page.
            (getAbsoluteParent -> line 124 returns null no matter what, probably because the header's parents are folders and not pages)
        */
        if(currentPage.getPath().startsWith("/content/experience-fragments")) {
            return;
        }

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
                                hreflangDetails.put(setKeyHreflang(currentSearch),setURL(currentSearch));
                            }
                            break;
                        }
                    }

                }
                if (translatedPathPieces.size() == 0) {
                    Boolean hasTranslationID = getTranslationID(currentRootPage) != null;
                    if (hasTranslationID && getTranslationID(currentPage).equals(getTranslationID(currentRootPage))) {
                        hreflangDetails.put(setKeyHreflang(currentRootPage),setURL(currentRootPage));
                    }

                }
            }
        }
    }

    public Map<String, String> getHreflangDetails() {
        setHreflangLink();
        return hreflangDetails;
    }

    private String getRegion (com.day.cq.wcm.api.Page page) {
        return page.getPath().split("/")[3];
    }

    private String getLang (com.day.cq.wcm.api.Page page) {
        String langCode = page.getPath().split("/")[4];
        if(langCode.equals("dk")){
            langCode = "da";
        }
        if(langCode.equals("cz")) {
            langCode = "cs";
        }
        return langCode;
    }

    private String setKeyHreflang (com.day.cq.wcm.api.Page page){
        if(getRegion(page).equals("corp"))
            return "x-default";
        else
            return getLang(page) + "-" + getRegion(page);
    }

    private String setURL (com.day.cq.wcm.api.Page page) {
        String url = "https://www.techem.com/" + page.getPath().replaceAll("/content/techem/", "");;
        return url;
    }

    private List<String> getTranslatedPathPieces(com.day.cq.wcm.api.Page currentPage, String currentPagePath, PageManager pageManager) {

        List<String> translatedPathPieces = new ArrayList<>();
        List<String> pathPiecesToGetTranslationIDs = Arrays.asList(Arrays.copyOfRange(currentPagePath.split("/"), 5, currentPagePath.split("/").length));

        String absoluteParent = currentPage.getAbsoluteParent(3).getPath(); // content/techem/tara/limba
        String provisoryPath = absoluteParent;

        for (String piece : pathPiecesToGetTranslationIDs) {
            provisoryPath = provisoryPath + "/" + piece;
            com.day.cq.wcm.api.Page provisoryPage = pageManager.getPage(provisoryPath);

            if (getTranslationID(provisoryPage)!= null) {
                translatedPathPieces.add(getTranslationID(provisoryPage));
            }
            else {
                return null;
            }
        }

        return translatedPathPieces;
    }

    private String getTranslationID (com.day.cq.wcm.api.Page page) {
        Object translationID = page.getProperties().get("translationID");
        if (translationID != null)
            return translationID.toString().replaceAll("\\s","");
        else
            return null;
    }
}