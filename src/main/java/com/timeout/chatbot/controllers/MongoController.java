package com.timeout.chatbot.controllers;

import com.timeout.chatbot.domain.FbUserProfile;
import com.timeout.chatbot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mongo")
public class MongoController {

    @Autowired
    private UserRepository repository;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> doGet() {

        repository.deleteAll();

        FbUserProfile fbUserProfile =  new FbUserProfile();
        fbUserProfile.setFirstName("Fb User Profile");

//        User user1 = new User("ljfldj");
//        user1.setFbUserProfile(fbUserProfile);
//        repository.save(user1);
//
//        System.out.println("Customers found with findAll():");
//        System.out.println("-------------------------------");
//        for (User user : repository.findAll()) {
//            System.out.println(user);
//        }
//        System.out.println();

        return ResponseEntity.ok("Ok");
    }
}
