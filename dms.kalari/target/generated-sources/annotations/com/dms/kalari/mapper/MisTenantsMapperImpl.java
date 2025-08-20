package com.dms.kalari.mapper;

import com.dms.kalari.dto.TenantDTO;
import com.dms.kalari.entity.MisTenants;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-20T20:39:30+0530",
    comments = "version: 1.5.3.Final, compiler: Eclipse JDT (IDE) 3.42.0.v20250523-0729, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class MisTenantsMapperImpl implements MisTenantsMapper {

    @Override
    public TenantDTO toDTO(MisTenants entity) {
        if ( entity == null ) {
            return null;
        }

        TenantDTO tenantDTO = new TenantDTO();

        tenantDTO.setActive( entity.getActive() );
        tenantDTO.setDeleted( map( entity.getDeleted() ) );
        tenantDTO.setTCreated( entity.getTCreated() );
        tenantDTO.setTDeleted( entity.getTDeleted() );
        tenantDTO.setTModified( entity.getTModified() );
        tenantDTO.setUCreated( entity.getUCreated() );
        tenantDTO.setUDeleted( entity.getUDeleted() );
        tenantDTO.setUModified( entity.getUModified() );
        tenantDTO.setTntAgrType( entity.getTntAgrType() );
        tenantDTO.setTntCompName( entity.getTntCompName() );
        tenantDTO.setTntCrno( entity.getTntCrno() );
        tenantDTO.setTntDocId( entity.getTntDocId() );
        tenantDTO.setTntExpat( entity.getTntExpat() );
        tenantDTO.setTntFullName( entity.getTntFullName() );
        tenantDTO.setTntId( entity.getTntId() );
        tenantDTO.setTntIdNo( entity.getTntIdNo() );
        tenantDTO.setTntPhone( entity.getTntPhone() );
        tenantDTO.setTntTele( entity.getTntTele() );

        return tenantDTO;
    }

    @Override
    public MisTenants toEntity(TenantDTO dto) {
        if ( dto == null ) {
            return null;
        }

        MisTenants misTenants = new MisTenants();

        misTenants.setActive( dto.getActive() );
        misTenants.setDeleted( map( dto.getDeleted() ) );
        misTenants.setTCreated( dto.getTCreated() );
        misTenants.setTDeleted( dto.getTDeleted() );
        misTenants.setTModified( dto.getTModified() );
        misTenants.setUCreated( dto.getUCreated() );
        misTenants.setUDeleted( dto.getUDeleted() );
        misTenants.setUModified( dto.getUModified() );
        misTenants.setTntAgrType( dto.getTntAgrType() );
        misTenants.setTntCompName( dto.getTntCompName() );
        misTenants.setTntCrno( dto.getTntCrno() );
        misTenants.setTntDocId( dto.getTntDocId() );
        misTenants.setTntExpat( dto.getTntExpat() );
        misTenants.setTntFullName( dto.getTntFullName() );
        misTenants.setTntId( dto.getTntId() );
        misTenants.setTntIdNo( dto.getTntIdNo() );
        misTenants.setTntPhone( dto.getTntPhone() );
        misTenants.setTntTele( dto.getTntTele() );

        return misTenants;
    }
}
