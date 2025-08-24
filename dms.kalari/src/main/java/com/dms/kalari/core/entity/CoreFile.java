package com.dms.kalari.core.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import com.dms.kalari.common.BaseEntity;

@Entity
@Table(name = "core_files")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoreFile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long fileId;

    @Column(name = "file_src", length = 50, nullable = false)
    private String fileSrc;

    @Column(name = "file_ref_id", nullable = false)
    private Long fileRefId;

    @Column(name = "file_actual_name", nullable = false)
    private String fileActualName;

    @Column(name = "file_exten")
    private String fileExten;

    @Column(name = "file_size")
    private Long fileSize;
    
    @Column(name = "file_path")
    private String filePath;
    
    
}
