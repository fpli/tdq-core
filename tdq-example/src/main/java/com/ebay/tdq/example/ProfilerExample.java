package com.ebay.tdq.example;

import com.ebay.tdq.dto.QueryProfilerParam;
import com.ebay.tdq.dto.QueryProfilerResult;
import com.ebay.tdq.svc.ServiceFactory;
import com.google.common.collect.Sets;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.time.DateUtils;

/**
 * @author juntzhang
 */
public class ProfilerExample {

  public static void main(String[] args) throws IOException, ParseException {
    Map<String, Set<String>> dimensions = new HashMap<>();
    dimensions.put("page_id", Sets.newHashSet("711", "1677718"));
    QueryProfilerParam param = new QueryProfilerParam(
        "{\n" +
            "  \"id\": \"1\",\n" +
            "  \"rules\": [\n" +
            "    {\n" +
            "      \"name\": \"rule_1\",\n" +
            "      \"type\": \"realtime.rheos.profiler\",\n" +
            "      \"config\": {\n" +
            "        \"window\": \"2min\"\n" +
            "      },\n" +
            "      \"profilers\": [\n" +
            "        {\n" +
            "          \"metric-name\": \"global_mandatory_tag_item_rate1\",\n" +
            "          \"dimensions\": [\"page_id\"],\n" +
            "          \"expression\": {\"operator\": \"Expr\", \"config\": {\"text\": \"itm_valid_cnt / itm_cnt\"}}," +
            "\n" +
            "          \"transformations\": [\n" +
            "            {\n" +
            "              \"alias\": \"page_id\",\n" +
            "              \"expression\": {\"operator\": \"UDF\", \"config\": {\"text\": \"CAST( SOJ_NVL('p') AS " +
            "INTEGER)\"}}\n" +
            "            },\n" +
            "            {\n" +
            "              \"alias\": \"item\",\n" +
            "              \"expression\": {\n" +
            "                \"operator\": \"UDF\",\n" +
            "                \"config\": {\"text\": \"CAST( SOJ_NVL('itm|itmid|itm_id|itmlist|litm') AS LONG)\"}\n" +
            "              }\n" +
            "            },\n" +
            "            {\n" +
            "              \"alias\": \"itm_valid_ind\",\n" +
            "              \"expression\": {\n" +
            "                \"operator\": \"Expr\",\n" +
            "                \"config\": {\n" +
            "                  \"text\": \"case when item is not null then 1.0 else 0.0 end\"\n" +
            "                }\n" +
            "              }\n" +
            "            },\n" +
            "            {\n" +
            "              \"alias\": \"itm_cnt\",\n" +
            "              \"expression\": {\"operator\": \"Count\", \"config\": {\"arg0\": \"1.0\"}}\n" +
            "            },\n" +
            "            {\n" +
            "              \"alias\": \"itm_valid_cnt\",\n" +
            "              \"expression\": {\n" +
            "                \"operator\": \"Sum\", \"config\": {\"arg0\": \"itm_valid_ind\"}\n" +
            "              }\n" +
            "            }\n" +
            "          ]\n" +
            "        }\n" +
            "      ]\n" +
            "    }\n" +
            "  ]\n" +
            "}",
        DateUtils.parseDate("2021-05-29 12:02:00", new String[]{"yyyy-MM-dd HH:mm:ss"}).getTime(),
        DateUtils.parseDate("2021-05-29 12:04:00", new String[]{"yyyy-MM-dd HH:mm:ss"}).getTime(),
        dimensions
    );

    QueryProfilerResult result = ServiceFactory.getProfiler().query(param);
    System.out.println(result.getRecords());
  }
}
