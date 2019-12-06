package com.ebay.sojourner.ubd.rt.connectors.kafka;

import com.ebay.sojourner.ubd.common.model.ClientData;
import com.ebay.sojourner.ubd.common.model.RheosHeader;
import com.ebay.sojourner.ubd.common.model.UbiEvent;
import com.ebay.sojourner.ubd.common.sharedlib.parser.LkpFetcher;
import com.ebay.sojourner.ubd.common.sharedlib.util.IntegerField;
import com.ebay.sojourner.ubd.common.sharedlib.util.SOJTS2Date;
import com.ebay.sojourner.ubd.common.util.PropertyUtils;
import io.ebay.rheos.schema.event.RheosEvent;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.util.Utf8;
import org.apache.commons.lang.StringUtils;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Soj2BinaryDeserializationSchema implements DeserializationSchema<byte[]> {

    private static final Logger logger = Logger.getLogger(Soj2BinaryDeserializationSchema.class);

    @Override
    public byte[] deserialize( byte[] message ) throws IOException {
       return message;
    }

    private Integer getInteger(Object o){
        if(StringUtils.isEmpty(getString(o))){
            return null;
        }else{
            return Integer.valueOf(getString(o));
        }
    }
    private boolean getBoolean(Object o){
        if(StringUtils.isEmpty(getString(o))){
            return false;
        }else{
            return Boolean.valueOf(getString(o));
        }
    }


    private Long getLong(Object o){
        if(StringUtils.isEmpty(getString(o))){
            return null;
        }else{
            return Long.valueOf(getString(o));
        }
    }

    private String getString( Object o ) {
        return (o != null) ? o.toString() : null;
    }
    private String getString2( Object o ) {
        return (o != null) ? o.toString() : "";
    }
    @Override
    public boolean isEndOfStream( byte[] nextElement ) {
        return false;
    }

    @Override
    public TypeInformation<byte[]> getProducedType() {
        return TypeInformation.of(byte[].class);
    }
}
