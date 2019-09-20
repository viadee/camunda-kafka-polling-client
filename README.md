# The Camunda-Kafka Polling Client
[![License](https://img.shields.io/badge/License-BSD%203--Clause-blue.svg)](https://opensource.org/licenses/BSD-3-Clause)
[![Status](https://travis-ci.org/viadee/camunda-kafka-polling-client.svg?branch=master)](https://travis-ci.org/viadee/camunda-kafka-polling-client/branches "See test builds")
[![Sonarcloud Coverage](https://sonarcloud.io/api/project_badges/measure?project=de.viadee.camunda:camunda-kafka-polling-client-parent&metric=coverage)](https://sonarcloud.io/dashboard?id=de.viadee.camunda:camunda-kafka-polling-client-parent)
![Docker Cloud Build Status](https://img.shields.io/docker/cloud/build/viadee/camunda-kafka-polling-client.svg)
[![Maven Central](https://img.shields.io/maven-central/v/de.viadee.camunda/camunda-kafka-polling-client-parent.svg)](https://search.maven.org/search?q=g:de.viadee.camunda%20a:camunda-kafka-polling-client-parent)

The Polling Client is a useful tool for the extraction of Camunda process data and their permanent storage in Apache Kafka. As such it is used in the projects [bpmn.ai](https://github.com/viadee/bpmn.ai) or [vPW](https://www.viadee.de/business-process-management/process-warehouse), that aim to open up standard process data for data mining and exploration.

Two different polling modes are supported: Polling via JDBC access from a Camunda database using an embedded Camunda engine and polling via the Camunda engines own REST API from an existing Camunda instance.

## Configuration
We provide a pre-configured [docker image on docker hub](https://hub.docker.com/r/viadee/camunda-kafka-polling-client).

The polling mode selection is done by using Spring profiles.
The polling client can be configured on several levels, i.e. directly via the applications properties files or by setting environment variables.

#### Data Store Configuration

| Property  | Value | Example |
| ------------- | ------------- | ------------- |
| KAFKA_BOOTSTRAP_SERVERS  | *name:port*  | 127.0.0.1:19092 |

Depending on its retention policy your Kafka server might discard messages after a defined time period.
You can set the retention policy for the broker by adjusting the value for the key log.retention.hours.
```bash
bin/kafka-configs.sh --bootstrap-server localhost:9092 --entity-type brokers --entity-default --alter --add-config log.retention.hours=1073741824
```

#### REST Polling

| Property  | Value | Example |
| ------------- | ------------- | ------------- |
| SPRING_PROFILES_ACTIVE  | *rest*  | rest |
| CAMUNDA_REST_URL  | *url to camunda rest api*  | http://localhost:8080/engine-rest/ |
| CAMUNDA_REST_USERNAME  | *(optional) username for authentication*  | demo |
| CAMUNDA_REST_PASSWORD  | *(optional) password for authentication*  | demo |
| CAMUNDA_REST_SOURCE_TIME_ZONE | *(optional) Time zone used for formatting timestamps provided to camunda rest api (Default: System timezone)* | CEST |
| CAMUNDA_REST_DATE_FORMAT_PATERN | *(optional) Format string used for formatting timestamps provided to camunda rest api (Default: See example)* | yyyy-MM-dd'T'HH:mm:ss.SSSZ |

#### JDBC Polling

| Property  | Value | Example |
| ------------- | ------------- | ------------- |
| SPRING_PROFILES_ACTIVE  | *jdbc*  | jdbc |
| CAMUNDA_JDBC_URL  | *jdbc url*  | |
| CAMUNDA_JDBC_USERNAME  | *db username*  | demo |
| CAMUNDA_JDBC_PASSWORD  | *db password*  | demo |

#### Polling start time configuration
| Property  | Value | Example |
| ------------- | ------------- | ------------- |
| POLLING_RUNTIME_DATA_INITIAL_TIMESTAMP | *(optional) initial start time the polling of runtime data (only relevant for initial polling);<br/>format: "yyyy-MM-dd HH:mm:ss".<br/>Default is the current time.*  | 2018-01-01 00:00:00 |
| POLLING_REPOSITORY_DATA_INITIAL_TIMESTAMP | *(optional) initial start time the polling of repository data (only relevant for initial polling);<br/>format: "yyyy-MM-dd HH:mm:ss.<br/>Default is the current time."*  | 2018-01-01 00:00:00 |

#### Further configurations

Further configurations can be made via the application.properties file, e.g. setting the polling interval (default: every 30000 ms).

## Docker Tags
There are several tags available on [docker hub](https://hub.docker.com/r/viadee/camunda-kafka-polling-client):

* Version tags (e.g. 1.0.2) are build from corresponding release tags and reflect the release version which is also available from [maven central](https://search.maven.org/search?q=g:de.viadee.camunda%20a:camunda-kafka-polling-client-parent).
* `latest` is build from `master` branch. Thus, the tag normally reflects the latest release version.
* `snapshot` is build from `develop` branch and provides the current development version for development and testing use.

## Extending Docker Image
All data is stored in `/data`. This is also the working directory and the only directory with write permissions.

All JAR files available in `/app/lib` are included in applications classpath. Adding e.g. an additional jdbc
driver can thus be done by just adding the driver library to `/app/lib`.

## Collaboration

The project is operated and further developed by the viadee Consulting AG in Münster, Westphalia.
* Community contributions to the project are welcome: Please open Github-Issues with suggestions (or PR), which we can then edit in the team. For contribution please take account of our [contributing guidelines](https://github.com/viadee/camunda-kafka-polling-client/blob/master/CONTRIBUTING.md).
* We are also looking for further partners who have interesting process data to refine our tooling as well as partners that are simply interested in a discussion about AI and data warehouses in the context of business process automation.


## Commitments

This library will remain under an open source licence indefinately.

We follow the semantic versioning scheme (2.0.0).

In the sense of semantic versioning, the resulting JSON outputs are the only public "API" provided here. We will keep these as stable as possible.

## Roadmap
This software component is considered to be stable and ready for production use.
However, we plan to extend on it if there is demand or if the Camunda APIs change.

## License
This project is licensed under the BSD 3-Clause "New" or "Revised" License - see the [LICENSE](https://github.com/viadee/camunda-kafka-polling-client/blob/master/LICENSE) file for details.