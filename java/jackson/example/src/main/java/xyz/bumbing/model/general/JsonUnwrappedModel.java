package xyz.bumbing.model.general;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JsonUnwrappedModel {

    public String normalField;

    @JsonUnwrapped
    public TestVV testVV;

    public static TestVV getVV() {
	return TestVV.builder().t1("t1").t2("t2").build();
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    static class TestVV {
	public String t1;
	public String t2;
    }
}
