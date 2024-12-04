package io.jenkins.plugins.sample;

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
 * Represents the global configuration for the sample plugin.
 * This class allows setting credentials and API URL used by the plugin.
 */
@Getter
@Extension
public class SampleConfiguration extends GlobalConfiguration {

    private static final Logger log = LoggerFactory.getLogger(SampleConfiguration.class);
    private String sampleCredentialId;
    private String sampleApiUrl;

    /**
     * Retrieves the singleton instance of {@link SampleConfiguration}.
     *
     * @return the {@link SampleConfiguration} instance.
     */
    public static SampleConfiguration get() {
        return ExtensionList.lookupSingleton(SampleConfiguration.class);
    }

    /**
     * Constructor that loads any saved configuration from disk.
     */
    public SampleConfiguration() {
        load();
    }

    /**
     * Sets the credential ID for accessing external services.
     *
     * @param sampleCredentialId the credential ID to be set.
     */
    @DataBoundSetter
    public void setSampleCredentialId(String sampleCredentialId) {
        this.sampleCredentialId = sampleCredentialId;
        save();
    }

    /**
     * Sets the API URL used by the plugin.
     *
     * @param sampleApiUrl the API URL to be set.
     */
    @DataBoundSetter
    public void setSampleApiUrl(String sampleApiUrl) {
        this.sampleApiUrl = sampleApiUrl;
        save();
    }

    /**
     * Populates the list box with available credentials.
     *
     * @param item the Jenkins item.
     * @param sampleCredentialId the current credential ID.
     * @return a {@link ListBoxModel} containing the available credentials.
     */
    public ListBoxModel doFillSampleCredentialIdItems(
            @AncestorInPath Item item, @QueryParameter String sampleCredentialId) {
        StandardListBoxModel listBoxModel = new StandardListBoxModel();

        if (!Jenkins.get().hasPermission(Jenkins.ADMINISTER)) {
            return listBoxModel;
        }

        return listBoxModel
                .includeEmptyValue()
                .includeAs(ACL.SYSTEM2, item, StandardCredentials.class)
                .includeCurrentValue(sampleCredentialId);
    }

    /**
     * Validates the API URL entered by the user.
     *
     * @param sampleApiUrl the API URL to validate.
     * @return a {@link FormValidation} indicating whether the URL is valid or not.
     */
    public FormValidation doCheckSampleApiUrl(@QueryParameter String sampleApiUrl) {
        try {
            new URL(sampleApiUrl);
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
                CredentialsMatchers.withId(getSampleCredentialId()));

        assert cr != null;
        return cr.getSecret().getPlainText();
    }

    /**
     * Retrieves the configured API URL.
     *
     * @return the API URL as a string.
     */
    public String getApiUrl() {
        return getSampleApiUrl();
    }
}
