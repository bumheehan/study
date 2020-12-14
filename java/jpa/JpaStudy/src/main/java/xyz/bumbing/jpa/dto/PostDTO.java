package xyz.bumbing.jpa.dto;

import lombok.Data;

public class PostDTO {

    @Data
    public static class PostAddDTO {
	private String title;
	private String description;
	private Long memberId;

    }
}
