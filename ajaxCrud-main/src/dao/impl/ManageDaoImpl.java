package dao.impl;

import dao.IManageDao;
import vo.Resource;
import vo.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ManageDaoImpl implements IManageDao {
    private Connection con;
    private PreparedStatement pst;

    public ManageDaoImpl(Connection con) {
        this.con = con;
    }

    @Override
    public List<Resource> getResourceList(User user) throws Exception{
        List<Resource> resourceList = new ArrayList<>();
        String sql = "select * from t_resource where resourceId in(" +
                "select resourceId from t_role_resource where roleId in(" +
                "select roleId from t_user_role where userName = ?))";
        this.pst = this.con.prepareStatement(sql);
        this.pst.setString(1, user.getUsername());
        ResultSet rs  = this.pst.executeQuery();
        while(rs.next()){
            Resource resource = new Resource(rs.getInt("resourceId"), rs.getString("resourceName"), rs.getString("url"));
            resourceList.add(resource);
        }
        return resourceList;
    }
}
