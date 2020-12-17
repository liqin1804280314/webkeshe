package dao;

import vo.Resource;
import vo.User;

import java.util.List;

public interface IManageDao {
    List<Resource> getResourceList(User user) throws  Exception ;
}
