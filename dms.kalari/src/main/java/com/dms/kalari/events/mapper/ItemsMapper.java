package com.dms.kalari.events.mapper;

import java.util.HashMap;
import java.util.Map;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ItemsMapper {

    @Named("convertToDtoMap")
    default Map<Integer, String> convertToDtoMap(Map<String, String> entityMap) {
        if (entityMap == null) return new HashMap<>();
        Map<Integer, String> dtoMap = new HashMap<>();
        entityMap.forEach((key, value) -> {
            try {
                int intKey = Integer.parseInt(key);
                dtoMap.put(intKey, value);
            } catch (NumberFormatException e) {
                // Log or ignore invalid keys
            }
        });
        return dtoMap;
    }

    @Named("convertToEntityMap")
    default Map<String, String> convertToEntityMap(Map<Integer, String> dtoMap) {
        if (dtoMap == null) return new HashMap<>();
        Map<String, String> entityMap = new HashMap<>();
        dtoMap.forEach((key, value) -> entityMap.put(key.toString(), value));
        return entityMap;
    }
}
