package com.api.domain.board.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class BoardPageResponse {

    private List<BoardListResponse> content;
    private PageInfo page;

    @Getter
    @AllArgsConstructor
    public static class PageInfo {
        private long totalElements;
        private int totalPages;
        private int number;
    }
}
