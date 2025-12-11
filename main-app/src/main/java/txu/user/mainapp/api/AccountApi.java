package txu.user.mainapp.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import txu.user.mainapp.base.AbstractApi;
import txu.user.mainapp.dto.LinkDto;
import txu.user.mainapp.dto.LinkRequest;
import txu.user.mainapp.dto.UpdateAvatarRequest;
import txu.user.mainapp.entity.AccountEntity;
import txu.user.mainapp.service.AccountService;

@Slf4j
@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountApi extends AbstractApi {

    private final AccountService accountService;

    @PostMapping("/update-avatar")
    public AccountEntity updateAvatar(@RequestBody UpdateAvatarRequest request) {
        return accountService.updateAvatar(request.getFilename(), request.getUsername(), request.getPassword(),
                request.getFirstName(), request.getLastName(), request.getEmail(), request.getPhoneNumber());
    }

    @PostMapping("/get-presignedurl-for-put")
    public LinkDto getPreSignedUrlForPut(@RequestBody LinkRequest request) {
        LinkDto linkDto = new LinkDto();
        try {
            return accountService.getPreSignedUrlForPut(request.getFilename());
        } catch (Exception e) {

        }
        return linkDto;
    }
}
