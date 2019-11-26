package com.ebay.sojourner.ubd.common.sharedlib.parser;

import com.ebay.sojourner.ubd.common.model.RawEvent;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.util.PropertyUtils;
import com.ebay.sojourner.ubd.common.sharedlib.util.SOJNVL;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class CookiesParser implements FieldParser<RawEvent, UbiEvent> {
	private static final String CK_TAG = "ck";
	private static final String COOKIE2_TAG = "C";
	public void parse(RawEvent rawEvent, UbiEvent ubiEvent) {
		Map<String, String> map = new HashMap<>();
		String cookies = rawEvent.getSojA().get("cookies")==null?null:rawEvent.getSojA().get("cookies").toString();
		ubiEvent.setCookies(cookies);
//		map.putAll(rawEvent.getSojA());
//		map.putAll(rawEvent.getSojK());
//		map.putAll(rawEvent.getSojC());
//		String mARecString = PropertyUtils.mapToString(rawEvent.getSojA());
//		String mKRecString = PropertyUtils.mapToString(rawEvent.getSojK());
//		String mCRecString = PropertyUtils.mapToString(rawEvent.getSojC());
//
//		String payload =null;
//		if (mARecString != null) {
//			payload = mARecString;
//		}
//		if ((payload != null) && (mKRecString != null)) {
//			payload = payload + "&" + mKRecString;
//		}
//
//		// else set C record
//		if (payload == null)
//			payload = mCRecString;
//
//		String cookie = SOJNVL.getTagValue(payload, "ck");
//		if (StringUtils.isNotBlank(cookie)) {
//			ubiEvent.setCookies(cookie);
//		} else {
//			cookie = SOJNVL.getTagValue(payload, "C");
//			if (StringUtils.isNotBlank(cookie)) {
//				ubiEvent.setCookies(cookie);
//			} else {
//				String cookie1 = map.get(CK_TAG);
//				if (StringUtils.isNotBlank(cookie1)) {
//					ubiEvent.setCookies(cookie1);
//				} else {
//					String cookie2 = map.get(COOKIE2_TAG);
//					if (StringUtils.isNotBlank(cookie2)) {
//						ubiEvent.setCookies(cookie2);
//					} else {
//						ubiEvent.setCookies("null");
//					}
//				}
//			}
//		}
	}

    @Override
    public void init() throws Exception {
    }
}
