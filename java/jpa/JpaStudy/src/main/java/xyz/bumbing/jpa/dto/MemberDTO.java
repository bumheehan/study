package xyz.bumbing.jpa.dto;

import lombok.Data;

public class MemberDTO {

    @Data
    public static class MemberAddDTO {
	private String name;
    }
}
