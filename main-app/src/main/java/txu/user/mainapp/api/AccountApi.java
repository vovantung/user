package txu.user.mainapp.api;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import txu.user.mainapp.base.AbstractApi;
import txu.user.mainapp.entity.AccountEntity;
import txu.user.mainapp.service.AccountService;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AccountApi extends AbstractApi {

    private final AccountService accountService;

//    @PostMapping("/update-avatar")
//    public AccountEntity updateAvatar(
//            @RequestPart(value = "file", required = false) MultipartFile file, // ✅ optional
//            @RequestPart("username") String username,
//            @RequestPart("password") String password,
//            @RequestPart("firstName") String firstName,
//            @RequestPart("lastName") String lastName,
//            @RequestPart("email") String email,
//            @RequestPart("phoneNumber") String phoneNumber
//
//    ) throws  IOException, NoSuchAlgorithmException, InvalidKeyException{
//        return accountService.updateAvatar(file, username, password, firstName, lastName, email, phoneNumber);
//    }

}
