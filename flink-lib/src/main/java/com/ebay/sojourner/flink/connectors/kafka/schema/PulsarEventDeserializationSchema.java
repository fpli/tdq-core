package com.ebay.sojourner.flink.connectors.kafka.schema;

import com.ebay.sojourner.common.model.PulsarEvent;
import com.ebay.sojourner.flink.connectors.kafka.RheosEventSerdeFactory;
import io.ebay.rheos.schema.event.RheosEvent;
import java.io.IOException;
import org.apache.avro.generic.GenericRecord;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;

public class PulsarEventDeserializationSchema implements DeserializationSchema<PulsarEvent> {

  @Override
  public PulsarEvent deserialize(byte[] message) throws IOException {
    RheosEvent rheosEvent =
        RheosEventSerdeFactory.getRheosEventHeaderDeserializer().deserialize(null, message);
    GenericRecord genericRecord =
        RheosEventSerdeFactory.getRheosEventDeserializer().decode(rheosEvent);

    long eventCreateTimestamp = rheosEvent.getEventCreateTimestamp();
    String si = getString(genericRecord.get("si"));
    String rq = getString(genericRecord.get("rq"));
    String gci = getString(genericRecord.get("gci"));
    String ai = getString(genericRecord.get("ai"));
    String ac = getString(genericRecord.get("ac"));
    String di = getString(genericRecord.get("di"));
    Long ct = getLong(genericRecord.get("ct"));
    Integer dy = getInteger(genericRecord.get("dy"));
    String ctz = getString(genericRecord.get("ctz"));
    String et = getString(genericRecord.get("et"));
    String tn = getString(genericRecord.get("tn"));
    String or = getString(genericRecord.get("or"));
    String gf = getString(genericRecord.get("gf"));
    String ef = getString(genericRecord.get("ef"));
    Long ei = getLong(genericRecord.get("ei"));
    String ipv4 = getString(genericRecord.get("ipv4"));
    String ipv6 = getString(genericRecord.get("ipv6"));
    String ua = getString(genericRecord.get("ua"));
    String lac = getString(genericRecord.get("lac"));
    String lc = getString(genericRecord.get("lc"));
    String pr = getString(genericRecord.get("pr"));
    String rf = getString(genericRecord.get("rf"));
    String ch = getString(genericRecord.get("ch"));
    String exg = getString(genericRecord.get("exg"));
    String exe = getString(genericRecord.get("exe"));
    String exa = getString(genericRecord.get("exa"));
    String exz = getString(genericRecord.get("exz"));
    String exs = getString(genericRecord.get("exs"));
    String exv = getString(genericRecord.get("exv"));
    String r0 = getString(genericRecord.get("r0"));
    String r1 = getString(genericRecord.get("r1"));
    String url = getString(genericRecord.get("url"));
    String hrc = getString(genericRecord.get("hrc"));
    String dd_bv = getString(genericRecord.get("dd_bv"));
    String dd_bf = getString(genericRecord.get("dd_bf"));
    String dd_osv = getString(genericRecord.get("dd_osv"));
    String dd_os = getString(genericRecord.get("dd_os"));
    String dd_dc = getString(genericRecord.get("dd_dc"));
    String dd_d = getString(genericRecord.get("dd_d"));
    String cty = getString(genericRecord.get("cty"));
    String rgn = getString(genericRecord.get("rgn"));
    String cn = getString(genericRecord.get("cn"));
    String con = getString(genericRecord.get("con"));
    String lat = getString(genericRecord.get("lat"));
    String lon = getString(genericRecord.get("lon"));
    String chl = getString(genericRecord.get("chl"));
    String ptnr = getString(genericRecord.get("ptnr"));
    String kwd = getString(genericRecord.get("kwd"));
    String kw = getString(genericRecord.get("kw"));
    String plm = getString(genericRecord.get("plm"));
    String msg = getString(genericRecord.get("msg"));
    String cpn = getString(genericRecord.get("cpn"));
    String pge = getString(genericRecord.get("pge"));
    String mdl = getString(genericRecord.get("mdl"));
    String lnk = getString(genericRecord.get("lnk"));
    Boolean bot = getBoolean(genericRecord.get("bot"));

    return new PulsarEvent(eventCreateTimestamp, si, rq, gci, ai, ac, di, ct, dy, ctz, et, tn, or,
        gf, ef, ei, pr, ipv4, ipv6, ua, lac, lc, rf, ch, exg, exe, exa, exz, exs, exv, r0, r1, url,
        hrc, dd_bv, dd_bf, dd_osv, dd_os, dd_dc, dd_d, cty, rgn, cn, con, lat, lon, chl, ptnr, kwd,
        kw, plm, msg, cpn, pge, mdl, lnk, bot);
  }

  @Override
  public boolean isEndOfStream(PulsarEvent nextElement) {
    return false;
  }

  @Override
  public TypeInformation<PulsarEvent> getProducedType() {
    return TypeInformation.of(PulsarEvent.class);
  }

  private Integer getInteger(Object o) {
    if (StringUtils.isEmpty(getString(o))) {
      return null;
    } else {
      return Integer.valueOf(getString(o));
    }
  }

  private boolean getBoolean(Object o) {
    if (StringUtils.isEmpty(getString(o))) {
      return false;
    } else {
      return Boolean.valueOf(getString(o));
    }
  }

  private Long getLong(Object o) {
    if (StringUtils.isEmpty(getString(o))) {
      return null;
    } else {
      return Long.valueOf(getString(o));
    }
  }

  private String getString(Object o) {
    return (o != null && !"null".equals(o.toString())) ? o.toString() : null;
  }
}
