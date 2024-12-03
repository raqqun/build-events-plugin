package io.jenkins.plugins.sample.model;

import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class BuildSummaryModel {
    private int id;
    private long queueId;
    private long timestamp;
    private long duration;
    private String url;
    private String status;
    private Map<String, String> buildEnv;
    private Map<String, String> buildParameters;
    private List<Stage> stages;
    private List<Agent> agents;
    private List<ScmCheckout> checkouts;
}
