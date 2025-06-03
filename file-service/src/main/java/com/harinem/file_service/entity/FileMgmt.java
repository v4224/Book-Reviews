package com.harinem.file_service.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@Document(collection = "file_mgmt")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileMgmt {

    @MongoId
    String id;

    String contentType;
    long size;
    String md5Checksum;
    String path;
    String ownerId;


}
