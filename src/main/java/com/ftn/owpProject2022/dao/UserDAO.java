package com.ftn.owpProject2022.dao;

import com.ftn.owpProject2022.model.User;

public interface UserDAO {
    int save(User user);
    int update(User user);
    User findOneById(String id);
    User findOneByEmailAndPassword(String email, String password);
}
