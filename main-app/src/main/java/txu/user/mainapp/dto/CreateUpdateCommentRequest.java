package txu.user.mainapp.dto;

import lombok.Getter;
import lombok.Setter;
import txu.user.mainapp.entity.CommentEntity;

@Getter
@Setter
public class CreateUpdateCommentRequest {
    CommentEntity comment;
}
