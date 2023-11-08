package com.medbid.MedBid.rest.v1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/user")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class.getSimpleName());

    @GetMapping("/profile")
    @PreAuthorize("hasRole('user')")
    public void getProfile() {
        log.info("Getting user profile from multiple sources!!!");
    }

}


// TODO handle HttpMessageNotReadableException

