package io.jenkins.plugins.sample;

import static org.junit.Assert.*;

import org.htmlunit.html.HtmlForm;
import org.htmlunit.html.HtmlTextInput;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsSessionRule;

public class SampleConfigurationTest {

    @Rule
    public JenkinsSessionRule sessions = new JenkinsSessionRule();

    /**
     * Tries to exercise enough code paths to catch common mistakes:
     * <ul>
     * <li>missing {@code load}
     * <li>missing {@code save}
     * <li>misnamed or absent getter/setter
     * <li>misnamed {@code textbox}
     * </ul>
     */
    @Test
    public void uiAndStorage() throws Throwable {
        sessions.then(r -> {
            HtmlForm config = r.createWebClient().goTo("configure").getFormByName("config");
            r.submit(config);
            assertEquals(
                    "add empty value for sample secret",
                    "",
                    SampleConfiguration.get().getSampleCredentialId());
        });

        sessions.then(r -> {
            HtmlForm config = r.createWebClient().goTo("configure").getFormByName("config");
            HtmlTextInput textbox = config.getInputByName("_.sampleApiUrl");
            textbox.setText("https://www.example.com");
            r.submit(config);

            assertEquals(
                    "add url value for sample url",
                    "https://www.example.com",
                    SampleConfiguration.get().getSampleApiUrl());
        });

        sessions.then(r -> {
            assertEquals(
                    "still there after restart of Jenkins",
                    "",
                    SampleConfiguration.get().getSampleCredentialId());
        });
    }
}
