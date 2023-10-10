package com.fo.pp.service;

import com.fo.pp.dao.PPFileDAO;
import com.fo.pp.pojo.po.PPFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PPFileService {

    @Autowired
    private PPFileDAO ppFileDAO;

    /**
     * 上传文件
     * @param filename - 文件名
     * @param stream  - 二进制数据
     * @return 返回id
     */
    public int uploadFile(String filename, byte[] stream){
//        上传文件
        PPFile ppFile = new PPFile();
        ppFile.setFilename(filename);
        ppFile.setStream(stream);
        int result = ppFileDAO.insertFile(ppFile);
        if(result == 0){
            throw  new RuntimeException("文件存储到数据库失败");
        }
        return ppFile.getId();
    }

    /**
     * 下载文件
     * @param id  - 文件的id
     * @return 文件对象
     */
    public PPFile downloadFile(int id){
//
        return ppFileDAO.selectFile(id);
    }
}
