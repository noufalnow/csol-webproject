package com.example.tenant_service.mapper;

import com.example.tenant_service.dto.CollectionDetDTO;
import com.example.tenant_service.entity.MisCollectionDet;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-10-07T14:38:44+0530",
    comments = "version: 1.5.3.Final, compiler: Eclipse JDT (IDE) 3.37.0.v20240215-1558, environment: Java 17.0.12 (Eclipse Adoptium)"
)
@Component
public class MisCollectionDetMapperImpl implements MisCollectionDetMapper {

    @Override
    public CollectionDetDTO toDTO(MisCollectionDet entity) {
        if ( entity == null ) {
            return null;
        }

        CollectionDetDTO collectionDetDTO = new CollectionDetDTO();

        collectionDetDTO.setCdetAmtPaid( entity.getCdetAmtPaid() );
        collectionDetDTO.setCdetAmtToPay( entity.getCdetAmtToPay() );
        collectionDetDTO.setCdetCollId( entity.getCdetCollId() );
        collectionDetDTO.setCdetId( entity.getCdetId() );
        collectionDetDTO.setCdetPoptId( entity.getCdetPoptId() );

        return collectionDetDTO;
    }

    @Override
    public MisCollectionDet toEntity(CollectionDetDTO dto) {
        if ( dto == null ) {
            return null;
        }

        MisCollectionDet misCollectionDet = new MisCollectionDet();

        misCollectionDet.setCdetAmtPaid( dto.getCdetAmtPaid() );
        misCollectionDet.setCdetAmtToPay( dto.getCdetAmtToPay() );
        misCollectionDet.setCdetCollId( dto.getCdetCollId() );
        misCollectionDet.setCdetId( dto.getCdetId() );
        misCollectionDet.setCdetPoptId( dto.getCdetPoptId() );

        return misCollectionDet;
    }
}
