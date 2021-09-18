package com.ebay.tdq.tools

/**
 * @author juntzhang
 */
object ProfilingConfigChecker {
  def main(args: Array[String]): Unit = {
    //    testKafkaPF()
    //    testLocalPF()
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
          |      "expr": "count(1)",
          |      "alias": "total_cnt"
          |    }
          |  ]
          |}
          |""".stripMargin)
      .test()
  }

  def testLocalPF(): Unit = {
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

  def testKafkaPF(): Unit = {
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
