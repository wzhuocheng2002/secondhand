package com.example.entity;

import java.io.Serializable;

/**
 * 类别
*/
public class Category extends Account implements Serializable {
    private static final long serialVersionUID = 1L;

    /** ID */
    private Integer id;

    /** 密码 */
    private String password;
    /** 姓名 */
    private String name;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}




