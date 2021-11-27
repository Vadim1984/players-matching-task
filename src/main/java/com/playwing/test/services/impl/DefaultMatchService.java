package com.playwing.test.services.impl;

import com.playwing.test.dto.MatchDto;
import com.playwing.test.dto.PlayerDto;
import com.playwing.test.services.MatchService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Setter
@Service
public class DefaultMatchService implements MatchService {

    private static final int PLAYER_WITHOUT_GROUP = -1;

    @Value("${players.skill.delta.threshold}")
    private int playersSkillDeltaThreshold;

    @Value("${squad.skills.delta.threshold}")
    private int squadSkillsDeltaThreshold;

    @Override
    public List<MatchDto> matchPlayers(List<PlayerDto> players) {
        Map<Integer, List<PlayerDto>> squads = Optional.ofNullable(players)
                .orElseGet(Collections::emptyList)
                .stream()
                .collect(Collectors.groupingBy(PlayerDto::getSquadId));

        List<MatchDto> matchedPlayersWithoutSquads = matchPlayersWithoutSquads(squads.remove(PLAYER_WITHOUT_GROUP));
        List<MatchDto> matchedPlayersWithSquads = matchPlayersWithSquads(squads);
        matchedPlayersWithoutSquads.addAll(matchedPlayersWithSquads);

        return matchedPlayersWithoutSquads;
    }

    private List<MatchDto> matchPlayersWithoutSquads(List<PlayerDto> players) {
        List<MatchDto> matchedPlayers = new ArrayList<>();

        List<PlayerDto> sortedPlayersBySkills = Optional.ofNullable(players)
                .orElseGet(Collections::emptyList)
                .stream()
                .sorted(Comparator.comparing(PlayerDto::getSkill))
                .collect(Collectors.toList());

        int regularStep = 1;
        int pairStep = 2;
        int step = regularStep;

        for (int i = 0; i < sortedPlayersBySkills.size() - step; i += step) {
            PlayerDto firstPlayer = sortedPlayersBySkills.get(i);
            PlayerDto secondPlayer = sortedPlayersBySkills.get(i + 1);

            if (firstPlayer == null || secondPlayer == null) {
                continue;
            }

            if (calculateDeltaOfSkillForPlayers(firstPlayer, secondPlayer) < playersSkillDeltaThreshold) {
                step = pairStep;
                matchedPlayers.add(new MatchDto(List.of(firstPlayer), List.of(secondPlayer)));
            } else {
                step = regularStep;
            }
        }

        return matchedPlayers;
    }

    private List<MatchDto> matchPlayersWithSquads(Map<Integer, List<PlayerDto>> squads) {
        List<MatchDto> matchedSquads = new ArrayList<>();
        Comparator<List<PlayerDto>> squadComparator = Comparator.comparing(list -> list.stream().mapToInt(PlayerDto::getSkill).sum());

        List<List<PlayerDto>> sortedSquads = Optional.ofNullable(squads)
                .orElseGet(Collections::emptyMap)
                .values()
                .stream()
                .sorted(squadComparator)
                .collect(Collectors.toList());

        if (sortedSquads.isEmpty()) {
            return matchedSquads;
        }

        int regularStep = 1;
        int pairStep = 2;
        int step = regularStep;

        for (int i = 0; i < sortedSquads.size() - step; i += step) {
            List<PlayerDto> firstSquad = sortedSquads.get(i);
            List<PlayerDto> secondSquad = sortedSquads.get(i + 1);

            if (firstSquad == null || secondSquad == null || firstSquad.isEmpty() || secondSquad.isEmpty()) {
                continue;
            }

            if (calculateDeltaOfSkillForSquads(firstSquad, secondSquad) < squadSkillsDeltaThreshold) {
                step = pairStep;
                matchedSquads.add(new MatchDto(firstSquad, secondSquad));
            } else {
                step = regularStep;
            }

        }

        return matchedSquads;
    }

    private int calculateDeltaOfSkillForPlayers(PlayerDto firstPlayer, PlayerDto secondPlayer) {
        return Math.abs(firstPlayer.getSkill() - secondPlayer.getSkill());
    }

    private int calculateDeltaOfSkillForSquads(List<PlayerDto> firstSquad, List<PlayerDto> secondSquad) {
        int sumSkillsOFFirstSquad = firstSquad.stream().mapToInt(PlayerDto::getSkill).sum();
        int sumSkillsOFSecondSquad = secondSquad.stream().mapToInt(PlayerDto::getSkill).sum();

        return Math.abs(sumSkillsOFFirstSquad - sumSkillsOFSecondSquad);
    }
}
