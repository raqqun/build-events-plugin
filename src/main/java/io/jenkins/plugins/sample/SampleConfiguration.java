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
 * Example of Jenkins global configuration.
 */
@Getter
@Extension
public class SampleConfiguration extends GlobalConfiguration {

    private static final Logger log = LoggerFactory.getLogger(SampleConfiguration.class);
    private String sampleCredentialId;
    private String sampleApiUrl;

    public static SampleConfiguration get() {
        return ExtensionList.lookupSingleton(SampleConfiguration.class);
    }

    public SampleConfiguration() {
        // When Jenkins is restarted, load any saved configuration from disk.
        load();
    }

    @DataBoundSetter
    public void setSampleCredentialId(String sampleCredentialId) {
        this.sampleCredentialId = sampleCredentialId;
        save();
    }

    @DataBoundSetter
    public void setSampleApiUrl(String sampleApiUrl) {
        this.sampleApiUrl = sampleApiUrl;
        save();
    }

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

    public FormValidation doCheckSampleApiUrl(@QueryParameter String sampleApiUrl) {
        try {
            new URL(sampleApiUrl);
        } catch (MalformedURLException e) {
            return FormValidation.error("Invalid Api URL");
        }

        return FormValidation.ok();
    }

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

    public String getApiUrl() {
        return getSampleApiUrl();
    }
}
