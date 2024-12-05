# Build Events Plugin

## Overview

The **Build Events Plugin** provides enhanced capabilities for monitoring and interacting with Jenkins builds. This plugin includes global configuration options, listens to executor events, run events, and SCM activities, and integrates with external APIs to capture critical data about builds and stages. The primary goal of this plugin is to collect information throughout the Jenkins pipeline and send it to an external service for further processing or analysis.

## Features

1. **Global Configuration**
    - Configurable API URL and credentials for accessing an external service.
    - Jenkins administrators can easily set up credentials through the global configuration.

2. **Executor Listener**
    - Tracks Jenkins agents involved in the builds.
    - Collects environment variables for each agent and stores the agent details for further analysis.

3. **Run Listener**
    - Monitors build events, including initialization, start, completion, and finalization.
    - Collects detailed build information such as build status, build environment variables, and pipeline stages.
    - Sends the collected data to an external API for further usage.

4. **SCM Listener**
    - Tracks SCM (Source Code Management) checkouts during Jenkins builds.
    - Gathers information about SCM changes and provides insights into which repositories and branches are being checked out.

## Plugin Classes

### 1. `BuildEventsConfiguration`
This class provides a **global configuration** for the plugin. It allows Jenkins administrators to configure API credentials and URLs needed for plugin operation. It also includes input validation to ensure valid configurations.

### 2. `BuildEventsExecutorListener`
The `BuildEventsExecutorListener` class listens for executor events in Jenkins. It records which Jenkins agents are assigned to specific builds and captures their environment variables. This information is useful for understanding which agents are used and the environment context in which builds are running.

### 3. `BuildEventsRunListener`
The `BuildEventsRunListener` listens to Jenkins build events and captures comprehensive information, such as:
- Build ID, URL, timestamp, and duration.
- Build environment variables.
- The different pipeline stages executed during the build.
- SCM checkouts and the agents involved.

The collected build data is then sent to an external API for further processing.

### 4. `BuildEventsSCMListener`
This class listens to **SCM checkout events** during a build. It captures relevant environment variables during the checkout process and tracks SCM information for each build, providing useful insights into repository and branch data.

## Use Cases
- **Monitoring and Reporting**: This plugin provides valuable information about builds, agents, SCM activities, and stages, which can be used for monitoring purposes and detailed reporting.
- **Integration with External Services**: By sending build data to an external API, users can integrate Jenkins builds with other systems, allowing advanced analytics and workflow automation.
- **Environment Tracking**: Track which Jenkins agents were involved in a build and their respective environment, providing greater visibility into the build infrastructure.

## Output

```json
{
  "name": "python3-nhzbw",
  "systemEnvVars": {
    "GPG_KEY": "7169605F62C751356D054A26A821E680E5FA6305",
    "HOME": "/root",
    "HOSTNAME": "python3-nhzbw",
    "JENKINS_AGENT_NAME": "python3-nhzbw",
    "JENKINS_AGENT_WORKDIR": "/home/jenkins/agent",
    "JENKINS_NAME": "python3-nhzbw",
    "JENKINS_SECRET": "dd71152b8958b88664c2065aaed157618c00adedaf152d757adf208b3fb41f1d",
    "JENKINS_TEST_AGENT_PORT": "tcp://10.101.13.29:50000",
    "JENKINS_TEST_AGENT_PORT_50000_TCP": "tcp://10.101.13.29:50000",
    "JENKINS_TEST_AGENT_PORT_50000_TCP_ADDR": "10.101.13.29",
    "JENKINS_TEST_AGENT_PORT_50000_TCP_PORT": "50000",
    "JENKINS_TEST_AGENT_PORT_50000_TCP_PROTO": "tcp",
    "JENKINS_TEST_AGENT_SERVICE_HOST": "10.101.13.29",
    "JENKINS_TEST_AGENT_SERVICE_PORT": "50000",
    "JENKINS_TEST_AGENT_SERVICE_PORT_AGENT_LISTENER": "50000",
    "JENKINS_TEST_PORT": "tcp://10.99.136.80:8080",
    "JENKINS_TEST_PORT_8080_TCP": "tcp://10.99.136.80:8080",
    "JENKINS_TEST_PORT_8080_TCP_ADDR": "10.99.136.80",
    "JENKINS_TEST_PORT_8080_TCP_PORT": "8080",
    "JENKINS_TEST_PORT_8080_TCP_PROTO": "tcp",
    "JENKINS_TEST_SERVICE_HOST": "10.99.136.80",
    "JENKINS_TEST_SERVICE_PORT": "8080",
    "JENKINS_TEST_SERVICE_PORT_HTTP": "8080",
    "JENKINS_TUNNEL": "jenkins-test-agent.jenkins.svc.cluster.local:50000",
    "JENKINS_URL": "http://jenkins-test.jenkins.svc.cluster.local:8080/",
    "KUBERNETES_PORT": "tcp://10.96.0.1:443",
    "KUBERNETES_PORT_443_TCP": "tcp://10.96.0.1:443",
    "KUBERNETES_PORT_443_TCP_ADDR": "10.96.0.1",
    "KUBERNETES_PORT_443_TCP_PORT": "443",
    "KUBERNETES_PORT_443_TCP_PROTO": "tcp",
    "KUBERNETES_SERVICE_HOST": "10.96.0.1",
    "KUBERNETES_SERVICE_PORT": "443",
    "KUBERNETES_SERVICE_PORT_HTTPS": "443",
    "PATH": "/usr/local/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin",
    "PWD": "/home/jenkins/agent",
    "PYTHON_SHA256": "086de5882e3cb310d4dca48457522e2e48018ecd43da9cdf827f6a0759efb07d",
    "PYTHON_VERSION": "3.13.0",
    "REMOTING_OPTS": "-noReconnectAfter 1d"
  }, 
  "buildEnv": {
    "BUILD_DISPLAY_NAME": "#13",
    "BUILD_ID": "13",
    "BUILD_NUMBER": "13",
    "BUILD_TAG": "jenkins-test1-13",
    "BUILD_URL": "https://jenkins.192-168-49-2.nip.io/job/test1/13/",
    "CI": "true",
    "CLASSPATH": "",
    "HUDSON_HOME": "/var/jenkins_home",
    "HUDSON_SERVER_COOKIE": "342f5f5a0daac370",
    "HUDSON_URL": "https://jenkins.192-168-49-2.nip.io/",
    "JENKINS_HOME": "/var/jenkins_home",
    "JENKINS_SERVER_COOKIE": "342f5f5a0daac370",
    "JENKINS_URL": "https://jenkins.192-168-49-2.nip.io/",
    "JOB_BASE_NAME": "test1",
    "JOB_DISPLAY_URL": "https://jenkins.192-168-49-2.nip.io/job/test1/display/redirect",
    "JOB_NAME": "test1",
    "JOB_URL": "https://jenkins.192-168-49-2.nip.io/job/test1/",
    "RUN_ARTIFACTS_DISPLAY_URL": "https://jenkins.192-168-49-2.nip.io/job/test1/13/display/redirect?page=artifacts",
    "RUN_CHANGES_DISPLAY_URL": "https://jenkins.192-168-49-2.nip.io/job/test1/13/display/redirect?page=changes",
    "RUN_DISPLAY_URL": "https://jenkins.192-168-49-2.nip.io/job/test1/13/display/redirect",
    "RUN_TESTS_DISPLAY_URL": "https://jenkins.192-168-49-2.nip.io/job/test1/13/display/redirect?page=tests"
  }, 
  "buildParameters": null,
  "checkouts": [
    {
      "scmEnvVars": {
        "GIT_BRANCH": "origin/main",
        "GIT_COMMIT": "54d9f800e7d00c7a74f036fc81799287ef9676f3",
        "GIT_LOCAL_BRANCH": "main",
        "GIT_PREVIOUS_COMMIT": "54d9f800e7d00c7a74f036fc81799287ef9676f3",
        "GIT_PREVIOUS_SUCCESSFUL_COMMIT": "54d9f800e7d00c7a74f036fc81799287ef9676f3",
        "GIT_URL": "https://github.com/writetoritika/Petclinic.git"
      }
    }
  ],
  "duration": 192468,
  "id": 13,
  "queueId": 131,
  "stages": [
    {
      "name": "Git Checkout",
      "status": "SUCCESS"
    },
    {
      "name": "Compile",
      "status": "SUCCESS"
    },
    {
      "name": "Test Cases",
      "status": "SUCCESS"
    }
  ],
  "status": "SUCCESS",
  "timestamp": 1733442099062,
  "url": "https://jenkins.192-168-49-2.nip.io/job/test1/13/"
}
```

## Setup Instructions

1. **Install the Plugin**
    - Clone the repository and build the plugin using Maven:
      ```sh
      mvn package
      ```
    - Use the `.hpi` file generated in the `target/` directory to install the plugin into Jenkins.

2. **Configure the Plugin**
    - Navigate to **Manage Jenkins > Configure System**.
    - Locate **Build Events Plugin** section.
    - Enter the **API URL** and **Credentials** that will be used for external service communication.

3. **Usage**
    - Once configured, the plugin will automatically track builds, SCM checkouts, and Jenkins agent events.
    - Build summary data will be sent to the configured external API after each build is completed.

## Dependencies
- **Jenkins Core API** for interacting with Jenkins internal events and objects.
- **Blue Ocean Pipeline API** to extract detailed pipeline information.
- **Credentials Plugin** for securely managing credentials.

## Contributing
Contributions are welcome! Feel free to open issues or pull requests to improve the functionality or add new features.
