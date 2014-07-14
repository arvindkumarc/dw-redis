package com.arvindc.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SeatRequest {
    private List<String> seatNumbers;
    private Long session;
}
