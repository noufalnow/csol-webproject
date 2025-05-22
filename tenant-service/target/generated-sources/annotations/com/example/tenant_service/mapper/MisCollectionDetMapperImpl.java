package com.example.tenant_service.mapper;

import com.example.tenant_service.dto.CollectionDetDTO;
import com.example.tenant_service.entity.MisCollectionDet;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-22T20:55:15+0530",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.13 (Ubuntu)"
)
@Component
public class MisCollectionDetMapperImpl implements MisCollectionDetMapper {

    @Override
    public CollectionDetDTO toDTO(MisCollectionDet entity) {
        if ( entity == null ) {
            return null;
        }

        CollectionDetDTO collectionDetDTO = new CollectionDetDTO();

        collectionDetDTO.setCdetId( entity.getCdetId() );
        collectionDetDTO.setCdetCollId( entity.getCdetCollId() );
        collectionDetDTO.setCdetPoptId( entity.getCdetPoptId() );
        collectionDetDTO.setCdetAmtToPay( entity.getCdetAmtToPay() );
        collectionDetDTO.setCdetAmtPaid( entity.getCdetAmtPaid() );

        return collectionDetDTO;
    }

    @Override
    public MisCollectionDet toEntity(CollectionDetDTO dto) {
        if ( dto == null ) {
            return null;
        }

        MisCollectionDet misCollectionDet = new MisCollectionDet();

        misCollectionDet.setCdetId( dto.getCdetId() );
        misCollectionDet.setCdetCollId( dto.getCdetCollId() );
        misCollectionDet.setCdetPoptId( dto.getCdetPoptId() );
        misCollectionDet.setCdetAmtToPay( dto.getCdetAmtToPay() );
        misCollectionDet.setCdetAmtPaid( dto.getCdetAmtPaid() );

        return misCollectionDet;
    }
}
