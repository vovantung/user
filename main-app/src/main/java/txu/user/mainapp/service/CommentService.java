package txu.user.mainapp.service;

import com.amazonaws.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import txu.common.exception.BadParameterException;
import txu.common.exception.NotFoundException;
import txu.user.mainapp.dao.CommentDao;
import txu.user.mainapp.entity.CommentEntity;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentDao commentDao;
    @Transactional
    public CommentEntity createOrUpdate(CommentEntity comment) {

        if (StringUtils.isNullOrEmpty(comment.getContent())) {
            log.error("Comment content field is required");
            throw new BadParameterException("Comment content field is required");
        }

        // Add new comment
        if (comment.getId() == null || comment.getId() == 0) {
            String content = comment.getContent();
            // Thay thế thẻ <p> đầu của đoạn mã html của content thành thẻ <p> mới
            if(content.substring(0, 2).equals("<p")){
                content = replaceFirstPTag(content, "<p style=\"margin-top: 0; margin-bottom: 0;\">");
            }
            comment.setContent(content);
            comment.setCreatedAt(DateTime.now().toDate());
            commentDao.save(comment);
            return comment;
        }

        // Update article
        CommentEntity articleForUpdate = commentDao.findById(comment.getId());
        if (articleForUpdate == null) {
           throw new NotFoundException("Not found article with id " + comment.getId());
        }else {

            articleForUpdate.setContent(comment.getContent());
            return commentDao.save(articleForUpdate);
        }
    }

    public  String replaceFirstPTag(String html, String newPTag) {
        return html.replaceFirst("<p.*?>", newPTag);
    }

    public CommentEntity getComment(String id) {
        if (StringUtils.isNullOrEmpty(id)) {
            log.error("Article id is required");
            throw new BadParameterException("Article id is required");
        }
        CommentEntity comment = commentDao.findById(id);
        return comment;
    }

    public List<CommentEntity> getCommentOfPost(int postId) {
        if (postId == 0) {
            log.error("PostId id is required");
            throw new BadParameterException("PostId id is required");
        }
        List<CommentEntity> rs = commentDao.getCommentsOfPost(postId);
        return rs;

    }

    public List<CommentEntity> getAllComment() {
        return commentDao.getAllArticle();
    }

    public void delete(String articleId){
        CommentEntity product = commentDao.findById(articleId);
        if (product !=null){
            commentDao.delete(product);
        }
    }

}
