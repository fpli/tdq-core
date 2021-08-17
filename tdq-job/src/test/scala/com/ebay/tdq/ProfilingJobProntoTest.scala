package com.ebay.tdq

import com.ebay.tdq.RawEventTest.getTdqEvent
import org.junit.Test

/**
 * @author juntzhang
 */
class ProfilingJobProntoTest {

  import TestMetricFactory._

  @Test
  def test_sum_by_page_id_sink2pronto(): Unit = {
    val id = "test_sum_by_page_id_sink2pronto"
    ProfilingJobIT.es(
      id = id,
      config =
        s"""
           |{
           |  "id": "1",
           |  "name": "$id",
           |  "sources": [{"name":"$id","type":"realtime.memory","config":{}}],
           |  "rules": [
           |    {
           |      "name": "rule_1",
           |      "type": "realtime.rheos.profiler",
           |      "config": {
           |        "window": "2min"
           |      },
           |      "profilers": [
           |        {
           |          "metric-name": "$id",
           |          "dimensions": ["page_id"],
           |          "filter": "CAST(clientData.contentLength AS DOUBLE) > 30.0",
           |          "expression": {"operator": "Expr", "config": {"text": "t_duration_sum"}},
           |          "transformations": [
           |            {
           |              "alias": "page_id",
           |              "expression": {"operator": "UDF", "config": {"text": "CAST( SOJ_NVL('p') AS INTEGER)"}}
           |            },
           |            {
           |              "alias": "t_duration_sum",
           |              "expression": {"operator": "SUM", "config": {"arg0": "CAST( SOJ_NVL('TDuration') AS DOUBLE)"}}
           |            }
           |          ]
           |        }
           |      ]
           |    }
           |  ],
           |  "sinks": [
           |    {
           |      "name": "hdfs_tdq_normal_metric",
           |      "type": "realtime.hdfs",
           |      "config": {
           |        "sub-type": "normal-metric",
           |        "rheos-services-urls": "https://rheos-services.qa.ebay.com",
           |        "schema-subject": "tdq.metric",
           |        "hdfs-path": "target/$${flink.app.profile}/metric/normal"
           |      }
           |    },
           |    {
           |      "name": "console_tdq_normal_metric",
           |      "type": "realtime.console",
           |      "config": {
           |        "sub-type": "normal-metric",
           |        "std-name": "nor@mal"
           |      }
           |    },
           |    {
           |      "name": "memory_tdq_normal_metric",
           |      "type": "realtime.memory",
           |      "config": {
           |        "sub-type": "normal-metric"
           |      }
           |    },
           |    {
           |      "name": "pronto_tdq_normal_metric",
           |      "type": "realtime.pronto",
           |      "config": {
           |        "sub-type": "normal-metric",
           |        "index-pattern": "tdq.$${flink.app.profile}.metric.normal."
           |      }
           |    }
           |  ]
           |}
           |""".stripMargin,
      events = List(
        getTdqEvent("2021-05-29 12:00:00", contentLength = 31, pageId = 1, tDuration = 1d),
        getTdqEvent("2021-05-29 12:01:02", contentLength = 29, pageId = 2, tDuration = 2d), //ignore
        getTdqEvent("2021-05-29 12:01:59", contentLength = 31, pageId = 1, tDuration = 3d),
        getTdqEvent("2021-05-29 12:01:59", contentLength = 31, pageId = 2, tDuration = 3d),
        getTdqEvent("2021-05-29 12:02:00", contentLength = 31, pageId = 1, tDuration = 5d),
        getTdqEvent("2021-05-29 12:03:59", contentLength = 31, pageId = 1, tDuration = 6d),
        getTdqEvent("2021-05-29 12:04:00", contentLength = 31, pageId = 2, tDuration = 7d)
      ),
      expects = List(
        getMetric(id, "2021-05-29 12:01:59", tagK = "page_id", tagV = "1", 4d),
        getMetric(id, "2021-05-29 12:01:59", tagK = "page_id", tagV = "2", 3d),
        getMetric(id, "2021-05-29 12:03:59", tagK = "page_id", tagV = "1", 11d),
        getMetric(id, "2021-05-29 12:05:59", tagK = "page_id", tagV = "2", 7d)
      )
    )
  }

  @Test
  def test_avg_casewhen_sink2pronto(): Unit = {
    val id = "test_avg_casewhen_sink2pronto"
    ProfilingJobIT.es(
      id = id,
      config =
        s"""
           |{
           |  "id": "1",
           |  "name": "$id",
           |  "sources": [{"name":"$id","type":"realtime.memory","config":{}}],
           |  "rules": [
           |    {
           |      "name": "rule_1",
           |      "type": "realtime.rheos.profiler",
           |      "config": {
           |        "window": "2min"
           |      },
           |      "profilers": [
           |        {
           |          "metric-name": "$id",
           |          "dimensions": ["page_id"],
           |          "expression": {"operator": "Expr", "config": {"text": "itm_valid_cnt / itm_cnt"}},
           |          "transformations": [
           |            {
           |              "alias": "page_id",
           |              "expression": {"operator": "UDF", "config": {"text": "CAST( SOJ_NVL('p') AS INTEGER)"}}
           |            },
           |            {
           |              "alias": "item",
           |              "expression": {
           |                "operator": "UDF",
           |                "config": {"text": "CAST( SOJ_NVL('itm|itmid|itm_id|itmlist|litm') AS LONG)"}
           |              }
           |            },
           |            {
           |              "alias": "itm_valid_ind",
           |              "expression": {
           |                "operator": "Expr",
           |                "config": {
           |                  "text": "case when item is not null then 1.0 else 0.0 end"
           |                }
           |              }
           |            },
           |            {
           |              "alias": "itm_cnt",
           |              "expression": {"operator": "Count", "config": {"arg0": "1.0"}}
           |            },
           |            {
           |              "alias": "itm_valid_cnt",
           |              "expression": {
           |                "operator": "Sum", "config": {"arg0": "itm_valid_ind"}
           |              }
           |            }
           |          ]
           |        }
           |      ]
           |    }
           |  ],
           |  "sinks": [
           |    {
           |      "name": "hdfs_tdq_normal_metric",
           |      "type": "realtime.hdfs",
           |      "config": {
           |        "sub-type": "normal-metric",
           |        "rheos-services-urls": "https://rheos-services.qa.ebay.com",
           |        "schema-subject": "tdq.metric",
           |        "hdfs-path": "target/$${flink.app.profile}/metric/normal"
           |      }
           |    },
           |    {
           |      "name": "console_tdq_normal_metric",
           |      "type": "realtime.console",
           |      "config": {
           |        "sub-type": "normal-metric",
           |        "std-name": "nor@mal"
           |      }
           |    },
           |    {
           |      "name": "memory_tdq_normal_metric",
           |      "type": "realtime.memory",
           |      "config": {
           |        "sub-type": "normal-metric"
           |      }
           |    },
           |    {
           |      "name": "pronto_tdq_normal_metric",
           |      "type": "realtime.pronto",
           |      "config": {
           |        "sub-type": "normal-metric",
           |        "index-pattern": "tdq.$${flink.app.profile}.metric.normal."
           |      }
           |    }
           |  ]
           |}
           |""".stripMargin,
      events = List(
        getTdqEvent("2021-05-29 12:00:00", pageId = 711, itm = "1"),
        getTdqEvent("2021-05-29 12:00:02", pageId = 711, itm = "2"),
        getTdqEvent("2021-05-29 12:00:29", pageId = 711, itm = null),
        getTdqEvent("2021-05-29 12:01:29", pageId = 711, itm = ""),
        getTdqEvent("2021-05-29 12:01:30", pageId = 711, itm = "5"),
        getTdqEvent("2021-05-29 12:01:58", pageId = 711, itm = "a"),
        getTdqEvent("2021-05-29 12:01:59", pageId = 711, itm = "7"),
        getTdqEvent("2021-05-29 12:03:19", pageId = 711, itm = "7"),
        getTdqEvent("2021-05-29 12:03:19", pageId = 1677718, itm = "7"),
        getTdqEvent("2021-05-29 12:03:59", pageId = 1677718, itm = "")
      ),
      expects = List(
        getMetric(id, time = "2021-05-29 12:01:59", tagK = "page_id", tagV = "711", v = 4d / 7d),
        getMetric(id, time = "2021-05-29 12:03:59", tagK = "page_id", tagV = "711", v = 1d / 1d),
        getMetric(id, time = "2021-05-29 12:03:59", tagK = "page_id", tagV = "1677718", v = 1d / 2d)
      )
    )
  }
}
