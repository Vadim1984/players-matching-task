package com.playwing.test.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MatchDto {
    private List<PlayerDto> source;
    private List<PlayerDto> target;
}
