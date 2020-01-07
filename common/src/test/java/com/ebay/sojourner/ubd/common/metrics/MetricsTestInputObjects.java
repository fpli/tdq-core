<<<<<<< HEAD:common/src/test/java/com/ebay/sojourner/ubd/common/metrics/MetricsTestInputObjects.java
package com.ebay.sojourner.ubd.common.metrics;

import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MetricsTestInputObjects {

    @JsonProperty("UbiEvent")
    private UbiEvent ubiEvent;

    @JsonProperty("SessionAccumulator")
    private SessionAccumulator sessionAccumulator;
}
=======
package com.ebay.sojourner.ubd.common.util;

import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MetricsTestInputObjects {

    @JsonProperty("UbiEvent")
    private UbiEvent ubiEvent;

    @JsonProperty("SessionAccumulator")
    private SessionAccumulator sessionAccumulator;
}
>>>>>>> c2751c69838aeefcb801365ff24cfbd77b932996:common/src/test/java/com/ebay/sojourner/ubd/common/util/MetricsTestInputObjects.java
