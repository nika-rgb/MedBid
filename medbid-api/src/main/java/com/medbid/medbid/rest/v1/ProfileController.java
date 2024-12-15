package com.medbid.medbid.rest.v1;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(path = "/api/v1/medbid/profile")
@RestController
public class ProfileController {

    @GetMapping
    public ResponseEntity<Void> getProfile() {
        return ResponseEntity.ok(null);
    }

}
