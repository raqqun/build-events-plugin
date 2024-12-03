package io.jenkins.plugins.sample;

import hudson.Extension;
import hudson.model.*;
import hudson.slaves.SlaveComputer;
import io.jenkins.plugins.sample.model.Agent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Extension
@Slf4j
public class SampleExecutorListener implements ExecutorListener {

    @Getter
    private static final Map<String, List<Agent>> agents = new ConcurrentHashMap<>();

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
