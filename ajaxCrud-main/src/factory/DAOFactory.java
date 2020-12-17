package factory;


import dao.IDownloadDao;
import dao.IManageDao;
import dao.IUserDao;

import dao.impl.IDownloadDaoImpl;
import dao.impl.IUserDaoImpl;
import dao.impl.ManageDaoImpl;

import java.sql.Connection;

public class DAOFactory {
    public static IUserDao getUserDAOInstance(Connection con) {

        return new IUserDaoImpl(con);
    }
    public static IDownloadDao getDownloadDAOInstance(Connection con){
        return new IDownloadDaoImpl(con);
    }
    public static IManageDao getManageDaoInstance(Connection con){return new ManageDaoImpl(con);
    }
}
