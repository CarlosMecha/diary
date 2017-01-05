package com.carlosmecha.diary.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Tag model.
 *
 * Created by Carlos on 12/25/16.
 */
@Entity
@Table(name = "tags")
public class Tag {

    @Id
    private String code;
    @Column(name = "created_on")
    private Date createdOn;

    public Tag() {
    }

    public Tag(String code) {
        this(code, new Date());
    }

    public Tag(String code, Date createdOn) {
        this();
        this.code = code;
        this.createdOn = createdOn;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    @Override
    public String toString() {
        return code;
    }

}
