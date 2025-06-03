package com.harinem.file_service.repository;

import com.harinem.file_service.entity.FileMgmt;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileMgmtRepository extends MongoRepository<FileMgmt,String> {
}
