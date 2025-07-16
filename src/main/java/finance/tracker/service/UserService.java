package finance.tracker.service;

import finance.tracker.model.User;
import finance.tracker.repository.UserDAO;

public class UserService {
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
    public User login(String email, String password) {
        return userDAO.login(email, password);
    }
    public boolean register(String email, String password, String username) {
        return userDAO.register(email, password, username);
    }
}
