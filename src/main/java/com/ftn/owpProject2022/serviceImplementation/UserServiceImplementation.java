package com.ftn.owpProject2022.serviceImplementation;

import com.ftn.owpProject2022.dao.UserDAO;
import com.ftn.owpProject2022.model.User;
import com.ftn.owpProject2022.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
public class UserServiceImplementation implements UserService {
    @Autowired
    UserDAO userDAO;

    @Override
    public User getLoggedUser(HttpSession session) {
        User loggedUser = (User) session.getAttribute("user");
        return loggedUser;
    }

    @Override
    public User save(User user) {
        userDAO.save(user);
        return user;
    }

    @Override
    public User update(User user) {
        userDAO.update(user);
        return user;
    }

    @Override
    public User findOneById(String id) {
        return userDAO.findOneById(id);
    }

    @Override
    public User findOneByEmailAndPassword(String email, String password) {
        return userDAO.findOneByEmailAndPassword(email, password);
    }
}
