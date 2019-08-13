package com.ebay.sojourner.ubd.common.sharedlib.metrics;

import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.parser.PageIndicator;
import com.ebay.sojourner.ubd.common.util.Property;
import com.ebay.sojourner.ubd.common.util.UBIConfig;

import java.io.File;
import java.io.InputStream;

public class PageIdMetrics implements FieldMetrics<UbiEvent, SessionAccumulator> {
	private static final Integer ZERO = new Integer(0);
	private PageIndicator indicator = null;
	private static UBIConfig ubiConfig;

	@Override
	public void start(SessionAccumulator sessionAccumulator) {
		sessionAccumulator.getUbiSession().setStartPageId(null);
		sessionAccumulator.getUbiSession().setEndPageId(null);
	}

	@Override
	public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) {
		if (ZERO.equals(event.getIframe())) {
			if (ZERO.equals(event.getRdt()) || indicator.isCorrespondingPageEvent(event)) {
				if (sessionAccumulator.getUbiSession().getStartPageId() == null) {
					sessionAccumulator.getUbiSession().setStartPageId(event.getPageId());
				}
				sessionAccumulator.getUbiSession().setEndPageId(event.getPageId());
			}
		}
	}

	@Override
	public void end(SessionAccumulator sessionAccumulator) {
		if (sessionAccumulator.getUbiSession().getStartPageId()!= null) {

		} else {
			sessionAccumulator.getUbiSession().setStartPageId(0);
		}
		if (sessionAccumulator.getUbiSession().getEndPageId() != null) {

		} else {
			sessionAccumulator.getUbiSession().setEndPageId(0);
		}
	}

	@Override
	public void init() throws Exception {
		InputStream resourceAsStream = PageIdMetrics.class.getResourceAsStream("/ubi.properties");
		ubiConfig = UBIConfig.getInstance(resourceAsStream);
		setPageIndicator(new PageIndicator(ubiConfig.getString(Property.SEARCH_VIEW_PAGES)));
	}

	void setPageIndicator(PageIndicator indicator) {
		this.indicator = indicator;
	}
}
