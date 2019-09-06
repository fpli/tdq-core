package com.ebay.sojourner.ubd.common.model;

import lombok.Data;
import org.apache.flink.api.common.typeinfo.TypeInfo;
import org.apache.flink.api.common.typeinfo.TypeInfoFactory;
import org.apache.flink.api.common.typeinfo.TypeInformation;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Map;

@Data
//@TypeInfo(AttributeAccumulator.MyTupleTypeInfoFactory.class)
public class AttributeAccumulator<T extends Attribute> implements Serializable{

    private static final long serialVersionUID = 5642072395614341391L;

    private T attribute;
    private UbiSession ubiSession;

    public AttributeAccumulator()
    {
        this.ubiSession = new UbiSession();
    }

//    public static class MyTupleTypeInfoFactory extends TypeInfoFactory<AttributeAccumulator>{
//        @Override
//        public TypeInformation<AttributeAccumulator> createTypeInfo(Type t, Map<String, TypeInformation<?>> genericParameters) {
//            return new MyTupleTypeInfo(genericParameters.get("T"));
//        }
//    }


}

