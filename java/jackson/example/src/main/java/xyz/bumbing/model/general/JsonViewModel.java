package xyz.bumbing.model.general;

import com.fasterxml.jackson.annotation.JsonView;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import xyz.bumbing.custom.Views;

@ToString
@AllArgsConstructor
@NoArgsConstructor
public class JsonViewModel {

    @JsonView(Views.Public.class)
    public int id;
    @JsonView(Views.Public.class)
    public String itemName;
    @JsonView(Views.Internal.class)
    public String ownerName;
}
