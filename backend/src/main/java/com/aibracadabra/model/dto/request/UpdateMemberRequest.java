package com.aibracadabra.model.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NonNull;

@Data
public class UpdateMemberRequest {
    @JsonIgnore
    private Integer mno;

    @NonNull
    @Schema(description = "회원 이름", example = "홍길동")
    private String name;
    @NonNull
    @Schema(description = "회원 이메일", example = "aibracadabra@aibracadabra.com")
    private String email;
    @NonNull
    @Schema(description = "현재 비밀 번호", example = "password123")
    private String currentPassword;

    @Schema(description = "새 비밀번호(옵션)", example ="aibracadabra")
    private String newPassword;
    @Schema(description = "새 비밀번호 확인(옵션)", example =  "aibracadabra")
    private String confirmPassword;
}
