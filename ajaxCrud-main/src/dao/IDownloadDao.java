package dao;

import vo.Download;

import java.util.List;

public interface IDownloadDao {
    List<Download> selectAll() throws Exception;
}
