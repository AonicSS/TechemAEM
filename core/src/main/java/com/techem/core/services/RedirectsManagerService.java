package com.techem.core.services;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationException;
import com.day.cq.search.result.SearchResult;
import com.techem.core.models.RedirectRule;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
    Interface for the Redirects Manager Service.<p>
    The Redirects Manager Service offers a wide array of functionality, from creating and editing, to finding, filtering and removing rules.
*/
public interface RedirectsManagerService {

        /** Path pointing to the root directory of the redirect rules. */
        public static final String REDIRECTS_ROOT_DEFAULT = "/etc/redirects";
        /** The <b>from</b> property of the rule. */
        public static final String REDIRECT_FROM = "from";
        /** The <b>to</b> property of the rule. */
        public static final String REDIRECT_TO = "to";
        /** The <b>until</b> property of the rule. */
        public static final String REDIRECT_UNTIL = "until";
        /** The <b>market</b> property of the rule. */
        public static final String REDIRECT_MARKET = "market";
        /** The <b>code</b> property of the rule. */
        public static final String REDIRECT_CODE = "code";
        /** The <b>status code</b> property of the rule. */
        public static final String REDIRECT_STATUS = "statusCode";
        /** The <b>keep query string</b> property of the rule. */
        public static final String REDIRECT_KEEP_QS = "keepQS";
        /** The <b>last checked date</b> property of the rule. */
        public static final String REDIRECT_STATUS_LAST_CHECKED = "statusDate";
        /** The <b>published status</b> property of the rule. */
        public static final String REDIRECT_PUBLISHED = "published";
        /** The <b>published by</b> property of the rule. */
        public static final String REDIRECT_PUBLISHED_BY = "publishedBy";
        /** The <b>published date</b> property of the rule. */
        public static final String REDIRECT_PUBLISHED_DATE = "publishDate";
        /** The resource type of a rule. */
        public static final String REDIRECT_ENTRY_RES_TYPE = "techem/components/redirects-manager/redirects-entry";
        /** Path from where content is being routed. This is also defined in the dispatcher rewrite.rules file. */
        public static final String REDIRECT_CONTENT_PATH = "/content/techem/content";
        public static final String REDIRECT_GLOBAL_IDENTIFIER = REDIRECT_CONTENT_PATH + "/redir";
        public static final String REDIRECT_GLOBAL_IDENTIFIER_QS = "gURL";
        public static final String REDIRECT_GLOBAL_LOCATION = "https://www.techem.com";

        /** Configuration for the Redirects Manager Service. */
        @ObjectClassDefinition(name = "Redirect Manager Config", description = "Configures the behaviour of the redirects manager.")
        public @interface RedirectsManagerServiceConfig {

            @AttributeDefinition(
                name = "Enabled",
                description = "Enabled/Disable Redirect Manager.",
                type = AttributeType.BOOLEAN
            )
            boolean enabled() default false;

            @AttributeDefinition(
                name = "Path",
                description = "Location of the redirects.",
                type = AttributeType.STRING
            )
            String path() default "/etc/redirects";
            
            @AttributeDefinition(
                name = "Exceptions",
                description = "Paths that will be ignored by the manager.",
                type = AttributeType.STRING
            )
            String[] exceptions() default "";
        }

        /**
            Creates a single rule, commits it to the repo and optionally, validates the destination path of the rule. <p>
            The name convention of the rule is as follows: <code>rule_ + URL encoded path (UTF-8)</code><p>
            This name convention enables rules to be matched directly by path, instead of looking for them in the repo. This highly improves execution times.
        */
        public void createRule(SlingHttpServletRequest req) throws PersistenceException, UnsupportedEncodingException;
        /**
            Reads an Excel file containing redirects and commits them to the repo. 
        */
        public List<Integer> importRules(SlingHttpServletRequest req) throws IOException, ServletException;
        /**
            Generates an <b>XSSFWorkbook</b> object containing a list of given redirects.
            @return <code>XSSFWorkbook</code> for the given redirects.
        */
        public XSSFWorkbook exportRules(SlingHttpServletRequest req); 
        /**
            Modifies the properties of an existing rule, commiting the changes to the repo.
        */
        public void editRule(SlingHttpServletRequest req) throws PersistenceException, UnsupportedEncodingException;
        /**
            Removes multiple rules from the repo.
        */
        public void deleteRules(SlingHttpServletRequest req) throws PersistenceException;
        /**
            Creates multiple rules, commits them to the repo and optionally, validates the destination path of the rule. <p>
            The name convention of the redirects is as follows: <code>rule_ + URL encoded path (UTF-8)</code><p>
            This name convention enables rules to be matched directly by path, instead of looking for them in the repo. This highly improves execution times.
        */
        public void createBatchRules(List<RedirectRule> rules, boolean checkRules) throws PersistenceException, UnsupportedEncodingException;
        /**
            Starts replication to the publish environment for the given rules.
        */
        public void replicateRules(ReplicationActionType type, SlingHttpServletRequest req) throws ReplicationException;
        /**
            Starts the Async check of the destination property of multiple rules. 
        */
        public void checkRules(SlingHttpServletRequest req) throws IOException;
        /**
            Gets a single rule from the given path.
    
            @param relPath The relative path to the rule, eg: /rule_test.
            @see RedirectRule
            @return <code>RedirectRule</code> from the given path.
        */
        public RedirectRule getRule(String path) throws UnsupportedEncodingException;
        /**
            Returns a list of {@link RedirectRule}s, containing all rules found in the repo.
            @return <code>List</code> of all existing <b>RedirectRules</b>.
        */
        public List<RedirectRule> getRules();
        /**
            Returns a list of {@link RedirectRule}s, filtering the rules based on their <b>market</b> property.
            @return <code>List</code> of filtered <b>RedirectRules</b>.
        */
        public List<RedirectRule> getRules(String filter);
        /**
            Executes a predicate-based query on the location of the redirects, looking for any matches.
    
            @return <code>SearchResult</code> of the query.
        */
        public SearchResult findRules(ResourceResolver resResolver, Map<String, String> predicateMap);
        /**
            Fetches a list of all available markets. <p>
    
            @param rulesList A list of rules to get the markets from.
            @see RedirectRule
            @return <code>List</code> of Strings containing all markets found.
        */
        public List<String> getMarkets(List<RedirectRule> rulesList);
        /**
            Fetches a list of all available markets. <p>
    
            @param rulesList A list of rules to get the markets from.
            @see RedirectRule
            @return <code>List</code> of ValueMapResources containing all markets found.
        */
        public List<Resource> getMarketsIterator(SlingHttpServletRequest req, List<RedirectRule> rulesList);
        /**
            Checks if the Redirects Manager is enabled or not.
            @return <code>boolean</code> <b>true</b> if the redirect manager is enabled, <b>false</b> otherwise.
        */
        public boolean isEnabled();
        /**
            Returns the PID of the Redirect Manager.
            @return <code>String</code> the Class Name of the Redirects Manager.
        */
        public String getPID();
        /**
            Returns the config of the Redirects Manager.
    
            @see RedirectsManagerServiceConfig
            @return <code>RedirectsManagerServiceConfig</code> the config of the RedirectsManager.
        */
        public RedirectsManagerServiceConfig getConfig();
}
