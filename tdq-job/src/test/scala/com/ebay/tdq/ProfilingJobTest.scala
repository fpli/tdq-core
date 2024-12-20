package com.ebay.tdq

import com.ebay.sojourner.common.env.EnvironmentUtils
import com.ebay.tdq.RawEventTest.getTdqEvent
import com.ebay.tdq.common.model.InternalMetric
import org.apache.commons.lang.time.DateUtils
import org.junit.{Assert, Test}

/**
 * @author juntzhang
 */
class SingleRuleSqlJobTest {

  import TestMetricFactory._

  @Test
  def test_sum_by_page_id(): Unit = {
    val id = "test_sum_by_page_id"
    ProfilingJobIT(
      id = id,
      config =
        s"""
           |{
           |  "id": "1",
           |  "name": "$id",
           |  "sources": [{"name":"$id","type":"realtime.memory","config":{"a.b":123,"c":{"c.a.d":1}}}],
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
           |      "name": "hdfs_sojevent_tdq_normal_metric",
           |      "type": "realtime.hdfs",
           |      "config": {
           |        "sub-type": "normal-metric",
           |        "rheos-services-urls": "https://rheos-services.qa.ebay.com",
           |        "schema-subject": "tdq.metric",
           |        "hdfs-path": "target/$${flink.app.profile}/metric/normal"
           |      }
           |    },
           |    {
           |      "name": "console_sojevent_tdq_normal_metric",
           |      "type": "realtime.console",
           |      "config": {
           |        "sub-type": "normal-metric",
           |        "std-name": "nor@mal"
           |      }
           |    },
           |    {
           |      "name": "console_sojevent_tdq_normal_metric",
           |      "type": "realtime.memory",
           |      "config": {
           |        "sub-type": "normal-metric"
           |      }
           |    }
           |  ],
           |  "env": {
           |    "config": {
           |      "flink.app.window.metric-1st-aggr": "10s",
           |      "flink.app.local-aggr.queue-size": 0,
           |      "flink.app.local-aggr.flush-timeout": "5s",
           |      "flink.app.local-aggr.output-partitions": 2,
           |      "flink.app.parallelism.metric-1st-aggr": 2,
           |      "flink.app.parallelism.metric-2nd-aggr": 2
           |    }
           |  }
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
  def test_avg_casewhen(): Unit = {
    val id = "test_avg_casewhen"
    ProfilingJobIT(
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
           |          "expression": {"operator": "Expr", "config": {"text": "itm_valid_cnt / itm_cnt"}},
           |          "transformations": [
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
           |  ],
           |  "sinks": [
           |    {
           |      "name": "hdfs_sojevent_tdq_normal_metric",
           |      "type": "realtime.hdfs",
           |      "config": {
           |        "sub-type": "normal-metric",
           |        "rheos-services-urls": "https://rheos-services.qa.ebay.com",
           |        "schema-subject": "tdq.metric",
           |        "hdfs-path": "target/$${flink.app.profile}/metric/normal"
           |      }
           |    },
           |    {
           |      "name": "console_sojevent_tdq_normal_metric",
           |      "type": "realtime.console",
           |      "config": {
           |        "sub-type": "normal-metric",
           |        "std-name": "nor@mal"
           |      }
           |    },
           |    {
           |      "name": "console_sojevent_tdq_normal_metric",
           |      "type": "realtime.memory",
           |      "config": {
           |        "sub-type": "normal-metric"
           |      }
           |    }
           |  ],
           |  "env": {
           |    "config": {
           |      "flink.app.window.metric-1st-aggr": "10s",
           |      "flink.app.local-aggr.queue-size": 0,
           |      "flink.app.local-aggr.flush-timeout": "5s",
           |      "flink.app.local-aggr.output-partitions": 2,
           |      "flink.app.parallelism.metric-1st-aggr": 2,
           |      "flink.app.parallelism.metric-2nd-aggr": 2
           |    }
           |  }
           |}
           |""".stripMargin,
      events = List(
        getTdqEvent("2021-05-29 12:00:00", itm = "1"),
        getTdqEvent("2021-05-29 12:00:02", itm = "2"),
        getTdqEvent("2021-05-29 12:00:29", itm = null),
        getTdqEvent("2021-05-29 12:01:29", itm = ""),
        getTdqEvent("2021-05-29 12:01:30", itm = "5"),
        getTdqEvent("2021-05-29 12:01:58", itm = "a"),
        getTdqEvent("2021-05-29 12:01:59", itm = "7")
      ),
      expects = List(
        getMetric(id, time = "2021-05-29 12:01:59", v = 4d / 7d)
      )
    )
  }

  @Test
  def test_count_by_domain(): Unit = {
    val id = "test_count_by_domain"
    val nullEvent = getTdqEvent("2021-05-29 12:01:51")
    nullEvent.remote("p")
    ProfilingJobIT(
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
           |          "dimensions": ["domain"],
           |          "expression": {"operator": "Expr", "config": {"text": "pv"}},
           |          "transformations": [
           |            {
           |              "alias": "domain",
           |              "expression": {
           |                "operator": "UDF",
           |                "config": {"text": " SOJ_PAGE_FAMILY(CAST( SOJ_NVL('p') AS INTEGER))"}
           |              }
           |            },
           |            {
           |              "alias": "pv",
           |              "expression": {"operator": "Count", "config": {"arg0": "1.0"}}
           |            }
           |          ]
           |        }
           |      ]
           |    }
           |  ],
           |  "sinks": [
           |    {
           |      "name": "hdfs_sojevent_tdq_normal_metric",
           |      "type": "realtime.hdfs",
           |      "config": {
           |        "sub-type": "normal-metric",
           |        "rheos-services-urls": "https://rheos-services.qa.ebay.com",
           |        "schema-subject": "tdq.metric",
           |        "hdfs-path": "target/$${flink.app.profile}/metric/normal"
           |      }
           |    },
           |    {
           |      "name": "console_sojevent_tdq_normal_metric",
           |      "type": "realtime.console",
           |      "config": {
           |        "sub-type": "normal-metric",
           |        "std-name": "nor@mal"
           |      }
           |    },
           |    {
           |      "name": "console_sojevent_tdq_normal_metric",
           |      "type": "realtime.memory",
           |      "config": {
           |        "sub-type": "normal-metric"
           |      }
           |    }
           |  ],
           |  "env": {
           |    "config": {
           |      "flink.app.window.metric-1st-aggr": "10s",
           |      "flink.app.local-aggr.queue-size": 0,
           |      "flink.app.local-aggr.flush-timeout": "5s",
           |      "flink.app.local-aggr.output-partitions": 2,
           |      "flink.app.parallelism.metric-1st-aggr": 2,
           |      "flink.app.parallelism.metric-2nd-aggr": 2
           |    }
           |  }
           |}
           |""".stripMargin,
      events = List(
        getTdqEvent("2021-05-29 12:01:50", pageId = 711),
        nullEvent,
        getTdqEvent("2021-05-29 12:01:52", pageId = 711),
        getTdqEvent("2021-05-29 12:01:53", pageId = 1677718),
        getTdqEvent("2021-05-29 12:01:59", pageId = 1),
        getTdqEvent("2021-05-29 12:01:59", pageId = 1677718),
        getTdqEvent("2021-05-29 12:01:59", pageId = 711)
      ),
      expects = List(
        getMetric(id, time = "2021-05-29 12:01:59", tagK = "domain", tagV = "ASQ", v = 3d),
        getMetric(id, time = "2021-05-29 12:01:59", tagK = "domain", tagV = "VI", v = 2d),
        getMetric(id, time = "2021-05-29 12:01:59", tagK = "domain", tagV = "NOTI", v = 1d),
        getMetric(id, time = "2021-05-29 12:01:59", tagK = "domain", tagV = "BID", v = 1d)
      )
    )
  }

  @Test
  def test_count_by_page_id(): Unit = {
    val id = "test_count_by_page_id"
    ProfilingJobIT(
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
           |          "expression": {"operator": "Expr", "config": {"text": "page_cnt"}},
           |          "transformations": [
           |            {
           |              "alias": "page_id",
           |              "expression": {
           |                "operator": "UDF",
           |                "config": {"text": "CAST( SOJ_NVL('p') AS INTEGER)"}
           |              }
           |            },
           |            {
           |              "alias": "page_cnt",
           |              "expression": {"operator": "Count", "config": {"arg0": "1.0"}}
           |            }
           |          ]
           |        }
           |      ]
           |    }
           |  ],
           |  "sinks": [
           |    {
           |      "name": "hdfs_sojevent_tdq_normal_metric",
           |      "type": "realtime.hdfs",
           |      "config": {
           |        "sub-type": "normal-metric",
           |        "rheos-services-urls": "https://rheos-services.qa.ebay.com",
           |        "schema-subject": "tdq.metric",
           |        "hdfs-path": "target/$${flink.app.profile}/metric/normal"
           |      }
           |    },
           |    {
           |      "name": "console_sojevent_tdq_normal_metric",
           |      "type": "realtime.console",
           |      "config": {
           |        "sub-type": "normal-metric",
           |        "std-name": "nor@mal"
           |      }
           |    },
           |    {
           |      "name": "console_sojevent_tdq_normal_metric",
           |      "type": "realtime.memory",
           |      "config": {
           |        "sub-type": "normal-metric"
           |      }
           |    }
           |  ],
           |  "env": {
           |    "config": {
           |      "flink.app.window.metric-1st-aggr": "10s",
           |      "flink.app.local-aggr.queue-size": 0,
           |      "flink.app.local-aggr.flush-timeout": "5s",
           |      "flink.app.local-aggr.output-partitions": 2,
           |      "flink.app.parallelism.metric-1st-aggr": 2,
           |      "flink.app.parallelism.metric-2nd-aggr": 2
           |    }
           |  }
           |}
           |""".stripMargin,
      events = List(
        getTdqEvent("2021-05-29 12:00:00", pageId = 1),
        getTdqEvent("2021-05-29 12:01:02", pageId = 1),
        getTdqEvent("2021-05-29 12:01:59", pageId = 2),
        getTdqEvent("2021-05-29 12:01:59", pageId = 1),

        getTdqEvent("2021-05-29 12:02:00", pageId = 2),
        getTdqEvent("2021-05-29 12:03:59", pageId = 2),

        getTdqEvent("2021-05-29 12:04:01", pageId = 2)
      ),
      expects = List(
        getMetric(id, "2021-05-29 12:01:59", tagK = "page_id", tagV = "1", 3d),
        getMetric(id, "2021-05-29 12:01:59", tagK = "page_id", tagV = "2", 1d),
        getMetric(id, "2021-05-29 12:03:59", tagK = "page_id", tagV = "2", 2d),
        getMetric(id, "2021-05-29 12:05:59", tagK = "page_id", tagV = "2", 1d)
      )
    )
  }

  @Test
  def test_max_min(): Unit = {
    val id = "test_max_min"
    ProfilingJobIT(
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
           |          "expression": {"operator": "Expr", "config": {"text": "t_duration_max - t_duration_min"}},
           |          "transformations": [
           |            {
           |              "alias": "t_duration",
           |              "expression": {
           |                "operator": "Expr",
           |                "config": {"text": "CAST( SOJ_NVL('TDuration') AS DOUBLE)"}
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
           |  ],
           |  "sinks": [
           |    {
           |      "name": "hdfs_sojevent_tdq_normal_metric",
           |      "type": "realtime.hdfs",
           |      "config": {
           |        "sub-type": "normal-metric",
           |        "rheos-services-urls": "https://rheos-services.qa.ebay.com",
           |        "schema-subject": "tdq.metric",
           |        "hdfs-path": "target/$${flink.app.profile}/metric/normal"
           |      }
           |    },
           |    {
           |      "name": "console_sojevent_tdq_normal_metric",
           |      "type": "realtime.console",
           |      "config": {
           |        "sub-type": "normal-metric",
           |        "std-name": "nor@mal"
           |      }
           |    },
           |    {
           |      "name": "console_sojevent_tdq_normal_metric",
           |      "type": "realtime.memory",
           |      "config": {
           |        "sub-type": "normal-metric"
           |      }
           |    }
           |  ],
           |  "env": {
           |    "config": {
           |      "flink.app.window.metric-1st-aggr": "10s",
           |      "flink.app.local-aggr.queue-size": 0,
           |      "flink.app.local-aggr.flush-timeout": "5s",
           |      "flink.app.local-aggr.output-partitions": 2,
           |      "flink.app.parallelism.metric-1st-aggr": 2,
           |      "flink.app.parallelism.metric-2nd-aggr": 2
           |    }
           |  }
           |}
           |""".stripMargin,
      events = List(
        getTdqEvent("2021-05-29 12:00:00", tDuration = 1d),
        getTdqEvent("2021-05-29 12:01:02", tDuration = 2d),
        getTdqEvent("2021-05-29 12:01:59", tDuration = 3d),
        getTdqEvent("2021-05-29 12:01:59", tDuration = 4d),
        getTdqEvent("2021-05-29 12:03:59", tDuration = 5d),
        getTdqEvent("2021-05-29 12:03:59", tDuration = 6d),
        getTdqEvent("2021-05-29 12:03:59", tDuration = 10d)
      ),
      expects = List(
        getMetric(id, "2021-05-29 12:01:59", 4d - 1d),
        getMetric(id, "2021-05-29 12:03:59", 10d - 5d)
      )
    )
  }

  @Test
  def test_max_min_by_page_id(): Unit = {
    val id = "test_max_min_by_page_id"
    ProfilingJobIT(
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
           |          "expression": {"operator": "Expr", "config": {"text": "t_duration_max - t_duration_min"}},
           |          "transformations": [
           |           {
           |              "alias": "page_id",
           |              "expression": {"operator": "UDF", "config": {"text": "CAST( SOJ_NVL('p') AS INTEGER)"}}
           |            },
           |            {
           |              "alias": "t_duration",
           |              "expression": {
           |                "operator": "Expr",
           |                "config": {"text": "CAST( SOJ_NVL('TDuration') AS DOUBLE)"}
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
           |  ],
           |  "sinks": [
           |    {
           |      "name": "hdfs_sojevent_tdq_normal_metric",
           |      "type": "realtime.hdfs",
           |      "config": {
           |        "sub-type": "normal-metric",
           |        "rheos-services-urls": "https://rheos-services.qa.ebay.com",
           |        "schema-subject": "tdq.metric",
           |        "hdfs-path": "target/$${flink.app.profile}/metric/normal"
           |      }
           |    },
           |    {
           |      "name": "console_sojevent_tdq_normal_metric",
           |      "type": "realtime.console",
           |      "config": {
           |        "sub-type": "normal-metric",
           |        "std-name": "nor@mal"
           |      }
           |    },
           |    {
           |      "name": "console_sojevent_tdq_normal_metric",
           |      "type": "realtime.memory",
           |      "config": {
           |        "sub-type": "normal-metric"
           |      }
           |    }
           |  ],
           |  "env": {
           |    "config": {
           |      "flink.app.window.metric-1st-aggr": "10s",
           |      "flink.app.local-aggr.queue-size": 0,
           |      "flink.app.local-aggr.flush-timeout": "5s",
           |      "flink.app.local-aggr.output-partitions": 2,
           |      "flink.app.parallelism.metric-1st-aggr": 2,
           |      "flink.app.parallelism.metric-2nd-aggr": 2
           |    }
           |  }
           |}
           |""".stripMargin,
      events = List(
        getTdqEvent("2021-05-29 12:00:00", pageId = 1, tDuration = 1d),
        getTdqEvent("2021-05-29 12:01:02", pageId = 2, tDuration = 2d),
        getTdqEvent("2021-05-29 12:01:59", pageId = 1, tDuration = 3d),
        getTdqEvent("2021-05-29 12:01:59", pageId = 2, tDuration = 4d),
        getTdqEvent("2021-05-29 12:01:59", pageId = 1, tDuration = 5d),
        getTdqEvent("2021-05-29 12:01:59", pageId = 1, tDuration = 6d),
        getTdqEvent("2021-05-29 12:01:59", pageId = 2, tDuration = 10d)
      ),
      expects = List(
        getMetric(id, "2021-05-29 12:01:59", tagK = "page_id", tagV = "1", 6d - 1d),
        getMetric(id, "2021-05-29 12:01:59", tagK = "page_id", tagV = "2", 10d - 2d)
      )
    )
  }


  @Test
  def test_global_cnt_by_10min(): Unit = {
    val id = "global_cnt_by_10min"
    ProfilingJobIT(
      id = id,
      config =
        s"""
           |{
           |  "id": "8",
           |  "name": "$id",
           |  "sources": [{"name":"$id","type":"realtime.memory","config":{}}],
           |  "rules": [
           |    {
           |      "name": "rule_8",
           |      "type": "realtime.rheos.profiler",
           |      "config": {
           |        "window": "10min"
           |      },
           |      "profilers": [
           |        {
           |          "metric-name": "global_cnt_by_10min",
           |          "expression": {"operator": "Expr", "config": {"text": "p1"}},
           |          "transformations": [
           |            {
           |              "alias": "p1",
           |              "expression": {"operator": "Count", "config": {"arg0": "1.0"}}
           |            }
           |          ]
           |        }
           |      ]
           |    }
           |  ],
           |  "sinks": [
           |    {
           |      "name": "hdfs_sojevent_tdq_normal_metric",
           |      "type": "realtime.hdfs",
           |      "config": {
           |        "sub-type": "normal-metric",
           |        "rheos-services-urls": "https://rheos-services.qa.ebay.com",
           |        "schema-subject": "tdq.metric",
           |        "hdfs-path": "target/$${flink.app.profile}/metric/normal"
           |      }
           |    },
           |    {
           |      "name": "console_sojevent_tdq_normal_metric",
           |      "type": "realtime.console",
           |      "config": {
           |        "sub-type": "normal-metric",
           |        "std-name": "nor@mal"
           |      }
           |    },
           |    {
           |      "name": "console_sojevent_tdq_normal_metric",
           |      "type": "realtime.memory",
           |      "config": {
           |        "sub-type": "normal-metric"
           |      }
           |    }
           |  ],
           |  "env": {
           |    "config": {
           |      "flink.app.window.metric-1st-aggr": "10s",
           |      "flink.app.local-aggr.queue-size": 0,
           |      "flink.app.local-aggr.flush-timeout": "5s",
           |      "flink.app.local-aggr.output-partitions": 2,
           |      "flink.app.parallelism.metric-1st-aggr": 2,
           |      "flink.app.parallelism.metric-2nd-aggr": 2
           |    }
           |  }
           |}
           |""".stripMargin,
      events = List(
        getTdqEvent("2021-05-29 12:01:50", pageId = 1),
        getTdqEvent("2021-05-29 12:01:52", pageId = 1),
        getTdqEvent("2021-05-29 12:01:59", pageId = 2),
        getTdqEvent("2021-05-29 12:01:59", pageId = 1),

        getTdqEvent("2021-05-29 12:12:00", pageId = 2),
        getTdqEvent("2021-05-29 12:13:59", pageId = 2),

        getTdqEvent("2021-05-29 12:24:01", pageId = 2)
      ),
      expects = List(
        getMetric(id, "2021-05-29 12:09:59", 4d),
        getMetric(id, "2021-05-29 12:19:59", 2d),
        getMetric(id, "2021-05-29 12:29:59", 1d)
      )
    )

    Assert.assertTrue(
      EnvironmentUtils.replaceStringWithPattern("/tmp/${flink.app.name}/checkpoint") == s"/tmp/$id/checkpoint")
  }


}

object TestMetricFactory {
  def getMetric(metricKey: String, time: String, v: Double): InternalMetric = {
    val t = DateUtils.parseDate(time, Array[String]("yyyy-MM-dd HH:mm:ss")).getTime
    new InternalMetric(metricKey, t)
      .genMetricId
      .setValue(v)
  }

  def getMetric(metricKey: String, time: String, tagK: String, tagV: String, v: Double): InternalMetric = {
    val t = DateUtils.parseDate(time, Array[String]("yyyy-MM-dd HH:mm:ss")).getTime
    new InternalMetric(metricKey, t)
      .putTag(tagK, tagV)
      .genMetricId
      .setValue(v)
  }
}