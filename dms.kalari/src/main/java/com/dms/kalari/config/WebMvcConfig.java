package com.dms.kalari.config;

/*import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations(
                    "classpath:/static/",
                    "classpath:/public/", 
                    "classpath:/resources/",
                    "classpath:/META-INF/resources/"
                );
    }
}

INSERT INTO "auth_apppagesoperations" ("operationid", "alias", "realpath", "entryby", "entrydate", "deleted", "t_created", "t_deleted", "t_modified", "u_created", "u_deleted", "u_modified", "operation", "real_path", "active", "apppageid") VALUES
(3,	'users_bynodeoff',	'/admin/users/bynodeoff',	1,	'2025-08-19 21:30:02.635373',	't',	'2025-08-19 21:30:02.635373',	NULL,	NULL,	NULL,	NULL,	NULL,	'VIEW',	'',	0,	4),
(1,	'users',	'/admin/users',	1,	'2025-08-19 21:30:02.635373',	't',	'2025-08-19 21:30:02.635373',	NULL,	NULL,	NULL,	NULL,	NULL,	'GET',	'',	0,	4);

INSERT INTO "auth_userprivilages" ("userprivilageid", "instid", "roleid", "moduleid", "operationuniqueid", "entryby", "entrydate", "deleted", "t_created", "t_deleted", "t_modified", "u_created", "u_deleted", "u_modified", "active", "apppageid") VALUES
(77,	NULL,	1,	2,	1,	1,	'2025-08-19 21:49:40.75294',	'f',	'2025-08-19 21:49:40.75294',	NULL,	NULL,	NULL,	NULL,	NULL,	1,	4),
(79,	NULL,	1,	2,	3,	1,	'2025-08-19 21:49:40.75294',	'f',	'2025-08-19 21:49:40.75294',	NULL,	NULL,	NULL,	NULL,	NULL,	1,	4);

*
*
*
*/