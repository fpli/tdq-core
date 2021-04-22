package com.ebay.tdq.sample;

import com.ebay.sojourner.common.model.RawEvent;
import com.ebay.sojourner.common.util.SojUtils;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;

/**
 * @author juntzhang
 */
// functions
// all opt in FlinkSqlOperatorTable SqlStdOperatorTable
// SqlFunctionUtils
// StringCallGen -> regexpExtract
@Slf4j
public class TdqUDFs {

    public static String regexpExtract(String str, String regex, int extractIndex) {
        if (str == null || regex == null) {
            return null;
        }
        try {
            Matcher m = Pattern.compile(regex).matcher(str);
            if (m.find()) {
                MatchResult mr = m.toMatchResult();
                return mr.group(extractIndex);
            }
        } catch (Exception e) {
            log.error(String.format("Exception in " +
                    "regexpExtract('%s', '%s', '%d')", str, regex, extractIndex), e);
        }

        return null;
    }

    public static String extractTag(RawEvent rawEvent, String tag) {
        return SojUtils.getTagValueStr(rawEvent, tag);
    }

    public static Integer siteId(RawEvent rawEvent) {
        return SojUtils.getSiteId(rawEvent);
    }

    public static String pageFamily(RawEvent rawEvent) {
        return SojUtils.getPageFmly(SojUtils.getPageId(rawEvent));
    }

}

