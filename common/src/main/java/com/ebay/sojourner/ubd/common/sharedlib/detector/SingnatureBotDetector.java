package com.ebay.sojourner.ubd.common.sharedlib.detector;

import com.ebay.sojourner.ubd.common.model.Attribute;
import com.ebay.sojourner.ubd.common.model.Singnature;

import java.util.List;
import java.util.Set;

public class SingnatureBotDetector implements BotDetector<Singnature> {
    @Override
    public Set<Integer> getBotFlagList(Singnature singnature) {
        return null;
    }

    @Override
    public void initBotRules() {

    }
}
