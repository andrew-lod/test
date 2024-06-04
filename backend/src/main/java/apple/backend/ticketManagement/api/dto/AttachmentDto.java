package apple.backend.ticketManagement.api.dto;

import java.util.Date;

public class AttachmentDto {
    private String name;
    private String url;
    private String uploader;
    private Date createdAt;

    public AttachmentDto(Date createdAt, String uploader, String name, String url) {
        this.name = name;
        this.url = url;
        this.uploader = uploader;
        this.createdAt = createdAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUploader() {
        return uploader;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
