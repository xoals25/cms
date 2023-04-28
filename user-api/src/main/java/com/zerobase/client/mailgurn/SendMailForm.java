package com.zerobase.client.mailgurn;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SendMailForm {
    private String from;
    private String to;
    private String subject;
    private String text;
}
