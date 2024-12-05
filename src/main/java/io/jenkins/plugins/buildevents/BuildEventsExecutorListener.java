package io.jenkins.plugins.buildevents;

import hudson.Extension;
import hudson.model.*;
import hudson.slaves.SlaveComputer;
import io.jenkins.plugins.buildevents.model.Agent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Listener for Jenkins executor events.
 * This class tracks the agents involved in Jenkins builds.
 */
@Extension
@Slf4j
public class BuildEventsExecutorListener implements ExecutorListener {

    @Getter
    private static final Map<String, List<Agent>> agents = new ConcurrentHashMap<>();

    /**
     * Invoked when a task is started by an executor.
     *
     * @param executor the executor that started the task.
     * @param task the task that has started.
     */
    @Override
    public void taskStarted(Executor executor, Queue.Task task) {
        Computer c = executor.getOwner();
        if (c instanceof SlaveComputer) {
            SlaveComputer sc = (SlaveComputer) c;

            Map<String, String> agentEnvVars;

            try {
                agentEnvVars = sc.getEnvVarsFull();
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }

            Queue.Executable currentExecutable = task.getOwnerExecutable();
            assert currentExecutable != null;

            if (currentExecutable instanceof Run) {
                Run<?, ?> run = (Run<?, ?>) currentExecutable;

                if (!agents.containsKey(run.getExternalizableId())) {
                    agents.put(run.getExternalizableId(), new ArrayList<>());
                }

                agents.get(run.getExternalizableId()).add(new Agent(c.getDisplayName(), agentEnvVars));
            }
        }
    }
}
