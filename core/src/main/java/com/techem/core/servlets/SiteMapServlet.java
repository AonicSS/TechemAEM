package com.techem.core.servlets;

import com.day.cq.commons.Externalizer;
import com.day.cq.commons.inherit.HierarchyNodeInheritanceValueMap;
import com.day.cq.commons.inherit.InheritanceValueMap;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.DamConstants;
import com.day.cq.wcm.api.NameConstants;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageFilter;
import com.day.cq.wcm.api.PageManager;
import com.techem.core.services.SiteMapService;
import com.techem.core.services.SiteMapService.SiteMapServiceConfig;

import org.apache.commons.lang.time.FastDateFormat;
import org.apache.commons.lang3.StringUtils;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.osgi.service.component.propertytypes.ServiceRanking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.sling.api.servlets.HttpConstants;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Component(service = { Servlet.class }, configurationPolicy = ConfigurationPolicy.REQUIRE, configurationPid = "com.techem.core.services.impl.SiteMapServiceImpl")
@SlingServletResourceTypes(
        resourceTypes="techem/components/page",
        methods=HttpConstants.METHOD_GET,
        selectors = "sitemap",
        extensions="xml")
@SlingServletPaths({"/eu/techem/sitemap_index"})
@ServiceRanking(1000)
@ServiceDescription("Techem Sitemap")
public class SiteMapServlet extends SlingSafeMethodsServlet {

    private static final FastDateFormat DATE_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd");
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Reference
    private transient Externalizer externalizer;

    @Reference
    private SiteMapService siteMapService;

    private SiteMapServiceConfig siteMapConfig;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        if(siteMapService == null) { return; }

        siteMapConfig = siteMapService.getConfig();

        boolean isIndex = request.getPathInfo().equals("/sitemap.xml") || request.getPathInfo().equals("/eu/techem/sitemap_index");
        response.setContentType(SiteMapService.CONTENT_TYPE);

        if (StringUtils.isNotEmpty(siteMapConfig.encoding())) {
            response.setCharacterEncoding(siteMapConfig.encoding());
        }

        ResourceResolver resourceResolver = request.getResourceResolver();
        PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
        Page page = pageManager.getContainingPage(isIndex ? resourceResolver.getResource(siteMapConfig.siteMapIndexRoot()) : request.getResource());
        XMLOutputFactory outputFactory = XMLOutputFactory.newFactory();
        XMLStreamWriter stream = null;

        try {
            stream = outputFactory.createXMLStreamWriter(response.getWriter());
            stream.writeStartDocument("1.0");

            stream.writeStartElement("", isIndex ? SiteMapService.SITEMAP_INDEX : SiteMapService.SITEMAP_URLSET, SiteMapService.NAME_SPACE);
            stream.writeNamespace("", SiteMapService.NAME_SPACE);

            write(page, stream, request, isIndex);

            for (Iterator<Page> children = page.listChildren(new PageFilter(false, true), !isIndex); children.hasNext(); ) {
                write(children.next(), stream, request, isIndex);
            }

            if (!isIndex && siteMapConfig.damAssetsMIMEs().length > 0 && siteMapConfig.damAssets().length() > 0) {
                for (Resource assetFolder : getAssetFolders(page, resourceResolver)) {
                    writeAssets(stream, assetFolder, request);
                }
            }

            stream.writeEndElement();
            stream.writeEndDocument();
        } catch (XMLStreamException e) {
            throw new IOException(e);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (XMLStreamException e) {
                    log.warn("Can not close xml stream writer", e);
                }
            }
        }
    }

    private Collection<Resource> getAssetFolders(Page page, ResourceResolver resolver) {
        List<Resource> allAssetFolders = new ArrayList<Resource>();
        ValueMap properties = page.getProperties();
        String[] configuredAssetFolderPaths = properties.get(siteMapConfig.damAssets(), String[].class);
        if (configuredAssetFolderPaths != null) {
            // Sort to aid in removal of duplicate paths.
            Arrays.sort(configuredAssetFolderPaths);
            String prevPath = "#";
            for (String configuredAssetFolderPath : configuredAssetFolderPaths) {
                // Ensure that this folder is not a child folder of another
                // configured folder, since it will already be included when
                // the parent folder is traversed.
                if (StringUtils.isNotBlank(configuredAssetFolderPath) && !configuredAssetFolderPath.equals(prevPath)
                        && !StringUtils.startsWith(configuredAssetFolderPath, prevPath + "/")) {
                    Resource assetFolder = resolver.getResource(configuredAssetFolderPath);
                    if (assetFolder != null) {
                        prevPath = configuredAssetFolderPath;
                        allAssetFolders.add(assetFolder);
                    }
                }
            }
        }
        return allAssetFolders;
    }

    private String applyUrlRewrites(String url) {
        try {
            String path = URI.create(url).getPath();
            Map<String, String> urlRewrites = ParameterUtil.toMap(siteMapConfig.rewrites(), ":", true, "");

            for (Map.Entry<String, String> rewrite : urlRewrites.entrySet()) {
                if (path.startsWith(rewrite.getKey())) {
                    return url.replaceFirst(rewrite.getKey(), rewrite.getValue());
                }
            }
            return url;
        } catch (IllegalArgumentException e) {
            return url;
        }
    }

    private void write(Page page, XMLStreamWriter stream, SlingHttpServletRequest request, boolean isIndex) throws XMLStreamException {

        if (isHiddenByPageProperty(page) || isHiddenByPageTemplate(page) || !isIndex && isHiddenByPageRedirect(page)) {
            return;
        }

        stream.writeStartElement(SiteMapService.NAME_SPACE, isIndex ? SiteMapService.SITEMAP_ELEMENT : SiteMapService.SITEMAP_URLELEMENT);
        String loc = "";

        if (siteMapConfig.useVanity() && !StringUtils.isEmpty(page.getVanityUrl())) {
            loc = externalizeUri(request, page.getVanityUrl());
        } else if (!siteMapConfig.extensionless()) {
            loc = externalizeUri(request, String.format("%s.html", page.getPath()));
        } else {
            String urlFormat = siteMapConfig.removeTrailing() ? "%s" : "%s/";
            loc = externalizeUri(request, String.format(urlFormat, page.getPath()));
        }

        loc = applyUrlRewrites(loc);

        writeElement(stream, SiteMapService.SITEMAP_LOCELEMENT, loc + (isIndex ? ".sitemap.xml" : ""));

        if (siteMapConfig.includeLastMod()) {
            Calendar cal = page.getLastModified();
            if (cal != null) {
                writeElement(stream, SiteMapService.SITEMAP_LASTMOD, DATE_FORMAT.format(cal));
            }
        }

        if(!isIndex) {
            if (siteMapConfig.includeInherit()) {
                HierarchyNodeInheritanceValueMap hierarchyNodeInheritanceValueMap = new HierarchyNodeInheritanceValueMap(
                        page.getContentResource());
                writeFirstPropertyValue(stream, SiteMapService.SITEMAP_CHANGEFREQ, siteMapConfig.changeFreq(), hierarchyNodeInheritanceValueMap);
                writeFirstPropertyValue(stream, SiteMapService.SITEMAP_PRIORITY, siteMapConfig.priority(), hierarchyNodeInheritanceValueMap);
            } else {
                ValueMap properties = page.getProperties();
                writeFirstPropertyValue(stream, SiteMapService.SITEMAP_CHANGEFREQ, siteMapConfig.changeFreq(), properties);
                writeFirstPropertyValue(stream, SiteMapService.SITEMAP_PRIORITY, siteMapConfig.priority(), properties);
            }
        }

        stream.writeEndElement();
    }

    private boolean isHiddenByPageProperty(Page page) {
        boolean flag = false;
        if (siteMapConfig.exclude() != null) {
            for (String pageProperty : siteMapConfig.exclude()) {
                flag = flag || page.getProperties().get(pageProperty, Boolean.FALSE);
            }
        }
        return flag;
    }

    private boolean isHiddenByPageRedirect(Page page) {
        return siteMapConfig.excludeRedirects() && page.getProperties().get(NameConstants.PN_REDIRECT_TARGET) != null;
    }

    private boolean isHiddenByPageTemplate(Page page) {
        boolean flag = false;
        if (siteMapConfig.excludeTemplates() != null) {
            for (String pageTemplate : siteMapConfig.excludeTemplates()) {
                flag = flag || page.getProperties().get(NameConstants.NN_TEMPLATE, StringUtils.EMPTY).equalsIgnoreCase(pageTemplate);
            }
        }
        return flag;
    }

    private String externalizeUri(SlingHttpServletRequest request, String path) {
        if (StringUtils.isNotBlank(siteMapConfig.externalizerDomain())) {
            return externalizer.externalLink(request.getResourceResolver(), siteMapConfig.externalizerDomain(), path);
        } else {
            log.debug("No externalizer domain configured, take into account current host header {} and current scheme {}", request.getServerName(), request.getScheme());
            return externalizer.absoluteLink(request, request.getScheme(), path);
        }
    }

    private void writeAsset(Asset asset, XMLStreamWriter stream, SlingHttpServletRequest request) throws XMLStreamException {
        stream.writeStartElement(SiteMapService.NAME_SPACE, SiteMapService.SITEMAP_URLELEMENT);

        String loc = externalizeUri(request, asset.getPath());
        writeElement(stream, SiteMapService.SITEMAP_LOCELEMENT, loc);

        if (siteMapConfig.includeLastMod()) {
            long lastModified = asset.getLastModified();
            if (lastModified > 0) {
                writeElement(stream, SiteMapService.SITEMAP_LASTMOD, DATE_FORMAT.format(lastModified));
            }
        }

        Resource contentResource = asset.adaptTo(Resource.class).getChild(JcrConstants.JCR_CONTENT);
        if (contentResource != null) {
            if (siteMapConfig.includeInherit()) {
                HierarchyNodeInheritanceValueMap hierarchyNodeInheritanceValueMap = new HierarchyNodeInheritanceValueMap(
                        contentResource);
                writeFirstPropertyValue(stream, SiteMapService.SITEMAP_CHANGEFREQ, siteMapConfig.changeFreq(), hierarchyNodeInheritanceValueMap);
                writeFirstPropertyValue(stream, SiteMapService.SITEMAP_PRIORITY, siteMapConfig.priority(), hierarchyNodeInheritanceValueMap);
            } else {
                ValueMap properties = contentResource.getValueMap();
                writeFirstPropertyValue(stream, SiteMapService.SITEMAP_CHANGEFREQ, siteMapConfig.changeFreq(), properties);
                writeFirstPropertyValue(stream, SiteMapService.SITEMAP_PRIORITY, siteMapConfig.priority(), properties);
            }
        }

        stream.writeEndElement();
    }

    private void writeAssets(final XMLStreamWriter stream, final Resource assetFolder, final SlingHttpServletRequest request)
            throws XMLStreamException {
        for (Iterator<Resource> children = assetFolder.listChildren(); children.hasNext(); ) {
            Resource assetFolderChild = children.next();
            if (assetFolderChild.isResourceType(DamConstants.NT_DAM_ASSET)) {
                Asset asset = assetFolderChild.adaptTo(Asset.class);

                if (Arrays.asList(siteMapConfig.damAssetsMIMEs()).contains(asset.getMimeType())) {
                    writeAsset(asset, stream, request);
                }
            } else {
                writeAssets(stream, assetFolderChild, request);
            }
        }
    }

    private void writeFirstPropertyValue(final XMLStreamWriter stream, final String elementName,
                                         final String[] propertyNames, final ValueMap properties) throws XMLStreamException {
        for (String prop : propertyNames) {
            String value = properties.get(prop, String.class);
            if (value != null) {
                writeElement(stream, elementName, value);
                break;
            }
        }
    }

    private void writeFirstPropertyValue(final XMLStreamWriter stream, final String elementName,
                                         final String[] propertyNames, final InheritanceValueMap properties) throws XMLStreamException {
        for (String prop : propertyNames) {
            String value = properties.get(prop, String.class);
            if (value == null) {
                value = properties.getInherited(prop, String.class);
            }
            if (value != null) {
                writeElement(stream, elementName, value);
                break;
            }
        }
    }

    private void writeElement(final XMLStreamWriter stream, final String elementName, final String text)
            throws XMLStreamException {
        stream.writeStartElement(SiteMapService.NAME_SPACE, elementName);
        stream.writeCharacters(text);
        stream.writeEndElement();
    }

}