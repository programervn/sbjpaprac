package com.thaipd.sbjpaprac.entity;

import javax.persistence.*;
import java.util.List;

//https://dzone.com/articles/spring-boot-jpa-hibernate-oracle

@Entity
public class Team {

    @Id
    //@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "team_Sequence")
    //@SequenceGenerator(name = "team_Sequence", sequenceName = "TEAM_SEQ")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            mappedBy = "team")
    private List<Player> players;

    public Team() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Player> getPlayers() {
        return players;
    }

}
