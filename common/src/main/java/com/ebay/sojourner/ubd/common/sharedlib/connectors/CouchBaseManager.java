package com.ebay.sojourner.ubd.common.sharedlib.connectors;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.N1qlQueryResult;
import com.couchbase.client.java.query.N1qlQueryRow;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import org.apache.log4j.Logger;

public class CouchBaseManager {

  private static final String serverName = "127.0.0.1";
  private static final String USER_NAME = "Administrator";
  private static final String USER_PASS = "111111";
  private static final String BUCKET_NAME = "botsignature";
  private static final Logger logger = Logger.getLogger(CouchBaseManager.class);
  private static CouchBaseManager couchBaseManager;
  private static Cluster couchBaseCluster = null;
  private static Bucket bucket = null;

  private CouchBaseManager() {
    couchBaseCluster = CouchbaseCluster.create(serverName);
    couchBaseCluster.authenticate(USER_NAME, USER_PASS);
    bucket = couchBaseCluster.openBucket(BUCKET_NAME);
    //        bucket.bucketManager().createN1qlPrimaryIndex(true, false);
  }

  public static CouchBaseManager getInstance() {
    if (couchBaseManager == null) {
      synchronized (CouchBaseManager.class) {
        if (couchBaseManager == null) {
          couchBaseManager = new CouchBaseManager();
        }
      }
    }
    return couchBaseManager;
  }

  public static void close() {
    bucket.close();
    couchBaseCluster.disconnect();
  }

  public void upsert(JsonObject jsonObject, String id) {

    bucket.upsert(JsonDocument.create(id, jsonObject));
  }

  public Set<Integer> getSignatureWithColumn(
      String inColumnName, String inColumnValue, String outColumnName) {
    N1qlQueryResult result =
        bucket.query(
            N1qlQuery.parameterized(
                "SELECT "
                    + outColumnName
                    + " FROM "
                    + BUCKET_NAME
                    + " WHERE "
                    + inColumnName
                    + " =\"$1\"",
                JsonArray.from(inColumnValue)));

    for (N1qlQueryRow row : result) {
      JsonArray jsonArray = (JsonArray) row.value().get(outColumnName);
      List<Object> botFlagList = jsonArray.toList();
      Set<Integer> botFlagSet = new HashSet<Integer>(botFlagList.size());
      for (Object o : botFlagList) {
        botFlagSet.add((Integer) o);
      }
      return botFlagSet;
    }
    return Collections.emptySet();
  }

  public Set<Integer> getSignatureWithDocId(String id) {
    JsonDocument response = null;
    if (id != null) {
      try {
        if (bucket.exists(id)) {
          response = bucket.get(id);
        }

      } catch (NoSuchElementException e) {
        logger.error("ERROR: No element with message: " + e.getMessage());
        return Collections.emptySet();
      }
      if (response != null) {
        JsonArray jsonArray = (JsonArray) response.content().get("botFlag");
        List<Object> botFlagList = jsonArray.toList();
        Set<Integer> botFlagSet = new HashSet<Integer>(botFlagList.size());
        for (Object o : botFlagList) {
          botFlagSet.add((Integer) o);
        }

        return botFlagSet;
      } else {
        return Collections.emptySet();
      }
    } else {
      logger.error("ERROR: document id is null! ");
      return Collections.emptySet();
    }
  }
}
