package com.techem.core.services;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

public interface SiteMapService {

    public static final String NAME_SPACE = "http://www.sitemaps.org/schemas/sitemap/0.9";
    public static final String CONTENT_TYPE = "application/xml";
    public static final String SITEMAP_INDEX = "sitemapindex";
    public static final String SITEMAP_URLSET = "urlset";
    public static final String SITEMAP_URLELEMENT = "url";
    public static final String SITEMAP_LOCELEMENT = "loc";
    public static final String SITEMAP_ELEMENT = "sitemap";
    public static final String SITEMAP_CHANGEFREQ = "changefreq";
    public static final String SITEMAP_PRIORITY = "priority";
    public static final String SITEMAP_LASTMOD = "lastmod";

    @ObjectClassDefinition(
            name = "Techem Sitemap Config",
            description = "Configure the SiteMap Servlet")
    public @interface SiteMapServiceConfig {

        @AttributeDefinition(
                name = "Sling Resource Types",
                description = "Sling Resource Type for the Home Page component or components.",
                type = AttributeType.STRING)
        public String[] resourceTypes();

        @AttributeDefinition(
                name = "Externalizer Domain",
                description = "Must correspond to a configuration of the Externalizer component. If blank the externalization will prepend the current request's scheme combined with the current request's host header.",
                type = AttributeType.STRING)
        public String externalizerDomain();

        @AttributeDefinition(
                name = "Include Last Modified",
                description = "If true, the last modified value will be included in the sitemap.",
                type = AttributeType.BOOLEAN)
        public boolean includeLastMod();

        @AttributeDefinition(
                name = "Change Frequency Properties",
                description = "The set of JCR property names which will contain the change frequency value.",
                type = AttributeType.STRING)
        public String[] changeFreq();

        @AttributeDefinition(
                name = "Priority Properties",
                description = "Sling Resource Type for the Home Page component or components.",
                type = AttributeType.STRING)
        public String[] priority();

        @AttributeDefinition(
                name = "DAM Folder Property",
                description = "The JCR property name which will contain DAM folders to include in the sitemap.",
                type = AttributeType.STRING)
        public String damAssets();

        @AttributeDefinition(
                name = "DAM Asset MIME Types",
                description = "MIME types allowed for DAM assets.",
                type = AttributeType.STRING)
        public String[] damAssetsMIMEs();

        @AttributeDefinition(
                name = "Exclude Pages (by properties of boolean values) from Sitemap Property",
                description = "The boolean [cq:Page]/jcr:content property name which indicates if the Page should be hidden from the Sitemap.",
                type = AttributeType.STRING)
        public String[] exclude();

        @AttributeDefinition(
                name = "URL Rewrites",
                description = "Colon separated URL rewrites to adjust the <loc> to match your dispatcher's apache rewrites.",
                type = AttributeType.STRING)
        public String[] rewrites();

        @AttributeDefinition(
                name = "Include Inherit Value",
                description = "If true searches for the frequency and priority attribute in the current page if null looks in the parent.",
                type = AttributeType.BOOLEAN)
        public boolean includeInherit();

        @AttributeDefinition(
                name = "Extensionless URLs",
                description = "If true, page links included in sitemap are generated without .html extension and the path is included with a trailing slash, e.g. /content/geometrixx/en/.",
                type = AttributeType.BOOLEAN)
        public boolean extensionless();

        @AttributeDefinition(
                name = "Remove Trailing Slash from Extensionless URLs",
                description = "Only relevant if Extensionless URLs is selected. If true, the trailing slash is removed from extensionless page links, e.g. /content/geometrixx/en.",
                type = AttributeType.BOOLEAN)
        public boolean removeTrailing();

        @AttributeDefinition(
                name = "Character Encoding",
                description = "If not set, the container's default is used (ISO-8859-1 for Jetty).",
                defaultValue = "UTF-8",
                type = AttributeType.STRING)
        public String encoding();

        @AttributeDefinition(
                name = "Exclude Pages (by Template) from Sitemap",
                description = "Excludes pages that have a matching value at [cq:Page]/jcr:content@cq:Template",
                type = AttributeType.STRING)
        public String[] excludeTemplates();

        @AttributeDefinition(
                name = "Exclude Redirect Pages",
                description = "Will exclude any page that are set up as redirects within AEM (does not apply to dispatcher redirects).",
                type = AttributeType.BOOLEAN)
        public boolean excludeRedirects();

        @AttributeDefinition(
                name = "Use Vanity URLs",
                description = "Use the Vanity URL for generating the Page URL",
                type = AttributeType.BOOLEAN)
        public boolean useVanity();

        @AttributeDefinition(
                name = "Root path of the sitemap index",
                description = "Define the default base path of the Sitemap index. E.g. /content/techem will include all children sitemaps (de.sitemap, it.sitemap and so on).",
                type = AttributeType.STRING,
                defaultValue = "/content/techem")
        public String siteMapIndexRoot();
    }

    public SiteMapServiceConfig getConfig();
}
