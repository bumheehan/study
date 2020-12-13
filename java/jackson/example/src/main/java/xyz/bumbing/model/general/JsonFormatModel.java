package xyz.bumbing.model.general;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JsonFormatModel {

    @JsonFormat(shape = Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    public Date testField;

}
