package com.ebay.sojourner.ubd.common.sharedlib.metrics;

import com.ebay.sojourner.ubd.common.model.SessionAccumulator;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.parser.LkpFetcher;

import java.util.Map;

public class Gr1CntMetrics implements FieldMetrics<UbiEvent, SessionAccumulator> {
	private int gr1Count;
	private static Map<Integer, String[]> pageFmlyNameMap;
    private static LkpFetcher lkpFetcher;
	@Override
	public void start(SessionAccumulator sessionAccumulator) throws Exception {
		this.gr1Count = 0;
		sessionAccumulator.getUbiSession().setGr1Cnt(0);
	}

	@Override
	public void feed(UbiEvent event, SessionAccumulator sessionAccumulator) throws Exception {
		Integer pageId = event.getPageId();
		String[] pageFmlyName = pageFmlyNameMap.get(pageId);
		if (event.getRdt() == 0 &&(event.getIframe()==Integer.MIN_VALUE|| event.getIframe()==0)  &&event.getPartialValidPage()!=Integer.MIN_VALUE&& event.getPartialValidPage() == 1 &&
				(pageFmlyName != null && "GR-1".equals(pageFmlyName[1]))) {
			sessionAccumulator.getUbiSession().setGr1Cnt(sessionAccumulator.getUbiSession().getGr1Cnt()+1);
		}
	}

	@Override
	public void end(SessionAccumulator sessionAccumulator) throws Exception {

	}

	@Override
	public void init() throws Exception {
		lkpFetcher=LkpFetcher.getInstance();
		lkpFetcher.loadPageFmlys();
		pageFmlyNameMap = lkpFetcher.getPageFmlyMaps();
	}
}
