package io.jenkins.plugins.buildevents;

import com.cloudbees.plugins.credentials.CredentialsMatchers;
import com.cloudbees.plugins.credentials.CredentialsProvider;
import com.cloudbees.plugins.credentials.common.StandardCredentials;
import com.cloudbees.plugins.credentials.common.StandardListBoxModel;
import com.cloudbees.plugins.credentials.domains.DomainRequirement;
import hudson.Extension;
import hudson.ExtensionList;
import hudson.model.Item;
import hudson.security.ACL;
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;
import java.net.MalformedURLException;
import java.net.URL;
import jenkins.model.GlobalConfiguration;
import jenkins.model.Jenkins;
import lombok.Getter;
import org.jenkinsci.plugins.plaincredentials.StringCredentials;
import org.kohsuke.stapler.AncestorInPath;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the global configuration for the build events plugin.
 * This class allows setting credentials and API URL used by the plugin.
 */
@Getter
@Extension
public class BuildEventsConfiguration extends GlobalConfiguration {

    private static final Logger log = LoggerFactory.getLogger(BuildEventsConfiguration.class);
    private String buildEventsCredentialId;
    private String buildEventsApiUrl;

    /**
     * Retrieves the singleton instance of {@link BuildEventsConfiguration}.
     *
     * @return the {@link BuildEventsConfiguration} instance.
     */
    public static BuildEventsConfiguration get() {
        return ExtensionList.lookupSingleton(BuildEventsConfiguration.class);
    }

    /**
     * Constructor that loads any saved configuration from disk.
     */
    public BuildEventsConfiguration() {
        load();
    }

    /**
     * Sets the credential ID for accessing external services.
     *
     * @param buildEventsCredentialId the credential ID to be set.
     */
    @DataBoundSetter
    public void setBuildEventsCredentialId(String buildEventsCredentialId) {
        this.buildEventsCredentialId = buildEventsCredentialId;
        save();
    }

    /**
     * Sets the API URL used by the plugin.
     *
     * @param buildEventsApiUrl the API URL to be set.
     */
    @DataBoundSetter
    public void setBuildEventsApiUrl(String buildEventsApiUrl) {
        this.buildEventsApiUrl = buildEventsApiUrl;
        save();
    }

    /**
     * Populates the list box with available credentials.
     *
     * @param item the Jenkins item.
     * @param buildEventsCredentialId the current credential ID.
     * @return a {@link ListBoxModel} containing the available credentials.
     */
    public ListBoxModel doFillBuildEventsCredentialIdItems(
            @AncestorInPath Item item, @QueryParameter String buildEventsCredentialId) {
        StandardListBoxModel listBoxModel = new StandardListBoxModel();

        if (!Jenkins.get().hasPermission(Jenkins.ADMINISTER)) {
            return listBoxModel;
        }

        return listBoxModel
                .includeEmptyValue()
                .includeAs(ACL.SYSTEM2, item, StandardCredentials.class)
                .includeCurrentValue(buildEventsCredentialId);
    }

    /**
     * Validates the API URL entered by the user.
     *
     * @param buildEventsApiUrl the API URL to validate.
     * @return a {@link FormValidation} indicating whether the URL is valid or not.
     */
    public FormValidation doCheckBuildEventsApiUrl(@QueryParameter String buildEventsApiUrl) {
        try {
            new URL(buildEventsApiUrl);
        } catch (MalformedURLException e) {
            return FormValidation.error("Invalid Api URL");
        }

        return FormValidation.ok();
    }

    /**
     * Retrieves the API token from the configured credentials.
     *
     * @return the API token as a plain text string.
     */
    public String getApiToken() {
        StringCredentials cr = CredentialsMatchers.firstOrNull(
                CredentialsProvider.lookupCredentials(
                        StringCredentials.class,
                        Jenkins.get(), // Or specific item if scoped
                        null, // Authentication
                        (DomainRequirement) null), // DomainRequirement
                CredentialsMatchers.withId(getBuildEventsCredentialId()));

        assert cr != null;
        return cr.getSecret().getPlainText();
    }

    /**
     * Retrieves the configured API URL.
     *
     * @return the API URL as a string.
     */
    public String getApiUrl() {
        return getBuildEventsApiUrl();
    }
}
