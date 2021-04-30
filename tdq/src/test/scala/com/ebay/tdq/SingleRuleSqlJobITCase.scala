package com.ebay.tdq

import com.ebay.tdq.RawEventTest.getRawEvent
import com.ebay.tdq.rules.TdqMetric
import org.apache.commons.lang.time.DateUtils
import org.scalatest.fixture

/**
 * @author juntzhang
 */
class SingleRuleSqlJobITCase extends fixture.FreeSpec with fixture.TestDataFixture {
  "test_sum_by_page_id" in { param =>
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
    ).submit()
  }

  "test_avg_casewhen" in { param =>
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
           |          "expression": {"operator": "Expr", "config": {"text": "itm_valid_cnt / itm_cnt"}},
           |          "transformations": [
           |            {
           |              "alias": "item",
           |              "expression": {
           |                "operator": "UDF",
           |                "config": {"text": "CAST(TAG_EXTRACT('itm|itmid|itm_id|itmlist|litm') AS LONG)"}
           |              }
           |            },
           |            {
           |              "alias": "itm_valid_ind",
           |              "expression": {
           |                "operator": "Expr",
           |                "config": {
           |                  "text": "case when item is not null then 1 else 0 end"
           |                }
           |              }
           |            },
           |            {
           |              "alias": "itm_cnt",
           |              "expression": {"operator": "Count", "config": {"arg0": "1"}}
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
        getRawEvent("2021-05-29 12:00:00", itm = "1"),
        getRawEvent("2021-05-29 12:00:02", itm = "2"),
        getRawEvent("2021-05-29 12:00:29", itm = null),
        getRawEvent("2021-05-29 12:01:29", itm = ""),
        getRawEvent("2021-05-29 12:01:30", itm = "5"),
        getRawEvent("2021-05-29 12:01:58", itm = "a"),
        getRawEvent("2021-05-29 12:01:59", itm = "7")
      ),
      expects = List(
        getMetric(id, time = "2021-05-29 12:01:59", v = 4d / 7d)
      )
    ).submit()
  }

  "test_count_by_domain" in { param =>
    val id = param.name

    val nullEvent = getRawEvent("2021-05-29 12:01:59")
    nullEvent.getSojA.remove("p")
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
           |          "dimensions": ["domain"],
           |          "expression": {"operator": "Expr", "config": {"text": "pv"}},
           |          "transformations": [
           |            {
           |              "alias": "domain",
           |              "expression": {
           |                "operator": "UDF",
           |                "config": {"text": "PAGE_FAMILY(CAST(TAG_EXTRACT('p') AS INTEGER))"}
           |              }
           |            },
           |            {
           |              "alias": "pv",
           |              "expression": {"operator": "Count", "config": {"arg0": "1"}}
           |            }
           |          ]
           |        }
           |      ]
           |    }
           |  ]
           |}
           |""".stripMargin,
      events = List(
        getRawEvent("2021-05-29 12:01:59", pageId = 711),
        nullEvent,
        getRawEvent("2021-05-29 12:01:59", pageId = 711),
        getRawEvent("2021-05-29 12:01:59", pageId = 1677718),
        getRawEvent("2021-05-29 12:01:59", pageId = 1),
        getRawEvent("2021-05-29 12:01:59", pageId = 1677718),
        getRawEvent("2021-05-29 12:01:59", pageId = 711)
      ),
      expects = List(
        getMetric(id, time = "2021-05-29 12:01:59", tagK = "domain", tagV = "ASQ", v = 3d),
        getMetric(id, time = "2021-05-29 12:01:59", tagK = "domain", tagV = "VI", v = 2d),
        getMetric(id, time = "2021-05-29 12:01:59", tagK = "domain", tagV = null, v = 2d)
      )
    ).submit()
  }

  "test_count_by_page_id" in { param =>
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
           |          "expression": {"operator": "Expr", "config": {"text": "page_cnt"}},
           |          "transformations": [
           |            {
           |              "alias": "page_id",
           |              "expression": {
           |                "operator": "UDF",
           |                "config": {"text": "CAST(TAG_EXTRACT('p') AS INTEGER)"}
           |              }
           |            },
           |            {
           |              "alias": "page_cnt",
           |              "expression": {"operator": "Count", "config": {"arg0": "1"}}
           |            }
           |          ]
           |        }
           |      ]
           |    }
           |  ]
           |}
           |""".stripMargin,
      events = List(
        getRawEvent("2021-05-29 12:00:00", pageId = 1),
        getRawEvent("2021-05-29 12:01:02", pageId = 1),
        getRawEvent("2021-05-29 12:01:59", pageId = 2),
        getRawEvent("2021-05-29 12:01:59", pageId = 1),

        getRawEvent("2021-05-29 12:02:00", pageId = 2),
        getRawEvent("2021-05-29 12:03:59", pageId = 2),

        getRawEvent("2021-05-29 12:04:01", pageId = 2)
      ),
      expects = List(
        getMetric(id, "2021-05-29 12:01:59", tagK = "page_id", tagV = "1", 3d),
        getMetric(id, "2021-05-29 12:01:59", tagK = "page_id", tagV = "2", 1d),
        getMetric(id, "2021-05-29 12:03:59", tagK = "page_id", tagV = "2", 2d),
        getMetric(id, "2021-05-29 12:04:01", tagK = "page_id", tagV = "2", 1d)
      )
    ).submit()
  }

  "test_max-min" in { param =>
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
           |          "expression": {"operator": "Expr", "config": {"text": "t_duration_max - t_duration_min"}},
           |          "transformations": [
           |            {
           |              "alias": "t_duration",
           |              "expression": {
           |                "operator": "Expr",
           |                "config": {"text": "CAST(TAG_EXTRACT('TDuration') AS DOUBLE)"}
           |              }
           |            },
           |            {
           |              "alias": "t_duration_min",
           |              "expression": {"operator": "Min", "config": {"arg0": "t_duration"}}
           |            },
           |            {
           |              "alias": "t_duration_max",
           |              "expression": {"operator": "Max", "config": {"arg0": "t_duration"}}
           |            }
           |          ]
           |        }
           |      ]
           |    }
           |  ]
           |}
           |""".stripMargin,
      events = List(
        getRawEvent("2021-05-29 12:00:00", tDuration = 1d),
        getRawEvent("2021-05-29 12:01:02", tDuration = 2d),
        getRawEvent("2021-05-29 12:01:59", tDuration = 3d),
        getRawEvent("2021-05-29 12:01:59", tDuration = 4d),
        getRawEvent("2021-05-29 12:03:59", tDuration = 5d),
        getRawEvent("2021-05-29 12:03:59", tDuration = 6d),
        getRawEvent("2021-05-29 12:03:59", tDuration = 10d)
      ),
      expects = List(
        getMetric(id, "2021-05-29 12:01:59", 3d),
        getMetric(id, "2021-05-29 12:03:59", 5d)
      )
    ).submit()
  }

  "test_max-min_by_page_id" in { param =>
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
           |          "expression": {"operator": "Expr", "config": {"text": "t_duration_max - t_duration_min"}},
           |          "transformations": [
           |           {
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
           |              "alias": "t_duration_min",
           |              "expression": {"operator": "Min", "config": {"arg0": "t_duration"}}
           |            },
           |            {
           |              "alias": "t_duration_max",
           |              "expression": {"operator": "Max", "config": {"arg0": "t_duration"}}
           |            }
           |          ]
           |        }
           |      ]
           |    }
           |  ]
           |}
           |""".stripMargin,
      events = List(
        getRawEvent("2021-05-29 12:00:00", pageId = 1, tDuration = 1d),
        getRawEvent("2021-05-29 12:01:02", pageId = 2, tDuration = 2d),
        getRawEvent("2021-05-29 12:01:59", pageId = 1, tDuration = 3d),
        getRawEvent("2021-05-29 12:01:59", pageId = 2, tDuration = 4d),
        getRawEvent("2021-05-29 12:01:59", pageId = 1, tDuration = 5d),
        getRawEvent("2021-05-29 12:01:59", pageId = 1, tDuration = 6d),
        getRawEvent("2021-05-29 12:01:59", pageId = 2, tDuration = 10d)
      ),
      expects = List(
        getMetric(id, "2021-05-29 12:01:59", tagK = "page_id", tagV = "1", 5d),
        getMetric(id, "2021-05-29 12:01:59", tagK = "page_id", tagV = "2", 8d)
      )
    ).submit()
  }


  protected def getMetric(metricKey: String, time: String, v: Double): TdqMetric = {
    val t = DateUtils.parseDate(time, Array[String]("yyyy-MM-dd HH:mm:ss")).getTime
    new TdqMetric(metricKey, t)
      .genUID
      .setValue(v)
  }

  protected def getMetric(metricKey: String, time: String, tagK: String, tagV: String, v: Double): TdqMetric = {
    val t = DateUtils.parseDate(time, Array[String]("yyyy-MM-dd HH:mm:ss")).getTime
    new TdqMetric(metricKey, t)
      .putTag(tagK, tagV)
      .genUID
      .setValue(v)
  }
}
