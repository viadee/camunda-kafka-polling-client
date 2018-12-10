# The Polling Client
The Polling Client can serve as an interface between a Camunda process engine and a Kafka Streaming Platform, where the former is the data source and the later acts as a data store.
Twwo polling modes are supported: Polling via JDBC access from a Camunda database using an embedded Camunda engine and polling via the Camunda engines own REST API from an existing Camunda instance.

## Polling Client Configuration
The polling mode selection is done by using profiles.
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
