package br.com.unipds.springboot_intro.model.evento;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class SubscriptionId implements Serializable {
    @ManyToOne
    @JoinColumn(name = "tbl_user_user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "tbl_session_session_id")
    private Session session;

    public SubscriptionId() {}
    public SubscriptionId(User user, Session session) {
        this.user = user;
        this.session = session;
    }

    // Getters, Setters, Equals e HashCode (obrigatórios para chave composta)
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Session getSession() { return session; }
    public void setSession(Session session) { this.session = session; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubscriptionId that = (SubscriptionId) o;
        return Objects.equals(user, that.user) && Objects.equals(session, that.session);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, session);
    }
}