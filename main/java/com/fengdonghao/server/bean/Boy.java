package com.fengdonghao.server.bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.*;

/**
 * @Author:fdh
 * @Description:
 * @Date： Create in 15:08 2017/12/22
 */
@Entity
public class Boy implements Serializable{
    private static final long serialVersionUID=1L;
    private int age;

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "Boy{" +
                "age=" + age +
                ", id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public void setId(int uid) {
        this.id = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Id
    @GeneratedValue
    private int id;
    private String name;


    public Boy(int age, String name) {
        this.age = age;
        this.name = name;
    }
}
