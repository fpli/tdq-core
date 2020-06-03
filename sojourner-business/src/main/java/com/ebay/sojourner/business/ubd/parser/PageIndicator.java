package com.ebay.sojourner.business.ubd.parser;

import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.util.Property;
import com.ebay.sojourner.ubd.common.util.PropertyUtils;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;

/**
 * Indicate the page type for event
 *
 * @author kofeng
 */
@Slf4j
public class PageIndicator {

  private Set<Integer> pageIds;

  public PageIndicator(String pageIds) {
    this(parse(pageIds));
  }

  public PageIndicator(Set<Integer> pageIds) {
    this.pageIds = Collections.unmodifiableSet(pageIds);
  }

  public static Set<Integer> parse(String pageIds) {
    Set<Integer> resultSet = new HashSet<>();
    Collection<String> ids = PropertyUtils.parseProperty(pageIds, Property.PROPERTY_DELIMITER);
    for (String id : ids) {
      try {
        resultSet.add(Integer.valueOf(id.trim()));
      } catch (NumberFormatException e) {
        log.error("Format page Id error: {}", id, e);
      }
    }

    return resultSet;
  }

  public boolean isCorrespondingPageEvent(UbiEvent event) {
    int pageId = event.getPageId();
    return pageIds.contains(pageId);
  }
}
