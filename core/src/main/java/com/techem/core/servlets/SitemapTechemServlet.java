package com.techem.core.servlets;

import com.day.cq.commons.Externalizer;
import com.day.cq.commons.inherit.HierarchyNodeInheritanceValueMap;
import com.day.cq.commons.inherit.InheritanceValueMap;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.DamConstants;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.*;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;
import java.util.*;

@Designate(ocd = SitemapTechemServlet.Config.class)
@Component(immediate = true, service = {Servlet.class}, configurationPolicy = ConfigurationPolicy.REQUIRE)
@SlingServletResourceTypes(resourceTypes = {}, selectors = {"sitemap"}, extensions = {"xml"}, methods = {HttpConstants.METHOD_GET})
public final class SitemapTechemServlet extends SlingSafeMethodsServlet {
    private static final Logger LOG = LoggerFactory.getLogger(SitemapTechemServlet.class);
    private SitemapPageFilter sitemapPageFilter;

    private static final long serialVersionUID = 6296104237376161899L;

    private static final FastDateFormat DATE_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd");

    private static final boolean DEFAULT_INCLUDE_LAST_MODIFIED = true;

    private static final boolean DEFAULT_INCLUDE_INHERITANCE_VALUE = false;

    private static final String DEFAULT_EXTERNALIZER_DOMAIN = "publish";

    private static final boolean DEFAULT_EXTENSIONLESS_URLS = false;

    private static final boolean DEFAULT_REMOVE_TRAILING_SLASH = false;

    private static final String NS = "http://www.sitemaps.org/schemas/sitemap/0.9";

    private static final String DEFAULT_DAM_ASSETS_FOLDER = "/content/dam/techem";

    private static final String DEFAULT_RESOURCE_TYPE ="/techem/components/page";

    @Reference
    private Externalizer externalizer;

    private String externalizerDomain;

    private boolean includeInheritValue;

    private boolean includeLastModified;

    private String[] changefreqProperties;

    private String[] priorityProperties;

    private String damAssetProperty;

    private List<String> damAssetTypes;

    private String[] excludeFromSiteMapProperties;

    private String characterEncoding;

    private boolean extensionlessUrls;

    private boolean removeTrailingSlash;

    private SitemapTechemServlet.Config siteMapConfig;

    private final XMLOutputFactory outputFactory = XMLOutputFactory.newFactory();

    private static final String CHANGEFREQ = "changefreq";
    private static final String LASTMOD = "lastmod";
    private static final String LOC = "loc";
    private static final String PRIORITY = "priority";

    private static final String URLSET = "urlset";
    private static final String URL = "url";

    @Activate
    @Modified
    protected void activate(final SitemapTechemServlet.Config siteMapConfig) {
        this.siteMapConfig = siteMapConfig;

        this.externalizerDomain = this.siteMapConfig.externalizer_domain();
        this.includeLastModified = this.siteMapConfig.include_lastmod();
        this.includeInheritValue = this.siteMapConfig.include_inherit();

        this.changefreqProperties = (this.siteMapConfig.changefreq_properties() != null) ? this.siteMapConfig.changefreq_properties() : new String[0];

        this.priorityProperties = (this.siteMapConfig.priority_properties() != null) ? this.siteMapConfig.priority_properties() : new String[0];

        this.damAssetProperty = this.siteMapConfig.damassets_property();
        this.damAssetTypes = (this.siteMapConfig.damassets_types() != null) ? Arrays.asList(this.siteMapConfig.damassets_types()) : Collections.EMPTY_LIST;

        this.excludeFromSiteMapProperties = (this.siteMapConfig.exclude_properties() != null) ? this.siteMapConfig.exclude_properties() : new String[0];
        this.sitemapPageFilter = new SitemapPageFilter(false, true, this.excludeFromSiteMapProperties);

        this.characterEncoding = this.siteMapConfig.character_encoding();
        this.extensionlessUrls = this.siteMapConfig.extensionless_urls();
        this.removeTrailingSlash = this.siteMapConfig.remove_slash();

    }

    @ObjectClassDefinition(name = "Techem Sitemap", description = "Configuration for Sitemap servlet")
    public @interface Config {
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
        String damassets_property() default DEFAULT_DAM_ASSETS_FOLDER;

        @AttributeDefinition(name = "DAM Asset MIME Types", description = "MIME types allowed for DAM assets.")
        String[] damassets_types();

        @AttributeDefinition(name = "Exclude from Sitemap Properties", description = "The boolean [cq:Page]/jcr:content properties names, which indicates if the Page should be hidden from the Sitemap.")
        String[] exclude_properties();

        @AttributeDefinition(name = "Include Inherit Value", description = "If true searches for the frequency and priority attribute in the current page if null looks in the parent.")
        boolean include_inherit() default DEFAULT_INCLUDE_INHERITANCE_VALUE;

        @AttributeDefinition(name = "Extensionless URLs", description = "If true, page links included in sitemap are generated without .html extension and the path is included with a trailing slash, e.g. /content/geometrixx/en/.")
        boolean extensionless_urls() default DEFAULT_EXTENSIONLESS_URLS;

        @AttributeDefinition(name = "Remove Trailing Slash from Extensionless URLs", description = "Only relevant if Extensionless URLs is selected.  If true, the trailing slash is removed from extensionless page links, e.g. /content/geometrixx/en.")
        boolean remove_slash() default DEFAULT_REMOVE_TRAILING_SLASH;

        @AttributeDefinition(name = "Character Encoding", description = "If not set, the container's default is used (ISO-8859-1 for Jetty)")
        String character_encoding();

    }

    @Override
    public void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response) throws ServletException, IOException {
        response.setContentType(request.getResponseContentType());
        if (StringUtils.isNotEmpty(this.characterEncoding)) {
            response.setCharacterEncoding(this.characterEncoding);
        }
        final ResourceResolver resourceResolver = request.getResourceResolver();
        final PageManager pageManager = resourceResolver.adaptTo(PageManager.class);

        if (pageManager != null) {
            final Page page = pageManager.getContainingPage(request.getResource());
            try {
                final XMLStreamWriter stream = this.outputFactory.createXMLStreamWriter(response.getWriter());
                stream.writeStartDocument("1.0");

                stream.writeStartElement(StringUtils.EMPTY, URLSET, NS);
                stream.writeNamespace(StringUtils.EMPTY, NS);

                // first do the current page
                this.write(page, stream, resourceResolver);

                for (final Iterator<Page> children = page.listChildren(this.sitemapPageFilter, true); children.hasNext(); ) {
                    this.write(children.next(), stream, resourceResolver);
                }

                if (!this.damAssetTypes.isEmpty() && this.damAssetProperty.length() > 0) {
                    for (final Resource assetFolder : this.getAssetFolders(page, resourceResolver)) {
                        this.writeAssets(stream, assetFolder, resourceResolver);
                    }
                }

                stream.writeEndElement();

                stream.writeEndDocument();

                stream.flush();
                stream.close();
            } catch (final XMLStreamException e) {
                throw new IOException(e);
            }
        }
    }

    private Collection<Resource> getAssetFolders(final Page page, final ResourceResolver resolver) {
        final List<Resource> allAssetFolders = new ArrayList<>();
        try {
            final ValueMap properties = page.getProperties();
            final String[] configuredAssetFolderPaths = properties.get(this.damAssetProperty, String[].class);
            if (configuredAssetFolderPaths != null) {
                // Sort to aid in removal of duplicate paths.
                Arrays.sort(configuredAssetFolderPaths);
                String prevPath = "#";
                for (final String configuredAssetFolderPath : configuredAssetFolderPaths) {
                    // Ensure that this folder is not a child folder of another
                    // configured folder, since it will already be included when
                    // the parent folder is traversed.
                    if (StringUtils.isNotBlank(configuredAssetFolderPath) && !configuredAssetFolderPath.equals(prevPath) && !StringUtils.startsWith(configuredAssetFolderPath, prevPath + "/")) {
                        final Resource assetFolder = resolver.getResource(configuredAssetFolderPath);
                        if (assetFolder != null) {
                            prevPath = configuredAssetFolderPath;
                            allAssetFolders.add(assetFolder);
                        }
                    }
                }
            }
        } catch (final Exception e) {
            LOG.error(" when getting Asset Folders: {}", e.getMessage());
        }
        return allAssetFolders;
    }

    private void write(final Page page, final XMLStreamWriter stream, final ResourceResolver resolver) throws XMLStreamException {
        // Check individual settings of SitemapPageFilter to exclude current page
        if (!this.sitemapPageFilter.isValidPageForSiteMap(page)) {
            return;
        }

        stream.writeStartElement(NS, URL);
        String loc = StringUtils.EMPTY;

        if (!this.extensionlessUrls) {
            loc = this.externalizer.externalLink(resolver, this.externalizerDomain, String.format("%s.html", page.getPath()));
        } else {
            final String urlFormat = this.removeTrailingSlash ? "%s" : "%s/";
            loc = this.externalizer.externalLink(resolver, this.externalizerDomain, String.format(urlFormat, page.getPath()));
        }

        this.writeElement(stream, LOC, loc);

        if (this.includeLastModified) {
            final Calendar cal = page.getLastModified();
            if (cal != null) {
                this.writeElement(stream, LASTMOD, DATE_FORMAT.format(cal));
            }
        }

        if (this.includeInheritValue) {
            final HierarchyNodeInheritanceValueMap hierarchyNodeInheritanceValueMap = new HierarchyNodeInheritanceValueMap(page.getContentResource());
            this.writeFirstPropertyValue(stream, CHANGEFREQ, this.changefreqProperties, hierarchyNodeInheritanceValueMap);
            this.writeFirstPropertyValue(stream, PRIORITY, this.priorityProperties, hierarchyNodeInheritanceValueMap);
        } else {
            final ValueMap properties = page.getProperties();
            this.writeFirstPropertyValue(stream, CHANGEFREQ, this.changefreqProperties, properties);
            this.writeFirstPropertyValue(stream, PRIORITY, this.priorityProperties, properties);
        }

        stream.writeEndElement();
    }

    private void writeAsset(final Asset asset, final XMLStreamWriter stream, final ResourceResolver resolver) throws XMLStreamException {
        stream.writeStartElement(NS, URL);

        final String loc = this.externalizer.externalLink(resolver, this.externalizerDomain, asset.getPath());
        this.writeElement(stream, LOC, loc);

        if (this.includeLastModified) {
            final long lastModified = asset.getLastModified();
            if (lastModified > 0) {
                this.writeElement(stream, LASTMOD, DATE_FORMAT.format(lastModified));
            }
        }

        final Resource contentResource = asset.adaptTo(Resource.class).getChild(JcrConstants.JCR_CONTENT);
        if (contentResource != null) {
            if (this.includeInheritValue) {
                final HierarchyNodeInheritanceValueMap hierarchyNodeInheritanceValueMap = new HierarchyNodeInheritanceValueMap(contentResource);
                this.writeFirstPropertyValue(stream, CHANGEFREQ, this.changefreqProperties, hierarchyNodeInheritanceValueMap);
                this.writeFirstPropertyValue(stream, PRIORITY, this.priorityProperties, hierarchyNodeInheritanceValueMap);
            } else {
                final ValueMap properties = contentResource.getValueMap();
                this.writeFirstPropertyValue(stream, CHANGEFREQ, this.changefreqProperties, properties);
                this.writeFirstPropertyValue(stream, PRIORITY, this.priorityProperties, properties);
            }
        }

        stream.writeEndElement();
    }

    private void writeAssets(final XMLStreamWriter stream, final Resource assetFolder, final ResourceResolver resolver) throws XMLStreamException {
        for (final Iterator<Resource> children = assetFolder.listChildren(); children.hasNext(); ) {
            final Resource assetFolderChild = children.next();
            if (assetFolderChild.isResourceType(DamConstants.NT_DAM_ASSET)) {
                final Asset asset = assetFolderChild.adaptTo(Asset.class);

                if (asset != null && this.damAssetTypes.contains(asset.getMimeType())) {
                    this.writeAsset(asset, stream, resolver);
                }
            } else {
                this.writeAssets(stream, assetFolderChild, resolver);
            }
        }
    }

    private void writeFirstPropertyValue(final XMLStreamWriter stream, final String elementName, final String[] propertyNames, final ValueMap properties) throws XMLStreamException {
        for (final String prop : propertyNames) {
            final String value = properties.get(prop, String.class);
            if (value != null) {
                this.writeElement(stream, elementName, value);
                break;
            }
        }
    }

    private void writeFirstPropertyValue(final XMLStreamWriter stream, final String elementName, final String[] propertyNames, final InheritanceValueMap properties) throws XMLStreamException {
        for (final String prop : propertyNames) {
            String value = properties.get(prop, String.class);
            if (value == null) {
                value = properties.getInherited(prop, String.class);
            }
            if (value != null) {
                this.writeElement(stream, elementName, value);
                break;
            }
        }
    }

    private void writeElement(final XMLStreamWriter stream, final String elementName, final String text) throws XMLStreamException {
        stream.writeStartElement(NS, elementName);
        stream.writeCharacters(text);
        stream.writeEndElement();
    }

}