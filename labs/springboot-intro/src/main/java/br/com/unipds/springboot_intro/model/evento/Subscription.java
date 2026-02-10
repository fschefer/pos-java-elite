package br.com.unipds.springboot_intro.model.evento;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_subscription")
public class Subscription {
    @EmbeddedId
    private SubscriptionId id;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "subscription_level")
    private Integer level;

    @Column(name = "unique_id", length = 45, nullable = false, unique = true)
    private String uniqueId;

    // Getters e Setters
    public SubscriptionId getId() { return id; }
    public void setId(SubscriptionId id) { this.id = id; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public Integer getLevel() { return level; }
    public void setLevel(Integer level) { this.level = level; }
    public String getUniqueId() { return uniqueId; }
    public void setUniqueId(String uniqueId) { this.uniqueId = uniqueId; }
}