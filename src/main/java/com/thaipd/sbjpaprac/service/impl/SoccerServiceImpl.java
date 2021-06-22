package com.thaipd.sbjpaprac.service.impl;

import com.thaipd.sbjpaprac.entity.Player;
import com.thaipd.sbjpaprac.entity.Team;
import com.thaipd.sbjpaprac.repository.PlayerRepository;
import com.thaipd.sbjpaprac.repository.TeamRepository;
import com.thaipd.sbjpaprac.service.SoccerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SoccerServiceImpl implements SoccerService {

    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private TeamRepository teamRepository;

    public List<String> getAllTeamPlayers(long teamId) {
        List<String> result = new ArrayList<String>();
        List<Player> players = playerRepository.findByTeamId(teamId);
        for (Player player : players) {
            result.add(player.getName());
        }

        return result;
    }
}
