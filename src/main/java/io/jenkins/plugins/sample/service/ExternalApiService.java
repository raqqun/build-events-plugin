package io.jenkins.plugins.sample.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.jenkins.plugins.sample.SampleConfiguration;
import io.jenkins.plugins.sample.model.BuildSummaryModel;
import java.io.IOException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExternalApiService {

    private static final SampleConfiguration config = SampleConfiguration.get();
    private static final Logger log = LoggerFactory.getLogger(ExternalApiService.class);

    public static void sendData(String jsonPayload) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(config.getApiUrl());

            request.setHeader("Content-Type", "application/json");
            request.setHeader("X-API-KEY", config.getApiToken());
            request.setEntity(new StringEntity(jsonPayload, "UTF-8"));

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                if (response.getStatusLine().getStatusCode() == 200) {
                    log.info("Successfully sent data.");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String buildToJson(BuildSummaryModel build) {
        Gson gson = new GsonBuilder().serializeNulls().create();

        return gson.toJson(build);
    }
}
