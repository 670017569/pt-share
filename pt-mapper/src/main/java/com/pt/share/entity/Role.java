package com.pt.share.entity;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Table(name = "role")
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
    private Long id;

    @Column
    private Long parentId;
    @Column
    private String name;
    @Column
    private String description;
    @Column
    private Date created;
    @Column
    private Date updated;

    private List<Permission> permissions;

}
