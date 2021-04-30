package com.ebay.tdq

import com.ebay.tdq.RawEventTest.getRawEvent

/**
 * @author juntzhang
 */
class ProntoSinkJobITCase extends SingleRuleSqlJobITCase {
  "test_sum_by_page_id_sink2pronto" in { param =>
    val id = param.name
    RheosJobTest(
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
           |              "expression": {"operator": "UDF", "config": {"text": "CAST(TAG_EXTRACT('p') AS INTEGER)"}}
           |            },
           |            {
           |              "alias": "t_duration",
           |              "expression": {
           |                "operator": "Expr",
           |                "config": {"text": "CAST(TAG_EXTRACT('TDuration') AS DOUBLE)"}
           |              }
           |            },
           |            {
           |              "alias": "t_duration_sum",
           |              "expression": {"operator": "SUM", "config": {"arg0": "t_duration"}}
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
        getRawEvent("2021-05-29 12:04:01", contentLength = 31, pageId = 2, tDuration = 7d)
      ),
      expects = List(
        getMetric(id, "2021-05-29 12:01:59", tagK = "page_id", tagV = "1", 4d),
        getMetric(id, "2021-05-29 12:01:59", tagK = "page_id", tagV = "2", 3d),
        getMetric(id, "2021-05-29 12:03:59", tagK = "page_id", tagV = "1", 11d),
        getMetric(id, "2021-05-29 12:04:01", tagK = "page_id", tagV = "2", 7d)
      )
    ).submit2Pronto()
  }
}
