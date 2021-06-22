package com.thaipd.sbjpaprac.repository;

import com.thaipd.sbjpaprac.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    Team findByPlayers(long playerId);
}
