package txu.user.mainapp.dto;

import lombok.Getter;
import lombok.Setter;
import txu.user.mainapp.entity.PostEntity;

@Getter
@Setter
public class CreateUpdatePostRequest {
    PostEntity post;
}
