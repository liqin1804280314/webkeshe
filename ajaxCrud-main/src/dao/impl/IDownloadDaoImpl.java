package dao.impl;

import dao.IDownloadDao;
import vo.Download;

import javax.xml.xpath.XPath;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class IDownloadDaoImpl implements IDownloadDao {

    private Connection con;
    private PreparedStatement pst;

    public IDownloadDaoImpl(Connection con) {
        this.con = con;
    }

    @Override
    public List<Download> selectAll() throws Exception{
        String sql = "select id,name,path,description,size,star,image from t_downloadList";
        this.pst = this.con.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();
        List<Download> downloadList = new ArrayList<>();
        while(rs.next()){
            int id = rs.getInt("id");
            String name = rs.getString("name");
            String path = rs.getString("path");
            String description = rs.getString("description");
            long size = rs.getLong("size");
            String fileSize = FormatFileSize(size);
            int star = rs.getInt("star");
            String image = rs.getString("image");
            Download download = new Download(id, name, path, description, fileSize, star, image);
            downloadList.add(download);
        }
        return downloadList;
    }
    public String FormatFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }
}
