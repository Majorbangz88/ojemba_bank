package com.big_joe.ojemba_bank.data.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class  User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Schema(name = "User first name")
    private String firstName;
    @Schema(name = "User other name")
    private String otherName;
    @Schema(name = "User last name")
    private String lastName;
    @Schema(name = "User gender")
    private String gender;
    @Schema(name = "User address")
    private String address;
    @Schema(name = "User state of origin")
    private String stateOfOrigin;
    @Schema(name = "User account number")
    private String accountNumber;
    @Schema(name = "User account balance")
    private BigDecimal accountBalance;
    @Schema(name = "User email")
    private String email;
    @Schema(name = "User password")
    private String password;
    @Schema(name = "User phone number")
    private String phoneNumber;
    @Schema(name = "User alternative phone number")
    private String alternativePhoneNumber;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime modifiedAt;
}
