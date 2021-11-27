package com.playwing.test.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PlayerDto {
    private String id;
    private int skill;
    private int squadId;
}
