package com.zerobase.client.user;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChangeBalanceForm {
    private String from;
    private String message;
    private Integer money;
}
