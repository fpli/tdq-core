package com.ebay.tdq.tools

/**
 * @author juntzhang
 */
object ProfilingConfigChecker {
  def main(args: Array[String]): Unit = {
    //    testLocalPathFinder()
    //    testKafkaPathFinder()
    //    testKafkaUtp()
    testKafkaAutoTracking()
  }

  def testKafkaAutoTracking(): Unit = {
    ProfilingKafkaAutoTrackingIT(
      profiler =
        """
          |{
          |  "expr": "total_cnt",
          |  "metric-name": "system_metric",
          |  "dimensions": ["site_id"],
          |  "transformations": [
          |    {
          |      "expr": "count(1)",
          |      "alias": "total_cnt"
          |    },
          |    {
          |      "expr": "autoTrackEvent.userId",
          |      "alias": "user_id"
          |    },
          |    {
          |      "expr": "autoTrackEvent.siteId",
          |      "alias": "site_id"
          |    }
          |  ]
          |}
          |""".stripMargin)
      .test()
  }

  def testKafkaUtp(): Unit = {
    ProfilingKafkaUtpIT(
      profiler =
        """
          |{
          |  "expr": "total_cnt",
          |  "metric-name": "system_metric",
          |  "transformations": [
          |    {
          |      "expr": "count(1)",
          |      "alias": "total_cnt"
          |    }
          |  ]
          |}
          |""".stripMargin)
      .test()
  }

  def testLocalPathFinder(): Unit = {
    ProfilingLocalPathfinderJobIT(
      """
        |{
        |  "expr": "total_cnt",
        |  "metric-name": "system_metric",
        |  "transformations": [
        |    {
        |      "expr": "count(1)",
        |      "alias": "total_cnt"
        |    }
        |  ]
        |}
        |""".stripMargin).test()
  }

  // need wait for 3min
  def testKafkaPathFinder(): Unit = {
    ProfilingKafkaPathfinderJobIT(
      """
        |{
        |  "expr": "total_cnt",
        |  "metric-name": "system_metric",
        |  "transformations": [
        |    {
        |      "expr": "count(1)",
        |      "alias": "total_cnt"
        |    }
        |  ]
        |}
        |""".stripMargin)
      .test()
  }
}
