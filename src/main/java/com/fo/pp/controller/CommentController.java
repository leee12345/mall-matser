package com.fo.pp.controller;

import com.fo.pp.common.Response;
import com.fo.pp.pojo.po.Comment;
import com.fo.pp.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CommentController {
    @Autowired
    CommentService commentService;

    @RequestMapping("/comment/save_comment.do")
    @ResponseBody
    public Response save_comment(String orderNo, String comment_level, String comment_content, String commodityNo){

        Comment comment = new Comment(Long.parseLong(orderNo),
                Integer.parseInt(comment_level),
                comment_content,
                Long.parseLong(commodityNo));

        if(commentService.save_comment(comment, orderNo) != 0) {
            return Response.error(1, "评论添加失败");
        }

        return Response.success("评论添加成功");
    }

    @RequestMapping("/comment/get_comment_detail.do")
    @ResponseBody
    public Response get_comment_detail(String orderNo){
        return Response.success(commentService.get_comment_detail(orderNo));
    }

    @RequestMapping("/comment/get_commodity_comment.do")
    @ResponseBody
    public Response get_commodity_comment(String commodityNo){
        return Response.success(commentService.get_commodity_comment(commodityNo));
    }
}
