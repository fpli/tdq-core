package com.ebay.sojourner.ubd.rt.util;

public class Constants {

  public static final String DEFAULT_APPLICATION_PROPERTIES_FILENAME = "/application.properties";

  // kafka consumer config
  public static final String TOPIC_PATHFINDER_EVENTS = AppEnv.config().getKafkaConsumerConfig()
      .getTopic();

  // rno
  public static final String GROUP_ID_RNO = AppEnv.config().getKafkaConsumerConfig()
      .getGroupIdForRNO();
  public static final String GROUP_ID_RNO_DQ = AppEnv.config().getKafkaConsumerConfig()
      .getGroupIdForRNODQ();
  public static final String BOOTSTRAP_SERVERS_RNO =
      String.join(",", AppEnv.config().getKafkaConsumerConfig().getBootstrapServersForRNO());

  // lvs
  public static final String GROUP_ID_LVS = AppEnv.config().getKafkaConsumerConfig()
      .getGroupIdForLVS();
  public static final String GROUP_ID_LVS_DQ = AppEnv.config().getKafkaConsumerConfig()
      .getGroupIdForLVSDQ();
  public static final String BOOTSTRAP_SERVERS_LVS =
      String.join(",", AppEnv.config().getKafkaConsumerConfig().getBootstrapServersForLVS());

  // slc
  public static final String GROUP_ID_SLC = AppEnv.config().getKafkaConsumerConfig()
      .getGroupIdForSLC();
  public static final String GROUP_ID_SLC_DQ = AppEnv.config().getKafkaConsumerConfig()
      .getGroupIdForSLCDQ();
  public static final String BOOTSTRAP_SERVERS_SLC =
      String.join(",", AppEnv.config().getKafkaConsumerConfig().getBootstrapServersForSLC());

  // QA
  public static final String GROUP_ID_QA = AppEnv.config().getKafkaConsumerConfig()
      .getGroupIdForQA();
  public static final String BOOTSTRAP_SERVERS_QA =
      String.join(",", AppEnv.config().getKafkaConsumerConfig().getBootstrapServersForQA());

  // session
  public static final String GROUP_ID_SESSION = AppEnv.config().getKafkaConsumerConfig()
      .getGroupIdForSession();

  // event
  public static final String GROUP_ID_EVENT = AppEnv.config().getKafkaConsumerConfig()
      .getGroupIdForEvent();

  // bot
  public static final String GROUP_ID_BOT = AppEnv.config().getKafkaConsumerConfig()
      .getGroupIdForBot();

  // copy
  public static final String GROUP_ID_COPY = AppEnv.config().getKafkaConsumerConfig()
      .getGroupIdForCopy();

  // cross session dq
  public static final String GROUP_ID_CROSS_SESSION_DQ = AppEnv.config().getKafkaConsumerConfig()
      .getGroupIdForCrossSession();

  // kafka producer config
  // event
  public static final String TOPIC_PRODUCER_EVENT = AppEnv.config().getKafkaProducerConfig()
      .getEventTopic();
  public static final String BOOTSTRAP_SERVERS_EVENT =
      String.join(",", AppEnv.config().getKafkaProducerConfig().getBootstrapServersForEvent());

  // session
  public static final String TOPIC_PRODUCER_SESSION = AppEnv.config().getKafkaProducerConfig()
      .getSessionTopic();
  public static final String BOOTSTRAP_SERVERS_SESSION =
      String.join(",", AppEnv.config().getKafkaProducerConfig().getBootstrapServersForSession());

  // bot
  public static final String TOPIC_PRODUCER_BOT = AppEnv.config().getKafkaProducerConfig()
      .getBotTopic();
  public static final String BOOTSTRAP_SERVERS_BOT =
      String.join(",", AppEnv.config().getKafkaProducerConfig().getBootstrapServersForBot());

  // copy
  public static final String TOPIC_PRODUCER_COPY = AppEnv.config().getKafkaProducerConfig()
      .getCopyTopic();
  public static final String BOOTSTRAP_SERVERS_COPY =
      String.join(",", AppEnv.config().getKafkaProducerConfig().getBootstrapServersForCopy());

  // cross session dq
  public static final String TOPIC_PRODUCER_CROSS_SESSION_DQ = AppEnv.config()
      .getKafkaProducerConfig().getCrossSessionDQTopic();
  public static final String BOOTSTRAP_SERVERS_CROSS_SESSION_DQ =
      String.join(",",
          AppEnv.config().getKafkaProducerConfig().getBootstrapServersForCrossSessionDQ());

  // message key
  public static final String MESSAGE_KEY = "guid";
}
