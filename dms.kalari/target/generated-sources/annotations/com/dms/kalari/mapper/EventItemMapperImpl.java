package com.dms.kalari.mapper;

import com.dms.kalari.dto.EventItemDTO;
import com.dms.kalari.entity.EventItem;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-20T20:39:31+0530",
    comments = "version: 1.5.3.Final, compiler: Eclipse JDT (IDE) 3.42.0.v20250523-0729, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class EventItemMapperImpl implements EventItemMapper {

    @Override
    public EventItemDTO toDTO(EventItem eventItem) {
        if ( eventItem == null ) {
            return null;
        }

        EventItemDTO eventItemDTO = new EventItemDTO();

        eventItemDTO.setActive( eventItem.getActive() );
        eventItemDTO.setDeleted( map( eventItem.getDeleted() ) );
        eventItemDTO.setTCreated( eventItem.getTCreated() );
        eventItemDTO.setTDeleted( eventItem.getTDeleted() );
        eventItemDTO.setTModified( eventItem.getTModified() );
        eventItemDTO.setUCreated( eventItem.getUCreated() );
        eventItemDTO.setUDeleted( eventItem.getUDeleted() );
        eventItemDTO.setUModified( eventItem.getUModified() );
        eventItemDTO.setEvitemCode( eventItem.getEvitemCode() );
        eventItemDTO.setEvitemCriteria( eventItem.getEvitemCriteria() );
        eventItemDTO.setEvitemDescription( eventItem.getEvitemDescription() );
        eventItemDTO.setEvitemId( eventItem.getEvitemId() );
        eventItemDTO.setEvitemName( eventItem.getEvitemName() );

        return eventItemDTO;
    }

    @Override
    public EventItem toEntity(EventItemDTO eventItemDTO) {
        if ( eventItemDTO == null ) {
            return null;
        }

        EventItem eventItem = new EventItem();

        eventItem.setActive( eventItemDTO.getActive() );
        eventItem.setDeleted( map( eventItemDTO.getDeleted() ) );
        eventItem.setTCreated( eventItemDTO.getTCreated() );
        eventItem.setTDeleted( eventItemDTO.getTDeleted() );
        eventItem.setTModified( eventItemDTO.getTModified() );
        eventItem.setUCreated( eventItemDTO.getUCreated() );
        eventItem.setUDeleted( eventItemDTO.getUDeleted() );
        eventItem.setUModified( eventItemDTO.getUModified() );
        eventItem.setEvitemCode( eventItemDTO.getEvitemCode() );
        eventItem.setEvitemCriteria( eventItemDTO.getEvitemCriteria() );
        eventItem.setEvitemDescription( eventItemDTO.getEvitemDescription() );
        eventItem.setEvitemId( eventItemDTO.getEvitemId() );
        eventItem.setEvitemName( eventItemDTO.getEvitemName() );

        return eventItem;
    }
}
