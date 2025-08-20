package txu.user.mainapp.service;

import com.amazonaws.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import txu.common.exception.BadParameterException;
import txu.common.exception.NotFoundException;
import txu.user.mainapp.dao.PostDao;
import txu.user.mainapp.entity.PostEntity;

import java.text.Normalizer;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostDao postDao;
    @Transactional
    public PostEntity createOrUpdate(PostEntity post) {

        if (StringUtils.isNullOrEmpty(post.getTitle()) || StringUtils.isNullOrEmpty(post.getContent())) {
            log.error("Post title or content field is required");
            throw new BadParameterException("Post title or content field is required");
        }

        // Add new article
        if (post.getId()==null || post.getId()==0) {
            String content = post.getContent();
            // Thay thế thẻ <p> đầu của đoạn mã html của content thành thẻ <p> mới
            if(content.substring(0, 2).equals("<p")){
                content = replaceFirstPTag(content, "<p style=\"margin-top: 0; margin-bottom: 0;\">");
            }
            post.setContent(content);
            post.setCreatedAt(DateTime.now().toDate());

            String normalized = Normalizer.normalize(post.getTitle(), Normalizer.Form.NFD);
            // Xóa các dấu bằng regex
            String withoutDiacritics = normalized.replaceAll("\\p{M}", "");
            // Chuyển về chữ thường và thay thế khoảng trắng bằng dấu "-"
            post.setUnsignedTitle(withoutDiacritics.replaceAll("[đĐ]", "d").toLowerCase().replaceAll("\\s+", "-"));
            postDao.save(post);
            return post;
        }

        // Update article
        PostEntity postForUpdate = postDao.findById(post.getId());
        if (postForUpdate == null) {
           throw new NotFoundException("Not found article with id " + post.getId());
        }else {

            postForUpdate.setTitle(post.getTitle());
            postForUpdate.setContent(post.getContent());
            return postDao.save(postForUpdate);
        }
    }

    public  String replaceFirstPTag(String html, String newPTag) {
        return html.replaceFirst("<p.*?>", newPTag);
    }

    public PostEntity getByUnsignedTitle(String unsignedTitle) {
        if (StringUtils.isNullOrEmpty(unsignedTitle)) {
            log.error("UnsignedTitle of Post id is required");
            throw new BadParameterException("UnsignedTitle of Post id is required");
        }
       return postDao.getByUnsignedTitle(unsignedTitle);
    }
    public List<PostEntity> getAllPost() {
        return postDao.getAllPost();
    }

    public void delete(String postId){
        PostEntity post = postDao.findById(postId);
        if (post !=null){
            postDao.delete(post);
        }
    }

}
