package com.example.springbootapi.service;

import com.example.springbootapi.api.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private List<User> userList;

    public UserService() {
        userList = new ArrayList<>();

        User user1 = new User(1, "Jan", 22, "jan@mail.com");
        User user2 = new User(2, "Lars", 21, "lars@mail.com");
        User user3 = new User(3, "Alena", 20, "alena@mail.com");
        User user4 = new User(4, "Marven", 21, "marven@mail.com");
        User user5 = new User(5, "Ledejna", 24, "ledejna@mail.com");

        userList.addAll(Arrays.asList(user1, user2, user3, user4, user5));
    }

    public Optional<User> getUser(Integer id) {
        Optional<User> optional = Optional.empty();

        for(User user:userList){
            if(id == user.getId()){
                optional = Optional.of(user);
                return optional;
            }
        }
        return optional;
    }
}
