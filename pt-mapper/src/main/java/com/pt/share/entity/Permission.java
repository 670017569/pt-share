package com.pt.share.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "permission")
public class Permission implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
    private Long id;

    @Column
    private Long parentId;
    @Column
    private String name;
    @Column
    private String url;
    @Column
    private String description;
    @Column
    private Date created;
    @Column
    private Date updated;

}
