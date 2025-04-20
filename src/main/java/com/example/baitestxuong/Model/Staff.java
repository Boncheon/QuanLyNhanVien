package com.example.baitestxuong.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Nationalized;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "staff")
public class Staff {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")

    private UUID id;

    @Column(name = "status", columnDefinition = "tinyint")
    private Short status;


    @Column(name = "created_date")
    private Long createdDate;

    @Column(name = "last_modified_date")
    private Long lastModifiedDate;

    @NotBlank(message = "Email FE không được để trống")
    @Size(max = 100, message = "Email FE phải nhỏ hơn 100 ký tự")
    @Pattern(regexp = "^\\S+$", message = "Email FE không được chứa khoảng trắng")
    @Pattern(regexp = "^[\\p{ASCII}]+$", message = "Email FE không được chứa ký tự tiếng Việt")
    @Pattern(
            regexp = "^[a-zA-Z0-9._%+-]+@fe\\.edu\\.vn$",
            message = "Email FE phải đúng định dạng và kết thúc bằng @fe.edu.vn"
    )
    @Column(name = "account_fe")
    private String accountFe;


    @NotBlank(message = "Email FPT không được để trống")
    @Size(max = 100, message = "Email FPT phải nhỏ hơn 100 ký tự")
    @Pattern(
            regexp = "^\\S+$",
            message = "Email FPT không được chứa khoảng trắng"
    )
    @Pattern(
            regexp = "^[\\p{ASCII}]+$",
            message = "Email FPT không được chứa ký tự tiếng Việt"
    )
    @Pattern(
            regexp = "^[a-zA-Z0-9._%+-]+@fpt\\.edu\\.vn$",
            message = "Email FPT phải đúng định dạng và kết thúc bằng @fpt.edu.vn"
    )
    @Column(name = "account_fpt")
    private String accountFpt;

    @Size(max = 255)
    @NotBlank(message = "tên không được để trống")
    @Column(name = "name")
    private String name;

    @NotBlank(message = "Mã không để trống")
    @Size(max = 15, message = "Mã phải nhỏ hơn 15 kí tự")
    @Column(name = "staff_code")
    private String staffCode;

}