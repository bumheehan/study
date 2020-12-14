package xyz.bumbing.model.general;

import com.fasterxml.jackson.annotation.JsonFilter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter("MyFilter")
public class JsonFilterModel {

    public String name;
    public int age;

}
