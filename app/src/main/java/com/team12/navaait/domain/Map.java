package com.team12.navaait.domain;

import java.util.Date;

/**
 * Created by Blen on 5/18/2017.
 */

public class Map {
    private String id;

    private String version;
    private String fileName;
    private Date created;

    public Map() {
    }

    public Map(String id, String version, String fileName, Date created) {
        this.id = id;
        this.version = version;
        this.fileName = fileName;
        this.created = created;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
