package com.example.tenant_service.mapper;

import com.example.tenant_service.dto.TenantDTO;
import com.example.tenant_service.entity.MisTenants;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-22T20:55:15+0530",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.13 (Ubuntu)"
)
@Component
public class MisTenantsMapperImpl implements MisTenantsMapper {

    @Override
    public TenantDTO toDTO(MisTenants entity) {
        if ( entity == null ) {
            return null;
        }

        TenantDTO tenantDTO = new TenantDTO();

        tenantDTO.setUCreated( entity.getUCreated() );
        tenantDTO.setTCreated( entity.getTCreated() );
        tenantDTO.setTModified( entity.getTModified() );
        tenantDTO.setUModified( entity.getUModified() );
        tenantDTO.setUDeleted( entity.getUDeleted() );
        tenantDTO.setTDeleted( entity.getTDeleted() );
        tenantDTO.setDeleted( map( entity.getDeleted() ) );
        tenantDTO.setActive( entity.getActive() );
        tenantDTO.setTntId( entity.getTntId() );
        tenantDTO.setTntFullName( entity.getTntFullName() );
        tenantDTO.setTntCompName( entity.getTntCompName() );
        tenantDTO.setTntPhone( entity.getTntPhone() );
        tenantDTO.setTntTele( entity.getTntTele() );
        tenantDTO.setTntIdNo( entity.getTntIdNo() );
        tenantDTO.setTntCrno( entity.getTntCrno() );
        tenantDTO.setTntExpat( entity.getTntExpat() );
        tenantDTO.setTntAgrType( entity.getTntAgrType() );
        tenantDTO.setTntDocId( entity.getTntDocId() );

        return tenantDTO;
    }

    @Override
    public MisTenants toEntity(TenantDTO dto) {
        if ( dto == null ) {
            return null;
        }

        MisTenants misTenants = new MisTenants();

        misTenants.setUCreated( dto.getUCreated() );
        misTenants.setTCreated( dto.getTCreated() );
        misTenants.setTModified( dto.getTModified() );
        misTenants.setUModified( dto.getUModified() );
        misTenants.setUDeleted( dto.getUDeleted() );
        misTenants.setTDeleted( dto.getTDeleted() );
        misTenants.setDeleted( map( dto.getDeleted() ) );
        misTenants.setActive( dto.getActive() );
        misTenants.setTntId( dto.getTntId() );
        misTenants.setTntFullName( dto.getTntFullName() );
        misTenants.setTntCompName( dto.getTntCompName() );
        misTenants.setTntPhone( dto.getTntPhone() );
        misTenants.setTntTele( dto.getTntTele() );
        misTenants.setTntIdNo( dto.getTntIdNo() );
        misTenants.setTntCrno( dto.getTntCrno() );
        misTenants.setTntExpat( dto.getTntExpat() );
        misTenants.setTntAgrType( dto.getTntAgrType() );
        misTenants.setTntDocId( dto.getTntDocId() );

        return misTenants;
    }
}
