package com.example.tenant_service.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import com.example.tenant_service.document.UserMemberDocument;
import java.util.List;

public interface UserMemberElasticsearchRepository extends ElasticsearchRepository<UserMemberDocument, Long> {
    List<UserMemberDocument> findByUserFnameContainingOrUserLnameContaining(String fname, String lname);
}
