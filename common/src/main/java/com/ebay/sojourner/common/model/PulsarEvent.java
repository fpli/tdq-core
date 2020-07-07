package com.ebay.sojourner.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PulsarEvent {

   private Long eventCreateTimestamp;
   private String si;
   private String rq;
   private String gci;
   private String ai;
   private String ac;
   private String di;
   private Long ct;
   private Integer dy;
   private String ctz;
   private String et;
   private String tn;
   private String or;
   private String gf;
   private String ef;
   private Long ei;
   private String pr;
   private String ipv4;
   private String ipv6;
   private String ua;
   private String lac;
   private String lc;
   private String rf;
   private String ch;
   private String exg;
   private String exe;
   private String exa;
   private String exz;
   private String exs;
   private String exv;
   private String r0;
   private String r1;
   private String url;
   private String hrc;
   private String dd_bv;
   private String dd_bf;
   private String dd_osv;
   private String dd_os;
   private String dd_dc;
   private String dd_d;
   private String cty;
   private String rgn;
   private String cn;
   private String con;
   private String lat;
   private String lon;
   private String chl;
   private String ptnr;
   private String kwd;
   private String kw;
   private String plm;
   private String msg;
   private String cpn;
   private String pge;
   private String mdl;
   private String lnk;
   private Boolean bot;
}
