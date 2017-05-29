package org.serge.ws.rs.hk2.service;

import java.util.List;

import org.jvnet.hk2.annotations.Contract;
import org.serge.ws.rs.hk2.dto.User;

@Contract
public interface UserService {

    List<User> list();

}
