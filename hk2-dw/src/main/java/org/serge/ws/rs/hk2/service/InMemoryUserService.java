package org.serge.ws.rs.hk2.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jvnet.hk2.annotations.Service;
import org.serge.ws.rs.hk2.dto.User;

@Service
public class InMemoryUserService implements UserService {

    private List<User> users = new ArrayList<>();

    {
        users.add(new User("test 1", 1));
        users.add(new User("test 2", 2));
        users.add(new User("test 3", 3));
    }
    public List<User> getUsers() {
        return users;
    }

    public List<User> list() {
        return Collections.unmodifiableList(users);
    }
}
