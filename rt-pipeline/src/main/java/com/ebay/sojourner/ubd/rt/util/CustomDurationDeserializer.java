package com.ebay.sojourner.ubd.rt.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonTokenId;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.Instant;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class CustomDurationDeserializer extends StdDeserializer<Duration> {

    private static final Pattern periodPattern = Pattern.compile("([0-9]+)([smhd])");

    public CustomDurationDeserializer() {
        this(null);
    }

    protected CustomDurationDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Duration deserialize(JsonParser parser, DeserializationContext context) throws IOException, JsonProcessingException {
        switch (parser.getCurrentTokenId()) {
            case JsonTokenId.ID_NUMBER_INT:
                if (context.isEnabled(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS)) {
                    return Duration.ofSeconds(parser.getLongValue());
                }
                return Duration.ofMillis(parser.getLongValue());

            case JsonTokenId.ID_STRING:
                String string = parser.getText().trim();
                if (string.length() == 0) {
                    return null;
                }
                try {
                    return Duration.ofMillis(getMillis(string));
                } catch (DateTimeException e) {
                    log.error("Cannot parse time duration", e);
                }
            case JsonTokenId.ID_EMBEDDED_OBJECT:
                // 20-Apr-2016, tatu: Related to [databind#1208], can try supporting embedded
                //    values quite easily
                return (Duration) parser.getEmbeddedObject();

            case JsonTokenId.ID_START_ARRAY:
                return _deserializeFromArray(parser, context);
        }
        return null;
    }

    private Long getMillis(String period) {
        if (period == null) return null;
        period = period.toLowerCase();
        Matcher matcher = periodPattern.matcher(period);
        Instant instant = Instant.EPOCH;
        while (matcher.find()) {
            int num = Integer.parseInt(matcher.group(1));
            String typ = matcher.group(2);
            switch (typ) {
                case "s":
                    instant = instant.plus(Duration.ofSeconds(num));
                    break;
                case "m":
                    instant = instant.plus(Duration.ofMinutes(num));
                    break;
                case "h":
                    instant = instant.plus(Duration.ofHours(num));
                    break;
                case "d":
                    instant = instant.plus(Duration.ofDays(num));
                    break;
            }
        }
        return instant.toEpochMilli();
    }
}
