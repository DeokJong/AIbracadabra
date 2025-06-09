package com.aibracadabra.model.dto.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardImage {
    private Long imgNo;
    private int bno;
    private String filename;
    private String contentType;
    private Long size;
    private String storagePath;

}
