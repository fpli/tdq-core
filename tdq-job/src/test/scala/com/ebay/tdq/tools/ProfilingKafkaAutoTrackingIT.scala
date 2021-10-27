package com.ebay.tdq.tools

import com.ebay.tdq.ProfilingJobIT
import com.ebay.tdq.jobs.ProfilingJob

/**
 * @author juntzhang
 */
case class ProfilingKafkaAutoTrackingIT(profiler: String) extends ProfilingJob {

  import ProfilingJobIT.setupDB

  override def setup(args: Array[String]): Unit = {
    setupDB(
      """
        |{
        |  "id": "123",
        |  "name": "tdq.dev.autotracking",
        |  "rules": [
        |    {
        |      "name": "tdq_system",
        |      "type": "realtime.rheos.profiler",
        |      "config": {
        |        "window": "1min"
        |      },
        |      "profilers": [
        |        {{profiler}}
        |      ]
        |    }
        |  ],
        |  "sources": [
        |    {
        |      "name": "kafka_autotracking_rno",
        |      "type": "realtime.kafka",
        |      "config": {
        |        "stream": "behavior.autotracking",
        |        "topics": "behavior.autotracking.rheostrackevent",
        |        "deserializer": "com.ebay.tdq.connector.kafka.schema.TdqEventDeserializationSchema",
        |        "startup-mode": "LATEST",
        |        "kafka-consumer": {
        |          "receive.buffer": 8388,
        |          "sasl.mechanism": "IAF",
        |          "fetch.max.bytes": 52428,
        |          "max.poll.records": 500,
        |          "sasl.jaas.config": "io.ebay.rheos.kafka.security.iaf.IAFLoginModule required iafConsumerId=\"urn:ebay-marketplace-consumerid:7b071ddc-1ab1-4f2b-8fc4-8d2a0afcff46\" iafSecret=\"2c43449b-0d6f-4054-a082-e5576d667654\" iafEnv=\"staging\";",
        |          "auto.offset.reset": "latest",
        |          "bootstrap.servers": "rhs-nbnaniaa-kfk-lvs-1.rheos-streaming-qa.svc.130.tess.io:9092,rhs-nbnaniaa-kfk-lvs-2.rheos-streaming-qa.svc.130.tess.io:9092,rhs-nbnaniaa-kfk-lvs-3.rheos-streaming-qa.svc.130.tess.io:9092,rhs-nbnaniaa-kfk-lvs-4.rheos-streaming-qa.svc.130.tess.io:9092",
        |          "fetch.max.wait.ms": 100,
        |          "security.protocol": "SASL_PLAINTEXT",
        |          "max.partition.fetch.bytes": 10485,
        |          "partition.assignment.strategy": "org.apache.kafka.clients.consumer.RoundRobinAssignor"
        |        },
        |        "schema-subject": "rheosTrackEvent",
        |        "rhs-parallelism": 3,
        |        "event-time-field": "autoTrackEvent.activity.`timestamp`",
        |        "rhs-idle-timeout": "10s",
        |        "rheos-services-urls": "https://rheos-services.qa.ebay.com",
        |        "rhs-out-of-orderless": "10s"
        |      }
        |    }
        |  ],
        |  "sinks": [
        |    {
        |      "name": "console",
        |      "type": "realtime.console",
        |      "config": {
        |        "sub-type": "normal-metric",
        |        "std-name": "normal"
        |      }
        |    }
        |  ],
        |  "env": {
        |    "config": {
        |      "flink.app.window.metric-1st-aggr": "1min",
        |      "flink.app.local-aggr.queue-size": 10,
        |      "flink.app.local-aggr.flush-timeout": "5s",
        |      "flink.app.local-aggr.output-partitions": 1,
        |      "flink.app.parallelism.metric-1st-aggr": 1,
        |      "flink.app.parallelism.metric-2nd-aggr": 1
        |    }
        |  }
        |}
        |""".stripMargin.replace("{{profiler}}", profiler))
    super.setup(args)
  }

  def test(): Unit = {
    submit(Array[String](
      "--flink.app.name", "tdq.dev.autotracking", "--flink.app.local", "true"
    ))
  }
}
