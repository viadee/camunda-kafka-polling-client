This tutorial explains how process data can be stored and archived in the Apache Kafka Streaming Platform.
First, we will look at how data is extracted from Camunda and stored in Kafka using the Polling Client.
For users who do not have a Camunda instance or Camunda process data available, this tutorial alternatively explains how JSON-based process data can be archived to Kafka (continue reading at [section 2](2.-Using-the-JSON-Import)).

## 1. Using the Polling Client
The Polling Client serves as an interface between the Camunda process engine and the Kafka Streaming Platform. The Client supports two polling modes: Polling via JDBC access from a Camunda database using an embedded Camunda engine and polling via the Camunda engines own REST API from an existing Camunda instance.
After cloning and building this repository, configure the Client to retrieve data from your Camunda Engine.

### 1.1. Configuration
The polling client can be configured on several levels, i.e. directly via the applications properties files or by setting environment variables (**recommended**).
First, we define where the Polling Client should store the data it has retrieved from Camunda.

#### Data Store Configuration

| Property  | Value | Example |
| ------------- | ------------- | ------------- |
| KAFKA_BOOTSTRAP_SERVERS  | *name:port*  | 127.0.0.1:19092 |

You can omit this property if you have no Kafka instance running yet (no process data will be stored then).

Next, set environment variables corresponding to the chosen polling mode.

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

Further configurations can be made via the applications property file *application.properties*, e.g. setting the polling interval (default: every 30000 ms).

### 1.2. Run the Polling Client
Run the *PollingClientApllication* to extract the process data and store them in Kafka.

## 2. Using the JSON-Import
This part of the tutorial serves to store process data in Apache Kafka **without having a Camunda database available**. This is for illustrative purposes and integration testing only.

Since you cloned the spark-importer repository already, you can now build the [kafkatutorialproducer](https://github.com/viadee/bpmn.ai/tree/develop/tutorials/spark%20importer/kafkatutorialproducer) submodule.

Two JSON based data sources are provided to be imported to Kafka: The *processInstances.json* contains 7 entries to be processed, the *variableUpdate.json* consists of 36 variable updates to be processed.

By running the *KafkaProducerApp*, Kafka will be filled with data on server defined in the app: "127.0.0.1:19092".


## 3. Next steps
In the fourth tutorial ["Kafka Import and Preprocessing"](https://github.com/viadee/bpmn.ai/wiki/Tutorial-4-%E2%80%90-Kafka-Import-and-Preprocessing), we will look at how to import and preprocess the data in Spark.
