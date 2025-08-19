package com.example.tenant_service.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Document(indexName = "user_members")
public class UserMemberDocument {
    @Id
    private Long userId;

    @Field(type = FieldType.Text)
    private String userFname;

    @Field(type = FieldType.Text)
    private String userLname;

    @Field(type = FieldType.Short)
    private Short userStatus;

    @Field(type = FieldType.Long)
    private Long userDesig;

    @Field(type = FieldType.Text)
    private String designationName;

    @Field(type = FieldType.Long)
    private Long userNode;

    @Field(type = FieldType.Text)
    private String userUname;

    @Field(type = FieldType.Keyword)
    private String userEmail;

    @Field(type = FieldType.Keyword)
    private String userType;
}
