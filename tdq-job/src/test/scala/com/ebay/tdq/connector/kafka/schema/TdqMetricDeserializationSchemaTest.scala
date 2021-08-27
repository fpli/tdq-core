package com.ebay.tdq.connector.kafka.schema

import com.ebay.tdq.common.env.TdqEnv
import com.ebay.tdq.common.model.InternalMetric
import com.ebay.tdq.config.{KafkaSinkConfig, KafkaSourceConfig}
import com.google.common.collect.Lists
import org.apache.commons.lang3.time.DateUtils
import org.junit.Test

/**
 * @author juntzhang
 */
class TdqMetricDeserializationSchemaTest {
  @Test
  def testSerde(): Unit = {
    val m = new InternalMetric(
      "a", DateUtils.parseDate("2021-05-06 12:05:00", "yyyy-MM-dd HH:mm:ss").getTime
    )
      .putTag("a1", "1")
      .putTag("a2", "2")
      .putExpr("b1", 1d)
      .putExpr("b2", 2d)
      .genMetricId()
      .setValue(2d)

    val tdqMetric = m.toTdqMetric("test", 6901)
    val kafkaSinkConfig = new KafkaSinkConfig()
    val kafkaSourceConfig = new KafkaSourceConfig()
    kafkaSourceConfig.setTopics(Lists.newArrayList("test"))
    kafkaSinkConfig.setParallelism(2)
    kafkaSinkConfig.setTopic("test")
    val tdqEnv = new TdqEnv()
    val ss = new TdqMetricSerializationSchema(kafkaSinkConfig, tdqEnv)
    val rec = ss.serialize(tdqMetric, tdqMetric.getEventTime)
    println(tdqMetric)
    println(new TdqMetricDeserializationSchema(kafkaSourceConfig, tdqEnv).deserialize(rec.value()))
    println(new TdqMetricDeserializationSchema(kafkaSourceConfig, tdqEnv).deserialize(rec.value()))
  }
}
