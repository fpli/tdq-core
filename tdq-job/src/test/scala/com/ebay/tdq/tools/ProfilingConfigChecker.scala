package com.ebay.tdq.tools

/**
 * @author juntzhang
 */
object ProfilingConfigChecker {
  def main(args: Array[String]): Unit = {
    //    testLocalPathFinder()
    //    testKafkaPathFinder()
    testKafkaUtp()
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
          |      "expr": "count1(1)",
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
