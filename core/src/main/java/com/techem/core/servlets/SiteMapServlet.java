package com.techem.core.servlets;

import java.io.IOException;
import java.net.URI;
import java.util.*;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.PropertyUnbounded;
import org.apache.felix.scr.annotations.Reference;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.commons.osgi.PropertiesUtil;

import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.commons.Externalizer;
import com.day.cq.commons.inherit.HierarchyNodeInheritanceValueMap;
import com.day.cq.commons.inherit.InheritanceValueMap;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.DamConstants;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageFilter;
import com.day.cq.wcm.api.PageManager;

import org.osgi.service.component.annotations.Component;

@Designate(ocd = SiteMapServlet.Config.class)
@Component( service = Servlet.class, configurationPolicy = ConfigurationPolicy.REQUIRE, immediate = true)
@SlingServletResourceTypes(
        resourceTypes="techem/components/page",
        methods= HttpConstants.METHOD_GET,
        extensions="xml",
        selectors = "sitemap")
public final class SiteMapServlet extends SlingSafeMethodsServlet {

    private static final Logger log = LoggerFactory.getLogger(SiteMapServlet.class);

    private static final FastDateFormat DATE_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd");

    private static final boolean DEFAULT_INCLUDE_LAST_MODIFIED = true;

    private static final boolean DEFAULT_INCLUDE_INHERITANCE_VALUE = false;

    private static final String DEFAULT_EXTERNALIZER_DOMAIN = "local";

    private static final boolean DEFAULT_EXTENSIONLESS_URLS = false;

    private static final boolean DEFAULT_REMOVE_TRAILING_SLASH = false;

    private static final boolean DEFAULT_USE_VANITY_URL = true;

    private static final String DEFAULT_RESOURCE_TYPE = "techem/components/structure/section-page";

    private static final String DEFAULT_ENCODING = "UTF-8";

    private static final String DEFAULT_DAM_ASSETS_PROPERTY = "/content/dam/techem";

    private static final String NS = "http://www.sitemaps.org/schemas/sitemap/0.9";

    @Reference
    private transient Externalizer externalizer;

    private String externalizerDomain;

    private boolean includeInheritValue;

    private boolean includeLastModified;

    private String[] changefreqProperties;

    private String[] priorityProperties;

    private String damAssetProperty;

    private List<String> damAssetTypes;

    private List<String> excludeFromSiteMapProperty;

    private String characterEncoding;

    private boolean extensionlessUrls;

    private Map<String, String> urlRewrites;

    private boolean removeTrailingSlash;

    private List<String> excludedPageTemplates;

    private boolean useVanityUrl;

    private SiteMapServlet.Config siteMapConfig;

    @Activate
    @Modified
    protected void activate(final SiteMapServlet.Config siteMapConfig)
    {
        this.siteMapConfig = siteMapConfig;

        this.externalizerDomain = this.siteMapConfig.externalizer_domain();
        this.includeLastModified = this.siteMapConfig.include_lastmod();
        this.includeInheritValue = this.siteMapConfig.include_inherit();

        this.changefreqProperties = (this.siteMapConfig.changefreq_properties() != null) ? this.siteMapConfig.changefreq_properties() : new String[0];

        this.priorityProperties = (this.siteMapConfig.priority_properties() != null) ? this.siteMapConfig.priority_properties() : new String[0];

        this.damAssetProperty = this.siteMapConfig.damassets_property();
        this.damAssetTypes = (this.siteMapConfig.damassets_types() != null) ? Arrays.asList(this.siteMapConfig.damassets_types()) : Collections.EMPTY_LIST;

        this.characterEncoding = this.siteMapConfig.character_encoding();
        this.extensionlessUrls = this.siteMapConfig.extensionless_urls();
        this.removeTrailingSlash = this.siteMapConfig.remove_slash();

    }

    @ObjectClassDefinition(name = "Techem Sitemap test - Customized configuration from ACS COMMONS Sitemap for Techem", description = "This is the Configuration for Techem Sitemap, which was customized from ACS COMMONS")
    public @interface Config
    {
        @AttributeDefinition(name = "Sling Resource Type", description = "Sling Resource Type for the Home Page component or components.")
        String[] sling_servlet_resourceTypes() default DEFAULT_RESOURCE_TYPE;

        @AttributeDefinition(name = "Externalizer Domain", description = "Must correspond to a configuration of the Externalizer component.")
        String externalizer_domain() default DEFAULT_EXTERNALIZER_DOMAIN;

        @AttributeDefinition(name = "Include Last Modified", description = "If true, the last modified value will be included in the sitemap.")
        boolean include_lastmod() default DEFAULT_INCLUDE_LAST_MODIFIED;

        @AttributeDefinition(name = "Change Frequency Properties", description = "The set of JCR property names which will contain the change frequency value.")
        String[] changefreq_properties();

        @AttributeDefinition(name = "Priority Properties", description = "The set of JCR property names which will contain the priority value.")
        String[] priority_properties();

        @AttributeDefinition(name = "DAM Folder Property", description = "The JCR property name which will contain DAM folders to include in the sitemap.")
        String damassets_property() default DEFAULT_DAM_ASSETS_PROPERTY;

        @AttributeDefinition(name = "DAM Asset MIME Types", description = "MIME types allowed for DAM assets.")
        String[] damassets_types() default StringUtils.EMPTY;

        @AttributeDefinition(name = "Exclude from Sitemap Properties", description = "The boolean [cq:Page]/jcr:content properties names, which indicates if the Page should be hidden from the Sitemap.")
        String[] exclude_properties();

        @AttributeDefinition(name = "Include Inherit Value", description = "If true searches for the frequency and priority attribute in the current page if null looks in the parent.")
        boolean include_inherit() default DEFAULT_INCLUDE_INHERITANCE_VALUE;

        @AttributeDefinition(name = "Extensionless URLs", description = "If true, page links included in sitemap are generated without .html extension and the path is included with a trailing slash, e.g. /content/geometrixx/en/.")
        boolean extensionless_urls() default DEFAULT_EXTENSIONLESS_URLS;

        @AttributeDefinition(name = "Remove Trailing Slash from Extensionless URLs", description = "Only relevant if Extensionless URLs is selected.  If true, the trailing slash is removed from extensionless page links, e.g. /content/geometrixx/en.")
        boolean remove_slash() default DEFAULT_REMOVE_TRAILING_SLASH;

        @AttributeDefinition(name = "Character Encoding", description = "If not set, the container's default is used (ISO-8859-1 for Jetty)")
        String character_encoding() default DEFAULT_ENCODING;

    }

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType(request.getResponseContentType());
        if (StringUtils.isNotEmpty(this.characterEncoding)) {
            response.setCharacterEncoding(characterEncoding);
        }
        ResourceResolver resourceResolver = request.getResourceResolver();
        PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
        Page page = pageManager.getContainingPage(request.getResource());

        XMLOutputFactory outputFactory = XMLOutputFactory.newFactory();
        XMLStreamWriter stream = null;
        try {
            stream = outputFactory.createXMLStreamWriter(response.getWriter());
            stream.writeStartDocument("1.0");

            stream.writeStartElement("", "urlset", NS);
            stream.writeNamespace("", NS);

            // first do the current page
            write(page, stream, request);

            for (Iterator<Page> children = page.listChildren(new PageFilter(false, true), true); children.hasNext();) {
                write(children.next(), stream, request);
            }

            if (damAssetTypes.size() > 0 && damAssetProperty.length() > 0) {
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
                try
                {
                    stream.close();
                }
                catch ( XMLStreamException e )
                {
                    log.warn("Can not close xml stream writer", e);
                }
            }
        }
    }

    private Collection<Resource> getAssetFolders(Page page, ResourceResolver resolver) {
        List<Resource> allAssetFolders = new ArrayList<Resource>();
        ValueMap properties = page.getProperties();
        String[] configuredAssetFolderPaths = properties.get(damAssetProperty, String[].class);
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

    @SuppressWarnings("squid:S1192")
    private void write(Page page, XMLStreamWriter stream, SlingHttpServletRequest request) throws XMLStreamException {
        if (isHiddenByPageProperty(page) || isHiddenByPageTemplate(page)) {
            return;
        }
        stream.writeStartElement(NS, "url");
        String loc = "";

        if (useVanityUrl && !StringUtils.isEmpty(page.getVanityUrl())) {
            loc = externalizeUri(request, page.getVanityUrl());
        } else if (!extensionlessUrls) {
            loc = externalizeUri(request, String.format("%s.html", page.getPath()));
        } else {
            String urlFormat = removeTrailingSlash ? "%s" : "%s/";
            loc = externalizeUri(request, String.format(urlFormat, page.getPath()));
        }

        loc = applyUrlRewrites(loc);

        writeElement(stream, "loc", loc);

        if (includeLastModified) {
            Calendar cal = page.getLastModified();
            if (cal != null) {
                writeElement(stream, "lastmod", DATE_FORMAT.format(cal));
            }
        }

        if (includeInheritValue) {
            HierarchyNodeInheritanceValueMap hierarchyNodeInheritanceValueMap = new HierarchyNodeInheritanceValueMap(
                    page.getContentResource());
            writeFirstPropertyValue(stream, "changefreq", changefreqProperties, hierarchyNodeInheritanceValueMap);
            writeFirstPropertyValue(stream, "priority", priorityProperties, hierarchyNodeInheritanceValueMap);
        } else {
            ValueMap properties = page.getProperties();
            writeFirstPropertyValue(stream, "changefreq", changefreqProperties, properties);
            writeFirstPropertyValue(stream, "priority", priorityProperties, properties);
        }

        stream.writeEndElement();
    }

    private boolean isHiddenByPageProperty(Page page){
        boolean flag = false;
        if(this.excludeFromSiteMapProperty != null){
            for(String pageProperty : this.excludeFromSiteMapProperty){
                flag = flag || page.getProperties().get(pageProperty, Boolean.FALSE);
            }
        }
        return flag;
    }

    private boolean isHiddenByPageTemplate(Page page) {
        boolean flag = false;
        if(this.excludedPageTemplates != null){
            for(String pageTemplate : this.excludedPageTemplates){
                flag = flag || page.getProperties().get("cq:template", StringUtils.EMPTY).equalsIgnoreCase(pageTemplate);
            }
        }
        return flag;
    }

    private String externalizeUri(SlingHttpServletRequest request, String path) {
        if (StringUtils.isNotBlank(externalizerDomain)) {
            return externalizer.externalLink(request.getResourceResolver(), externalizerDomain, path);
        } else {
            log.debug("No externalizer domain configured, take into account current host header {} and current scheme {}", request.getServerName(), request.getScheme());
            return externalizer.absoluteLink(request, request.getScheme(), path);
        }
    }

    private void writeAsset(Asset asset, XMLStreamWriter stream, SlingHttpServletRequest request) throws XMLStreamException {
        stream.writeStartElement(NS, "url");

        String loc = externalizeUri(request, asset.getPath());
        writeElement(stream, "loc", loc);

        if (includeLastModified) {
            long lastModified = asset.getLastModified();
            if (lastModified > 0) {
                writeElement(stream, "lastmod", DATE_FORMAT.format(lastModified));
            }
        }

        Resource contentResource = asset.adaptTo(Resource.class).getChild(JcrConstants.JCR_CONTENT);
        if (contentResource != null) {
            if (includeInheritValue) {
                HierarchyNodeInheritanceValueMap hierarchyNodeInheritanceValueMap = new HierarchyNodeInheritanceValueMap(
                        contentResource);
                writeFirstPropertyValue(stream, "changefreq", changefreqProperties, hierarchyNodeInheritanceValueMap);
                writeFirstPropertyValue(stream, "priority", priorityProperties, hierarchyNodeInheritanceValueMap);
            } else {
                ValueMap properties = contentResource.getValueMap();
                writeFirstPropertyValue(stream, "changefreq", changefreqProperties, properties);
                writeFirstPropertyValue(stream, "priority", priorityProperties, properties);
            }
        }

        stream.writeEndElement();
    }

    private void writeAssets(final XMLStreamWriter stream, final Resource assetFolder, final SlingHttpServletRequest request)
            throws XMLStreamException {
        for (Iterator<Resource> children = assetFolder.listChildren(); children.hasNext();) {
            Resource assetFolderChild = children.next();
            if (assetFolderChild.isResourceType(DamConstants.NT_DAM_ASSET)) {
                Asset asset = assetFolderChild.adaptTo(Asset.class);

                if (damAssetTypes.contains(asset.getMimeType())) {
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

    @SuppressWarnings("squid:S1144")
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
        stream.writeStartElement(NS, elementName);
        stream.writeCharacters(text);
        stream.writeEndElement();
    }

}