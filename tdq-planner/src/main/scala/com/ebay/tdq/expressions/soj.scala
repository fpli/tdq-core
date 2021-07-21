package com.ebay.tdq.expressions

import com.ebay.sojourner.common.model.{ClientData, RawEvent}
import com.ebay.sojourner.common.util.{LkpManager, SojUtils}
import com.ebay.tdq.types.{BooleanType, DataType, StringType}
import org.apache.commons.beanutils.PropertyUtils
import org.apache.commons.lang3.StringUtils

/**
 * @author juntzhang
 */
case class SojParseRlogid(subject: Expression, infoType: Expression, cacheKey: Option[String] = None) extends LeafExpression {
  val dataType = StringType

  override def nullable: Boolean = true

  protected override def eval(input: InternalRow): Any = new com.ebay.sojourner.common.util.SojParseRlogid()
    .evaluate(subject.call(input).asInstanceOf[String], infoType.call(input).asInstanceOf[String])
}

object SojTag {
  val SPLIT_DEL = "\\|"

  def eval(rawEvent: RawEvent, tag: String): String = {
    val tags = tag.split(SPLIT_DEL)
    for (t <- tags) {
      if (StringUtils.isNotBlank(rawEvent.getSojA.get(t))) return rawEvent.getSojA.get(t)
      if (StringUtils.isNotBlank(rawEvent.getSojC.get(t))) return rawEvent.getSojC.get(t)
      if (StringUtils.isNotBlank(rawEvent.getSojK.get(t))) return rawEvent.getSojK.get(t)
    }
    null
  }
}

case class SojTag(subject: GetRawEvent, tag: String, dataType: DataType, cacheKey: Option[String] = None) extends LeafExpression {
  override def nullable: Boolean = true

  protected override def eval(input: InternalRow): Any = {
    val rawEvent = subject.call(input).asInstanceOf[RawEvent]
    SojTag.eval(rawEvent, tag)
  }
}

case class SojNvl(subject: GetRawEvent, tag: String,
                  dataType: DataType, cacheKey: Option[String] = None) extends LeafExpression {
  override def nullable: Boolean = true

  protected override def eval(input: InternalRow): Any = {
    val rawEvent = subject.call(input).asInstanceOf[RawEvent]
    val v = SojTag.eval(rawEvent, tag)
    if (StringUtils.isNotBlank(v)) {
      v
    } else {
      val tags = tag.split(SojTag.SPLIT_DEL)
      for (t <- tags) {
        if (!ClientData.FIELDS.contains(t)) {
          return null
        }
        val v = PropertyUtils.getProperty(input.getCache("__RAW_EVENT"), s"clientData.$t")
        if (v != null) {
          return v
        }
      }
      null
    }
  }
}

case class SojPageFamily(subject: Expression, cacheKey: Option[String] = None) extends LeafExpression {
  override def nullable: Boolean = true

  override def dataType: DataType = StringType

  protected override def eval(input: InternalRow): Any = {
    SojUtils.getPageFmly(subject.call(input).asInstanceOf[Int])
  }
}

// p_alfu_t.bbwoa_pages_with_itm
case class IsBBWOAPageWithItm(subject: Expression, cacheKey: Option[String] = None) extends LeafExpression {
  override def nullable: Boolean = true

  override def dataType: DataType = BooleanType

  val pageIds = Set(2545343, 1468716, 2051312, 1596423, 2051585, 2265246, 5411, 1596424, 2064555, 2051963, 2047646, 2045327, 2284654, 2258337, 2056019, 2536268, 2060562, 5969, 1596422, 2056816, 2058919, 2545337, 2494950, 2503, 2495273, 2510220, 2561049, 1596428, 2484112, 2052206, 2494952, 4068, 2055453, 6014, 1596425, 2545340, 2058891, 2239238, 2485954, 2265244, 2050510, 1468715, 2536267, 5713, 2052207, 1596418, 5860, 1596443, 6111, 2046772, 3161660, 2523513, 5830, 2048325, 2050464, 1468710, 2486515, 2510218, 2051291, 2301752, 2239220, 1596437, 5372, 2052205, 2051964, 2490004, 2495288, 2054175, 2052204, 2045329, 5884, 2060497, 2510396, 2545342, 2484109, 1702886, 2064974, 2486575, 2510398, 3191123, 2058538, 2062351, 2486677, 2047119, 2760, 2016, 2056817, 1596427, 1596432, 2265243, 2545338, 2052200, 2056819, 2510400, 6110, 2046448, 2050451, 2041645, 2499, 2054900, 1596445, 2046771, 2056979, 4930, 167, 2052247, 2046964, 2046969, 2048308, 2052202, 2500857, 2053553, 2332075, 5401, 2051366, 2284653, 5414, 2065828, 2047937, 3296, 760, 6109, 4018, 3897, 3665, 3929, 2056986, 2171218, 2494951, 2062353, 2052178, 2545339, 2510395, 2552383, 3221897, 6024, 2304064, 2498, 3221898, 2054172, 5968, 2051290, 2759, 2494957, 2048321, 2053965, 2050447, 2258336, 1468718, 2494953, 2057885, 2495285, 1596441, 4006, 2041644, 2501, 2334216, 2334218, 3663, 2561045, 2171217, 2053898, 1596421, 1596440, 2507978, 711, 2057641, 2050509, 2334217, 2493973, 2056812, 2045328, 2053922, 2057886, 2484178, 2494956, 2545977, 2536265, 2810, 2057640, 2510217, 2045326, 2046018, 2530661, 2059107, 1596431, 2500, 1677972, 2510403, 2057189, 2493971, 2255925, 2494958, 2046552, 2053897, 2046551, 1468713, 2067189, 2042411, 2258800, 2368479, 2067188, 2051283, 2056815, 1468714, 2494767, 1596442, 3235385, 2046963, 2484110, 2047645, 2490003, 5374, 1596438, 2056814, 4925, 2355844, 1596434, 1596429, 5297, 2010, 5371, 2485302, 2057192, 2067187, 1596420, 2545100, 2304061, 2545345, 2055415, 2376473, 2053249, 2055526, 2486573, 2560101, 2486514, 2043012, 2050536, 2047120, 2806, 2064556, 2064796, 2566990, 5831, 2045325, 1468739, 5466, 2510402, 2317030, 573, 2053246, 2052286, 2679, 2566992, 2056818)

  protected override def eval(input: InternalRow): Any = {
//    val pageIds = LkpManager.getInstance.getItmPages
    pageIds.contains(subject.call(input).asInstanceOf[Int])
  }
}