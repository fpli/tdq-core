package com.ebay.tdq

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, ObjectInputStream, ObjectOutputStream}

import com.ebay.tdq.common.model.TdqEventTime
import org.apache.avro.Schema.Type
import org.junit.{Assert, Test}

/**
 * @author juntzhang
 */
class TdqEventTimeTest {
  @Test
  def test_serializable(): Unit = {
    val t = new TdqEventTime("time", Type.LONG)
    val buff = new ByteArrayOutputStream()
    val out = new ObjectOutputStream(buff)
    out.writeObject(t)
    out.close()


    val in = new ObjectInputStream(new ByteArrayInputStream(buff.toByteArray))
    val t2 = in.readObject.asInstanceOf[TdqEventTime]
    in.close()


    Assert.assertEquals(t.getExpression, t2.getExpression)
    Assert.assertEquals(t.getType, t2.getType)
  }
}
