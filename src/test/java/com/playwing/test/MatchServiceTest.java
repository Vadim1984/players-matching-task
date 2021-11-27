package com.playwing.test;

import com.playwing.test.dto.MatchDto;
import com.playwing.test.dto.PlayerDto;
import com.playwing.test.services.impl.DefaultMatchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class MatchServiceTest {

    @InjectMocks
    private DefaultMatchService matchService;

    @BeforeEach
    public void setUp() {
        matchService.setPlayersSkillDeltaThreshold(100);
        matchService.setSquadSkillsDeltaThreshold(50);
    }

    @Test
    void testThatPlayersWithoutSquadAreMatchedProperly() {

        PlayerDto playerDto1 = new PlayerDto("0x183A89", 1264, -1);
        PlayerDto playerDto2 = new PlayerDto("0x153A89", 764, -1);
        PlayerDto playerDto3 = new PlayerDto("0x123A89", 264, -1);
        PlayerDto playerDto4 = new PlayerDto("0x163A89", 964, -1);
        PlayerDto playerDto5 = new PlayerDto("0x133A89", 464, -1);
        PlayerDto playerDto6 = new PlayerDto("0x143A89", 563, -1);
        PlayerDto playerDto7 = new PlayerDto("0x173A89", 1063, -1);

        MatchDto expectedFirstMatch = new MatchDto(List.of(playerDto5), List.of(playerDto6));
        MatchDto expectedSecondMatch = new MatchDto(List.of(playerDto4), List.of(playerDto7));
        List<MatchDto> expectedResult = List.of(expectedFirstMatch, expectedSecondMatch);

        List<MatchDto> actualResult = matchService.matchPlayers(List.of(playerDto1, playerDto2, playerDto3, playerDto4,
                playerDto5, playerDto6, playerDto7));

        assertThat(actualResult).hasSameElementsAs(expectedResult);
    }

    @Test
    void testThatPlayersWithSquadAreMatchedProperly() {

        PlayerDto playerDto1 = new PlayerDto("0x183A89", 1, 1);
        PlayerDto playerDto2 = new PlayerDto("0x153A89", 2, 1);
        PlayerDto playerDto3 = new PlayerDto("0x123A89", 5, 2);
        PlayerDto playerDto4 = new PlayerDto("0x163A89", 6, 2);
        PlayerDto playerDto5 = new PlayerDto("0x133A89", 3, 3);
        PlayerDto playerDto6 = new PlayerDto("0x143A89", 4, 3);
        PlayerDto playerDto7 = new PlayerDto("0x173A89", 1, 4);
        PlayerDto playerDto8 = new PlayerDto("0x173A89", 1, 4);
        PlayerDto playerDto9 = new PlayerDto("0x173A89", 2, 5);
        PlayerDto playerDto10 = new PlayerDto("0x173A89", 2, 5);

        List<PlayerDto> squad1 = List.of(playerDto7, playerDto8);
        List<PlayerDto> squad2 = List.of(playerDto1, playerDto2);
        List<PlayerDto> squad3 = List.of(playerDto9, playerDto10);
        List<PlayerDto> squad4 = List.of(playerDto5, playerDto6);
        MatchDto expectedFirstMatch = new MatchDto(squad1, squad2);
        MatchDto expectedSecondMatch = new MatchDto(squad3, squad4);
        List<MatchDto> expectedResult = List.of(expectedFirstMatch, expectedSecondMatch);

        List<MatchDto> actualResult = matchService.matchPlayers(List.of(playerDto1, playerDto2, playerDto3,
                playerDto4, playerDto5, playerDto6,
                playerDto7, playerDto8, playerDto9, playerDto10));

        assertThat(actualResult).hasSameElementsAs(expectedResult);
    }

    @Test
    void testThatPlayersWithSquadAndWithoutSquadAreMatchedProperly() {

        PlayerDto playerDto1 = new PlayerDto("0x183A89", 1, 1);
        PlayerDto playerDto2 = new PlayerDto("0x153A89", 2, 1);
        PlayerDto playerDto3 = new PlayerDto("0x123A89", 5, 2);
        PlayerDto playerDto4 = new PlayerDto("0x163A89", 6, 2);
        PlayerDto playerDto5 = new PlayerDto("0x133A89", 3, 3);
        PlayerDto playerDto6 = new PlayerDto("0x143A89", 4, 3);
        PlayerDto playerDto7 = new PlayerDto("0x173A89", 1, 4);
        PlayerDto playerDto8 = new PlayerDto("0x173A89", 1, 4);
        PlayerDto playerDto9 = new PlayerDto("0x173A89", 2, 5);
        PlayerDto playerDto10 = new PlayerDto("0x173A89", 2, 5);
        PlayerDto playerDto11 = new PlayerDto("0x183A89", 1264, -1);
        PlayerDto playerDto12 = new PlayerDto("0x153A89", 764, -1);
        PlayerDto playerDto13 = new PlayerDto("0x123A89", 264, -1);
        PlayerDto playerDto14 = new PlayerDto("0x163A89", 964, -1);
        PlayerDto playerDto15 = new PlayerDto("0x133A89", 464, -1);
        PlayerDto playerDto16 = new PlayerDto("0x143A89", 563, -1);
        PlayerDto playerDto17 = new PlayerDto("0x173A89", 1063, -1);

        List<PlayerDto> playerWithoutSquad1 = List.of(playerDto15);
        List<PlayerDto> playerWithoutSquad2 = List.of(playerDto16);
        List<PlayerDto> playerWithoutSquad3 = List.of(playerDto14);
        List<PlayerDto> playerWithoutSquad4 = List.of(playerDto17);

        List<PlayerDto> squad1 = List.of(playerDto7, playerDto8);
        List<PlayerDto> squad2 = List.of(playerDto1, playerDto2);
        List<PlayerDto> squad3 = List.of(playerDto9, playerDto10);
        List<PlayerDto> squad4 = List.of(playerDto5, playerDto6);

        MatchDto expectedMatch1 = new MatchDto(playerWithoutSquad1, playerWithoutSquad2);
        MatchDto expectedMatch2 = new MatchDto(playerWithoutSquad3, playerWithoutSquad4);
        MatchDto expectedMatch3 = new MatchDto(squad1, squad2);
        MatchDto expectedMatch4 = new MatchDto(squad3, squad4);

        List<MatchDto> expectedResult = List.of(expectedMatch1, expectedMatch2, expectedMatch3, expectedMatch4);

        List<MatchDto> actualResult = matchService.matchPlayers(List.of(playerDto1, playerDto2, playerDto3,
                playerDto4, playerDto5, playerDto6, playerDto7, playerDto8, playerDto9, playerDto10,
                playerDto11, playerDto12, playerDto13, playerDto14, playerDto15, playerDto16, playerDto17));

        assertThat(actualResult).hasSameElementsAs(expectedResult);
    }
}
