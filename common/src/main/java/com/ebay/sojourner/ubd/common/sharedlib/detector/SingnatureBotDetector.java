package com.ebay.sojourner.ubd.common.sharedlib.detector;

import com.ebay.sojourner.ubd.common.model.Attribute;
import com.ebay.sojourner.ubd.common.model.Singnature;

import java.util.List;

public class SingnatureBotDetector implements BotDetector<Singnature> {
    @Override
    public List<Integer> getBotFlagList(Singnature singnature) {
        return null;
    }

    @Override
    public void initBotRules() {

    }
}
