package io.jenkins.plugins.buildevents;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.model.Result;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.model.listeners.RunListener;
import io.jenkins.blueocean.rest.impl.pipeline.FlowNodeWrapper;
import io.jenkins.blueocean.rest.impl.pipeline.PipelineNodeGraphVisitor;
import io.jenkins.blueocean.rest.model.BlueRun;
import io.jenkins.plugins.buildevents.model.Agent;
import io.jenkins.plugins.buildevents.model.BuildSummary;
import io.jenkins.plugins.buildevents.model.ScmCheckout;
import io.jenkins.plugins.buildevents.model.Stage;
import io.jenkins.plugins.buildevents.service.ExternalApiService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import jenkins.model.Jenkins;
import org.jenkinsci.plugins.workflow.job.WorkflowRun;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Listener for Jenkins run events.
 * This class collects build information and sends it to an external API.
 */
@Extension
public class BuildEventsRunListener extends RunListener<Run<?, ?>> {

    private static final Logger log = LoggerFactory.getLogger(BuildEventsRunListener.class);

    /**
     * Invoked when a build is completed.
     *
     * @param run the completed build.
     * @param listener the task listener.
     */
    @Override
    public void onCompleted(Run<?, ?> run, @NonNull TaskListener listener) {
        Map<String, List<ScmCheckout>> checkedSCM = BuildEventsSCMListener.getCheckedSCM();
        Map<String, List<Agent>> agentSystemEnvVars = BuildEventsExecutorListener.getAgents();

        BuildSummary build = new BuildSummary();
        build.setId(run.getNumber());
        build.setQueueId(run.getQueueId());
        build.setUrl(Jenkins.get().getRootUrl() + run.getUrl());

        Result result = run.getResult();
        if (result != null) {
            build.setStatus(result.toString());
        }

        build.setTimestamp(run.getTimeInMillis());
        build.setDuration(run.getDuration());

        try {
            build.setBuildEnv(run.getEnvironment(listener));
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        build.setAgents(agentSystemEnvVars.get(run.getExternalizableId()));
        build.setCheckouts(checkedSCM.get(run.getExternalizableId()));

        PipelineNodeGraphVisitor visitor = new PipelineNodeGraphVisitor((WorkflowRun) run);
        List<FlowNodeWrapper> flowNodes = visitor.getPipelineNodes();
        List<Stage> stages = new ArrayList<>();
        for (FlowNodeWrapper node : flowNodes) {
            if (node.getType() == FlowNodeWrapper.NodeType.STAGE) {
                String stageName = node.getDisplayName();
                BlueRun.BlueRunResult stageResult = node.getStatus().getResult();
                stages.add(new Stage(stageName, stageResult.name()));
            }
        }

        build.setStages(stages);

        checkedSCM.remove(run.getExternalizableId());
        agentSystemEnvVars.remove(run.getExternalizableId());

        ExternalApiService.sendData(ExternalApiService.buildToJson(build));
    }

    /**
     * Invoked when a build is finalized.
     *
     * @param run the finalized build.
     */
    @Override
    public void onFinalized(Run<?, ?> run) {
        log.info("onFinalized");
    }

    /**
     * Invoked when a build is initialized.
     *
     * @param run the build being initialized.
     */
    @Override
    public void onInitialize(Run<?, ?> run) {
        log.info("onInitialize");
    }

    /**
     * Invoked when a build is started.
     *
     * @param run the build that has started.
     * @param listener the task listener.
     */
    @Override
    public void onStarted(Run<?, ?> run, TaskListener listener) {
        log.info("onStarted");
    }
}
