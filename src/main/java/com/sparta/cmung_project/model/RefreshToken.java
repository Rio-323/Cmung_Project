package com.sparta.cmung_project.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Entity
@Getter
@NoArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String refreshToken;
    @NotBlank
    private String memberEmail;

    public RefreshToken(String token, String email) {
        this.refreshToken = token;
        this.memberEmail = email;
    }

    public RefreshToken updateToken(String token) {
        this.refreshToken = token;
        return this;
    }

}
