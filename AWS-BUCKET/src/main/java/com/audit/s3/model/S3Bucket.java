package com.audit.s3.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@Table(name = "s3_bucket")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class S3Bucket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    private String imageName;

    private String imageType;

    private String imageUrl;

    private String imageBuket;

}
