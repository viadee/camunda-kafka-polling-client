# The Camunda-Kafka Polling Client
[![License](https://img.shields.io/badge/License-BSD%203--Clause-blue.svg)](https://opensource.org/licenses/BSD-3-Clause)
[![Status](https://travis-ci.org/viadee/camunda-kafka-polling-client.svg?branch=master)](https://travis-ci.org/viadee/camunda-kafka-polling-client/branches "See test builds")

The Polling Client is a useful tool for the extraction of Camunda process data and their permanent storage in Apache Kafka. As such it is used in the projects [bpmn.ai](https://github.com/viadee/bpmn.ai) or [vPW](https://www.viadee.de/business-process-management/process-warehouse), that aim to open up standard process data for data mining and exploration.

Two different polling modes are supported: Polling via JDBC access from a Camunda database using an embedded Camunda engine and polling via the Camunda engines own REST API from an existing Camunda instance.

## Configuration
The polling mode selection is done by using Spring profiles.
The polling client can be configured on several levels, i.e. directly via the applications properties files or by setting environment variables.

#### Data Store Configuration

| Property  | Value | Example |
| ------------- | ------------- | ------------- |
| KAFKA_BOOTSTRAP_SERVERS  | *name:port*  | 127.0.0.1:19092 |

#### REST Polling

| Property  | Value | Example |
| ------------- | ------------- | ------------- |
| SPRING_PROFILES_ACTIVE  | *rest*  | rest |
| CAMUNDA_REST_URL  | *url to camunda rest api*  | http://localhost:8080/engine-rest/ |
| CAMUNDA_REST_USERNAME  | *(optional) username for authentication*  | demo |
| CAMUNDA_REST_PASSWORD  | *(optional) password for authentication*  | demo |

#### JDBC Polling

| Property  | Value | Example |
| ------------- | ------------- | ------------- |
| SPRING_PROFILES_ACTIVE  | *jdbc*  | jdbc |
| CAMUNDA_JDBC_URL  | *jdbc url*  | |
| CAMUNDA_JDBC_USERNAME  | *db username*  | demo |
| CAMUNDA_JDBC_PASSWORD  | *db password*  | demo |

#### Further configurations

Further configurations can be made via the application.properties file, e.g. setting the polling interval (default: every 30000 ms).

## Collaboration

The project is operated and further developed by the viadee Consulting AG in Münster, Westphalia.
* Community contributions to the project are welcome: Please open Github-Issues with suggestions (or PR), which we can then edit in the team.
* We are also looking for further partners who have interesting process data to refine our tooling as well as partners that are simply interested in a discussion about AI and data warehouses in the context of business process automation.

## Roadmap
This software component is considered to be stable and ready for production use.
However, we plan to extend on it if there is demand or if the Camunda APIs change.
