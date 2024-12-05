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

### 1. `SampleConfiguration`
This class provides a **global configuration** for the plugin. It allows Jenkins administrators to configure API credentials and URLs needed for plugin operation. It also includes input validation to ensure valid configurations.

### 2. `SampleExecutorListener`
The `SampleExecutorListener` class listens for executor events in Jenkins. It records which Jenkins agents are assigned to specific builds and captures their environment variables. This information is useful for understanding which agents are used and the environment context in which builds are running.

### 3. `SampleRunListener`
The `SampleRunListener` listens to Jenkins build events and captures comprehensive information, such as:
- Build ID, URL, timestamp, and duration.
- Build environment variables.
- The different pipeline stages executed during the build.
- SCM checkouts and the agents involved.

The collected build data is then sent to an external API for further processing.

### 4. `SampleSCMListener`
This class listens to **SCM checkout events** during a build. It captures relevant environment variables during the checkout process and tracks SCM information for each build, providing useful insights into repository and branch data.

## Use Cases
- **Monitoring and Reporting**: This plugin provides valuable information about builds, agents, SCM activities, and stages, which can be used for monitoring purposes and detailed reporting.
- **Integration with External Services**: By sending build data to an external API, users can integrate Jenkins builds with other systems, allowing advanced analytics and workflow automation.
- **Environment Tracking**: Track which Jenkins agents were involved in a build and their respective environment, providing greater visibility into the build infrastructure.

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
