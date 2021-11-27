package com.playwing.test.controller;

import com.playwing.test.dto.MatchDto;
import com.playwing.test.dto.PlayerDto;
import com.playwing.test.services.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/players")
@RestController
public class MatchingPlayersController {

    @Autowired
    private MatchService matchService;

    @PostMapping("/match")
    public List<MatchDto> match(@RequestBody List<PlayerDto> players){
        return matchService.matchPlayers(players);
    }
}
