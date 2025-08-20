package txu.user.mainapp.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import txu.user.mainapp.base.AbstractApi;
import txu.user.mainapp.dto.CreateUpdatePostRequest;
import txu.user.mainapp.dto.GetPostRequest;
import txu.user.mainapp.entity.PostEntity;
import txu.user.mainapp.service.PostService;


import java.util.List;

@CrossOrigin(origins = "*", allowCredentials = "false", maxAge = 86400, allowedHeaders = "*")
@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostApi extends AbstractApi {

    private final PostService postService;

    @PostMapping(value = "/create-or-update", consumes = "application/json")
    public PostEntity createOrUpdate(@RequestBody CreateUpdatePostRequest request) {
        return postService.createOrUpdate(request.getPost());
    }

    @PostMapping(value = "/get-post", consumes = "application/json")
    public PostEntity getByUnsignedTitle(@RequestBody GetPostRequest request) {
        return postService.getByUnsignedTitle(request.getUnsignedTitle());

    }

    @PostMapping(value = "/get-all-post", consumes = "application/json")
    public List<PostEntity> getAllPost() {
        return postService.getAllPost();
    }
}
