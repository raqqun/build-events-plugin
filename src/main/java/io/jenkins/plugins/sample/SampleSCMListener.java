package io.jenkins.plugins.sample;

import hudson.Extension;
import hudson.FilePath;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.model.listeners.SCMListener;
import hudson.scm.SCM;
import hudson.scm.SCMRevisionState;
import io.jenkins.plugins.sample.model.ScmCheckout;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Extension
public class SampleSCMListener extends SCMListener {

    private static final Logger log = LoggerFactory.getLogger(SampleSCMListener.class);

    @Getter
    private static final Map<String, List<ScmCheckout>> checkedSCM = new ConcurrentHashMap<>();

    @Override
    public void onCheckout(
            Run<?, ?> run,
            SCM scm,
            FilePath workspace,
            TaskListener listener,
            File changelogFile,
            SCMRevisionState pollingBaseline) {

        Map<String, String> envVars = new HashMap<>();
        scm.buildEnvironment(run, envVars);

        // Check if build had previous checked out SCM
        if (!checkedSCM.containsKey(run.getExternalizableId())) {
            // Initialize
            checkedSCM.put(run.getExternalizableId(), new ArrayList<>());
        }

        // Get SCM envVars and update build's checkedSCM
        checkedSCM.get(run.getExternalizableId()).add(new ScmCheckout(envVars));
    }
}
