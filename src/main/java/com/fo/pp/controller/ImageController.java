package com.fo.pp.controller;

import com.fo.pp.common.Response;
import com.fo.pp.pojo.po.PPFile;
import com.fo.pp.pojo.vo.ImageURL;
import com.fo.pp.service.PPFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Controller
public class ImageController {

    @Autowired
    private PPFileService ppFileService;

    @ResponseBody
    @RequestMapping("/mgr/product/upload.do")
    public Response uploadImage(String filename, MultipartFile stream, HttpServletRequest request){
        if(stream == null){
            return Response.error(1, "参数错误");
        }
        if(filename == null){
            filename = stream.getOriginalFilename();
        }
        if(stream.isEmpty()){
            return Response.error(1, "不能上传空文件");
        }
        if(!isValidImageFormat(filename)){
            return Response.error(1, "不是合理的图片名, 仅支持 jpg|jpeg|png|gif|bmp|svg");
        }
        try {
            int id = ppFileService.uploadFile(filename, stream.getBytes());
            if(id == 0){
                return Response.error(1, "上传图片失败");
            }
            //String baseUrl=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort+request.getContextPath()+"/";
            //String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/upload/" + id;
            String url = "/upload/" + id;
            ImageURL imageURL = new ImageURL();
            imageURL.setAssessId(id);
            imageURL.setUrl(url);
            return Response.success(imageURL);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(1, "上传图片出现异常");
        }
    }

    @RequestMapping("/upload/{id}")
    public void downloadImage(@PathVariable Integer id, HttpServletRequest request, HttpServletResponse response){
//        这边貌似会报错啊
//        这边的实现还是比较模糊的，所以先暂时记录下来
        PPFile ppFile = ppFileService.downloadFile(id);
        try {
            OutputStream os = response.getOutputStream();
            //如果文件不存在或不是合理的文件名，则直接返回
            if (ppFile == null || !isValidImageFormat(ppFile.getFilename())) {
                os.close();
                return;
            }
            //否则以流的形式返回文件
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", "attachment; filename=" + ppFile.getFilename());
            os.write(ppFile.getStream());
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断一个文件不否中合适的图片后缀
     * @param filename 文件名
     * @return 是否是合适的图片后缀名
     */
    boolean isValidImageFormat(String filename){
        return filename.endsWith("jpg") || filename.endsWith("jpeg") || filename.endsWith("png")
                || filename.endsWith("gif") || filename.endsWith("svg") || filename.endsWith("bmp");
    }
}
