package com.ebay.tdq

import com.ebay.tdq.RawEventTest.getRawEvent
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
           |  ]
           |}
           |""".stripMargin,
      events = List(
        getRawEvent("2021-05-29 12:00:00", contentLength = 31, pageId = 1, tDuration = 1d),
        getRawEvent("2021-05-29 12:01:02", contentLength = 29, pageId = 2, tDuration = 2d), //ignore
        getRawEvent("2021-05-29 12:01:59", contentLength = 31, pageId = 1, tDuration = 3d),
        getRawEvent("2021-05-29 12:01:59", contentLength = 31, pageId = 2, tDuration = 3d),
        getRawEvent("2021-05-29 12:02:00", contentLength = 31, pageId = 1, tDuration = 5d),
        getRawEvent("2021-05-29 12:03:59", contentLength = 31, pageId = 1, tDuration = 6d),
        getRawEvent("2021-05-29 12:04:00", contentLength = 31, pageId = 2, tDuration = 7d)
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
           |  ]
           |}
           |""".stripMargin,
      events = List(
        getRawEvent("2021-05-29 12:00:00", pageId = 711, itm = "1"),
        getRawEvent("2021-05-29 12:00:02", pageId = 711, itm = "2"),
        getRawEvent("2021-05-29 12:00:29", pageId = 711, itm = null),
        getRawEvent("2021-05-29 12:01:29", pageId = 711, itm = ""),
        getRawEvent("2021-05-29 12:01:30", pageId = 711, itm = "5"),
        getRawEvent("2021-05-29 12:01:58", pageId = 711, itm = "a"),
        getRawEvent("2021-05-29 12:01:59", pageId = 711, itm = "7"),
        getRawEvent("2021-05-29 12:03:19", pageId = 711, itm = "7"),
        getRawEvent("2021-05-29 12:03:19", pageId = 1677718, itm = "7"),
        getRawEvent("2021-05-29 12:03:59", pageId = 1677718, itm = "")
      ),
      expects = List(
        getMetric(id, time = "2021-05-29 12:01:59", tagK = "page_id", tagV = "711", v = 4d / 7d),
        getMetric(id, time = "2021-05-29 12:03:59", tagK = "page_id", tagV = "711", v = 1d / 1d),
        getMetric(id, time = "2021-05-29 12:03:59", tagK = "page_id", tagV = "1677718", v = 1d / 2d)
      )
    )
  }
}
