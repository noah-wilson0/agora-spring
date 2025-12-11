package com.agora.debate.debate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FastApiSummaryResponse {

    @JsonProperty("board_id")
    private Long boardId;

    @JsonProperty("pros_content") // JSON의 키값과 정확히 매핑
    private String prosContent;

    @JsonProperty("cons_content")
    private String consContent;
}