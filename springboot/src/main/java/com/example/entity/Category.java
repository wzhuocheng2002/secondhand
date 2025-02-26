package com.example.entity;

import java.io.Serializable;

/**
 * 类别
*/
public class Category {
    private static final long serialVersionUID = 1L;

    /** ID */
    private Integer id;

    /** 姓名 */
    private String name;


    public Integer getId() {
        return id;
    }


    public void setId(Integer id) {
        this.id = id;
    }



    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }
}




