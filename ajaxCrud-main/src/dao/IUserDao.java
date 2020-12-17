package dao;

import vo.Page;
import vo.User;

import java.util.List;

public interface IUserDao {
    User selectOne(User user) throws Exception;
    boolean insertOne(User user) throws Exception;
    int count (User user) throws Exception;
    boolean deleteUser(List<String> deleteUsernameList) throws Exception;
    List<User> query(User user, Page page) throws Exception;
    boolean updateUser(User user) throws Exception;
    boolean updateRole(String userName,String role) throws Exception;
}
