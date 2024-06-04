package apple.backend.ticketManagement.api.dto;

import java.util.Date;

public class CommentDto {
    private String author;
    private String content;
    private String createdAt;

    public CommentDto(Date createdAt, String author, String content) {
        this.author = author;
        this.content = content;
        this.createdAt = createdAt.toString();
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
