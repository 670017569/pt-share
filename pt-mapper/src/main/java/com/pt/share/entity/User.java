package com.pt.share.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
    private Long id;

    @Column
    private String username;
    @Column
    private Integer status;
    @Column
    private String password;
    @Column
    private String email;
    @Column
    private String phone;
    @Column
    private String wxOpenid;
    @Column
    private Integer deleted;

    private List<Role> roles;

}
