package com.dms.kalari.mapper;

import com.dms.kalari.dto.MemberEventItemDTO;
import com.dms.kalari.entity.MemberEventItem;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-19T13:35:09+0530",
    comments = "version: 1.5.3.Final, compiler: Eclipse JDT (IDE) 3.42.0.v20250523-0729, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class MemberEventItemMapperImpl implements MemberEventItemMapper {

    @Override
    public MemberEventItemDTO toDTO(MemberEventItem entity) {
        if ( entity == null ) {
            return null;
        }

        MemberEventItemDTO memberEventItemDTO = new MemberEventItemDTO();

        memberEventItemDTO.setActive( entity.getActive() );
        memberEventItemDTO.setDeleted( map( entity.getDeleted() ) );
        memberEventItemDTO.setTCreated( entity.getTCreated() );
        memberEventItemDTO.setTDeleted( entity.getTDeleted() );
        memberEventItemDTO.setTModified( entity.getTModified() );
        memberEventItemDTO.setUCreated( entity.getUCreated() );
        memberEventItemDTO.setUDeleted( entity.getUDeleted() );
        memberEventItemDTO.setUModified( entity.getUModified() );
        memberEventItemDTO.setApproveDateTime( entity.getApproveDateTime() );
        memberEventItemDTO.setApprovedBy( entity.getApprovedBy() );
        memberEventItemDTO.setEntryDateTime( entity.getEntryDateTime() );
        memberEventItemDTO.setId( entity.getId() );
        memberEventItemDTO.setItemKey( entity.getItemKey() );
        memberEventItemDTO.setItemValue( entity.getItemValue() );
        memberEventItemDTO.setMemberEventMember( entity.getMemberEventMember() );
        memberEventItemDTO.setScore( entity.getScore() );
        memberEventItemDTO.setScoreEntryBy( entity.getScoreEntryBy() );
        memberEventItemDTO.setUniqueId( entity.getUniqueId() );

        return memberEventItemDTO;
    }

    @Override
    public MemberEventItem toEntity(MemberEventItemDTO dto) {
        if ( dto == null ) {
            return null;
        }

        MemberEventItem memberEventItem = new MemberEventItem();

        memberEventItem.setActive( dto.getActive() );
        memberEventItem.setDeleted( map( dto.getDeleted() ) );
        memberEventItem.setTCreated( dto.getTCreated() );
        memberEventItem.setTDeleted( dto.getTDeleted() );
        memberEventItem.setTModified( dto.getTModified() );
        memberEventItem.setUCreated( dto.getUCreated() );
        memberEventItem.setUDeleted( dto.getUDeleted() );
        memberEventItem.setUModified( dto.getUModified() );
        memberEventItem.setApproveDateTime( dto.getApproveDateTime() );
        memberEventItem.setApprovedBy( dto.getApprovedBy() );
        memberEventItem.setEntryDateTime( dto.getEntryDateTime() );
        memberEventItem.setId( dto.getId() );
        memberEventItem.setItemKey( dto.getItemKey() );
        memberEventItem.setItemValue( dto.getItemValue() );
        memberEventItem.setMemberEventMember( dto.getMemberEventMember() );
        memberEventItem.setScore( dto.getScore() );
        memberEventItem.setScoreEntryBy( dto.getScoreEntryBy() );
        memberEventItem.setUniqueId( dto.getUniqueId() );

        return memberEventItem;
    }
}
