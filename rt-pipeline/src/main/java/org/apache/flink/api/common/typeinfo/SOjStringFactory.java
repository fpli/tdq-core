package org.apache.flink.api.common.typeinfo;



import java.lang.reflect.Type;
import java.util.Map;

public class SOjStringFactory extends TypeInfoFactory<String> {
    @Override
    public TypeInformation<String> createTypeInfo( Type t, Map<String, TypeInformation<?>> genericParameters ) {

        System.out.println("Factory Test");

        return BasicTypeInfo.getInfoFor(String.class);
    }
}
