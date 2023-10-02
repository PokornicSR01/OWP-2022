package com.ftn.owpProject2022.service;

import com.ftn.owpProject2022.model.User;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface UserService {
    User getLoggedUser(HttpSession session);
    User save(User user);
    User update(User user);
    User findOneById(String id);
    User findOneByEmailAndPassword(String email, String password);
}
