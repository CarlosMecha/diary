package com.carlosmecha.diary.models;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Notebook model.
 *
 * Created by Carlos on 12/25/16.
 */
@Entity
@Table(name = "notebooks")
public class Notebook {

    @Id
    @NotEmpty
    private String code;
    @NotEmpty
    private String name;
    private Date createdOn;

    public Notebook() {
    }

    public Notebook(String code, String name) {
        this(code, name, new Date());
    }

    public Notebook(String code, String name, Date createdOn) {
        this();
        this.code = code;
        this.name = name;
        this.createdOn = createdOn;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    @Override
    public String toString() {
        return String.format("Notebook %s: %s", code, name);
    }

}
