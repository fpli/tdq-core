package com.ebay.sojourner.ubd.sojlib.parser;


import com.ebay.sojourner.ubd.util.Property;
import com.ebay.sojourner.ubd.model.UbiEvent;
import com.ebay.sojourner.ubd.util.PropertyUtils;
import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Indicate the page type for event
 * @author kofeng
 */
public class PageIndicator {
    public static final Logger log = Logger.getLogger(PageIndicator.class);
    
    private Set<Integer> pageIds = null;
    
    public PageIndicator(String pageIds) {
        this(parse(pageIds));
    }
    
    public PageIndicator(Set<Integer> pageIds) {
        this.pageIds = Collections.unmodifiableSet(pageIds);
    }
    
    public boolean isCorrespondingPageEvent(UbiEvent event) {
        Integer pageId = event.getPageId();
        return pageId != null && pageIds.contains(pageId) ? true : false;
    }
    
    public static Set<Integer> parse(String pageIds) {
        Set<Integer> resultSet = new HashSet<Integer>();
        Collection<String> ids = PropertyUtils.parseProperty(pageIds, Property.PROPERTY_DELIMITER);
        for (String id : ids) {
            try {
                resultSet.add(Integer.valueOf(id.trim()));
            } catch (NumberFormatException e) {
                e.printStackTrace(System.err);
                log.error("Format page Id error: " + id);
            }
        }
        
        return resultSet;
    }
}
