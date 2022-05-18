package com.cassandra.restaurant.core.beans;

public class JCRData {

    private String lastModified;
    private String lastModifiedBy;
    private String template;
    private String createdBy;
    private String created;
    private String primaryType;
    private String title;
    private String slingResoucreType;

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getPrimaryType() {
        return primaryType;
    }

    public void setPrimaryType(String primaryType) {
        this.primaryType = primaryType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSlingResoucreType() {
        return slingResoucreType;
    }

    public void setSlingResoucreType(String slingResoucreType) {
        this.slingResoucreType = slingResoucreType;
    }
}
