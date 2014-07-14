package com.arvindc.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Seat {
    private Long id;
    private String name;
    private String status;
    private Long sessionId;
}
