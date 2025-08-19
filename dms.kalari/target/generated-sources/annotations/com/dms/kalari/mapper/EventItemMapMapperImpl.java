package com.dms.kalari.mapper;

import com.dms.kalari.dto.EventItemMapDTO;
import com.dms.kalari.entity.EventItemMap;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-19T13:35:09+0530",
    comments = "version: 1.5.3.Final, compiler: Eclipse JDT (IDE) 3.42.0.v20250523-0729, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class EventItemMapMapperImpl implements EventItemMapMapper {

    @Override
    public EventItemMapDTO toDTO(EventItemMap entity) {
        if ( entity == null ) {
            return null;
        }

        EventItemMapDTO eventItemMapDTO = new EventItemMapDTO();

        eventItemMapDTO.setActive( entity.getActive() );
        eventItemMapDTO.setDeleted( map( entity.getDeleted() ) );
        eventItemMapDTO.setTCreated( entity.getTCreated() );
        eventItemMapDTO.setTDeleted( entity.getTDeleted() );
        eventItemMapDTO.setTModified( entity.getTModified() );
        eventItemMapDTO.setUCreated( entity.getUCreated() );
        eventItemMapDTO.setUDeleted( entity.getUDeleted() );
        eventItemMapDTO.setUModified( entity.getUModified() );
        eventItemMapDTO.setCategory( entity.getCategory() );
        eventItemMapDTO.setEimId( entity.getEimId() );

        return eventItemMapDTO;
    }

    @Override
    public EventItemMap toEntity(EventItemMapDTO dto) {
        if ( dto == null ) {
            return null;
        }

        EventItemMap eventItemMap = new EventItemMap();

        eventItemMap.setActive( dto.getActive() );
        eventItemMap.setDeleted( map( dto.getDeleted() ) );
        eventItemMap.setTCreated( dto.getTCreated() );
        eventItemMap.setTDeleted( dto.getTDeleted() );
        eventItemMap.setTModified( dto.getTModified() );
        eventItemMap.setUCreated( dto.getUCreated() );
        eventItemMap.setUDeleted( dto.getUDeleted() );
        eventItemMap.setUModified( dto.getUModified() );
        eventItemMap.setCategory( dto.getCategory() );
        eventItemMap.setEimId( dto.getEimId() );

        return eventItemMap;
    }
}
