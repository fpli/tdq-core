package com.ebay.tdq.planner.utils.udf.soj;

import java.io.Serializable;
import org.apache.commons.lang.StringUtils;

public class SojGetTopDomain implements Serializable {

  public String evaluate(String url_domain) {

    char dot = '.';

    if (StringUtils.isEmpty(url_domain)) {
      return null;
    }

    int len = url_domain.length();

    int numDot = 0;
    for (int i = 0; i < len; i++) {
      if (url_domain.charAt(i) == dot) {
        numDot++;
      }
    }

    if (numDot == 0) {
      return url_domain;
    }

    int sdLen = 0;

    if (numDot == 1) {
      int rDotIndex = url_domain.lastIndexOf(dot);
      sdLen = len - rDotIndex - 1;
      if (sdLen < 2) {
        return url_domain;
      } else if (sdLen == 2) {

        String sd = url_domain.substring(rDotIndex + 1);
        if (isalpha(sd.charAt(0))) {
          return url_domain.substring(0, rDotIndex);
        } else {
          return url_domain;
        }
      } else if (sdLen >= 3) {
        if (ebayVerifyGTLD(url_domain.substring(rDotIndex + 1)) == 1) {
          return url_domain.substring(0, rDotIndex);
        } else {
          return url_domain;
        }
      }
    } else if (numDot > 1) {
      int skip = 0;
      int rDotIndex = url_domain.lastIndexOf(dot);
      sdLen = len - rDotIndex - 1;
      if (sdLen < 2) {
        return url_domain;
      } else if (sdLen == 2) {
        String sd = url_domain.substring(rDotIndex + 1);
        if (isalpha(sd.charAt(0))) {
          skip = 1;
        } else {
          return url_domain;
        }
      } else if (ebayVerifyGTLD(url_domain.substring(rDotIndex + 1)) == 1) {
        skip = 2;
      } else {
        return url_domain;
      }

      String part2 = url_domain.substring(0, rDotIndex);
      int r2ndDotIndex = part2.lastIndexOf(dot);

      if (skip == 2) {
        return part2.substring(r2ndDotIndex + 1);
      }

      if (ebayVerifyGTLD(part2.substring(r2ndDotIndex + 1)) == 0) {
        return part2.substring(r2ndDotIndex + 1);
      }

      if (numDot > 2) {
        String part3 = part2.substring(0, r2ndDotIndex);
        int r3thDotIndex = part3.lastIndexOf(dot);
        return part3.substring(r3thDotIndex + 1);
      }
    }
    return null;
  }


  public int ebayVerifyGTLD(String gTLDin) {
    int isgTLD = 0;

    int gTLDlen = gTLDin.length();

    if (gTLDlen > 6) {
      return isgTLD;
    }

    String lowergTLDin = gTLDin.toLowerCase();
    char ch = lowergTLDin.charAt(0);

    switch (ch) {
      case 'c':
        if (gTLDlen == 3 && (lowergTLDin.substring(1, 3).equals("om") || lowergTLDin.substring(1).equals("at"))) {
          isgTLD = 1;
        } else if (gTLDlen == 4 && lowergTLDin.substring(1, 4).equals("oop")) {
          isgTLD = 1;
        } else if (gTLDlen == 2 && lowergTLDin.substring(1, 2).equals("o")) {
          isgTLD = 1;
        }
        break;
      case 'n':
        if (gTLDlen == 3 && lowergTLDin.substring(1, 3).equals("et")) {
          isgTLD = 1;
        } else if (gTLDlen == 4 && lowergTLDin.substring(1, 4).equals("ame")) {
          isgTLD = 1;
        } else if (gTLDlen == 2 && lowergTLDin.substring(1, 2).equals("e")) {
          isgTLD = 1;
        }
        break;
      case 'e':
        if (gTLDlen == 3 && lowergTLDin.substring(1, 3).equals("du")) {
          isgTLD = 1;
        }
        break;
      case 'g':
        if (gTLDlen == 3 && lowergTLDin.substring(1, 3).equals("ov")) {
          isgTLD = 1;
        } else if (gTLDlen == 2 && lowergTLDin.substring(1, 2).equals("r")) {
          isgTLD = 1;
        }
        break;
      case 'a':
        if (gTLDlen == 4 && (lowergTLDin.substring(1, 4).equals("ero") ||
            lowergTLDin.substring(1, 4).equals("rpa") || lowergTLDin.substring(1, 4).equals("sia"))) {
          isgTLD = 1;

        }
        break;
      case 'b':
        if (gTLDlen == 3 && lowergTLDin.substring(1, 3).equals("iz")) {
          isgTLD = 1;
        } else if (gTLDlen == 2 && (lowergTLDin.substring(1, 2).equals("c") || lowergTLDin.substring(1, 2)
            .equals("d"))) {
          isgTLD = 1;
        }
        break;
      case 'i':
        if (gTLDlen == 3 && lowergTLDin.substring(1, 3).equals("nt")) {
          isgTLD = 1;
        } else if (gTLDlen == 4 && lowergTLDin.substring(1, 4).equals("nfo")) {
          isgTLD = 1;
        }
        break;
      case 'm':
        if (gTLDlen == 3 && lowergTLDin.substring(1, 3).equals("il")) {
          isgTLD = 1;
        } else if (gTLDlen == 4 && lowergTLDin.substring(1, 4).equals("obi")) {
          isgTLD = 1;
        } else if (gTLDlen == 5 && lowergTLDin.substring(1, 5).equals("useu")) {
          isgTLD = 1;
        }
        break;
      case 'o':
        if (gTLDlen == 3 && lowergTLDin.substring(1, 3).equals("rg")) {
          isgTLD = 1;
        } else if (gTLDlen == 2 && lowergTLDin.substring(1, 2).equals("r")) {
          isgTLD = 1;
        }
        break;
      case 'p':
        if (gTLDlen == 3 && (lowergTLDin.substring(1, 3).equals("ro") || lowergTLDin.substring(1, 3).equals("lc"))) {
          isgTLD = 1;
        } else if (gTLDlen == 3 && lowergTLDin.substring(1, 2).equals("t")) {
          isgTLD = 1;
        }
        break;
      case 't':
        if (gTLDlen == 3 && lowergTLDin.substring(1, 3).equals("el")) {
          isgTLD = 1;
        } else if (gTLDlen == 6 && lowergTLDin.substring(1, 6).equals("ravel")) {
          isgTLD = 1;
        }
        break;
      default:
        isgTLD = 0;
        break;
    }
    return isgTLD;
  }

  public boolean isalpha(char c) {
    if (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z') {
      return true;
    }
    return false;
  }
}
