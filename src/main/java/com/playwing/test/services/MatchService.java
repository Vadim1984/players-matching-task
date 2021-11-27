package com.playwing.test.services;

import com.playwing.test.dto.MatchDto;
import com.playwing.test.dto.PlayerDto;

import java.util.List;

public interface MatchService {
    List<MatchDto> matchPlayers(List<PlayerDto> players);
}
