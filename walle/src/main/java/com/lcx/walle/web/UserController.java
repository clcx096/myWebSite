package com.lcx.walle.web;

import com.lcx.walle.mongo.po.User;
import com.lcx.walle.mongo.po.UserD;
import com.lcx.walle.utils.CommonUtils;
import com.lcx.walle.utils.GsonUtils;
import com.lcx.walle.utils.MongoQueryUtils;
import com.lcx.walle.utils.ResultUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    @RequestMapping("/add")
    public void add(HttpServletResponse response, User user){
        try{
            user.setId(CommonUtils.getUUID());
            mongoTemplate.insert(user);
            ResultUtils.sendData(response, user);
        } catch (Exception e){
            e.printStackTrace();
            ResultUtils.sendError(response);
        }
    }

    @RequestMapping("/query")
    public void query(HttpServletResponse response, User user){
        try{
            logger.info(GsonUtils.toJsonString(user));
            Query query = new Query();
            if (StringUtils.isNotBlank(user.getName())){
                MongoQueryUtils.like(query,"name", user.getName());
            }
            if (StringUtils.isNotBlank(user.getSex())){
                MongoQueryUtils.like(query,"sex", user.getSex());
            }
            List<User> users = mongoTemplate.find(query, User.class);
            ResultUtils.sendData(response, users);
        } catch (Exception e){
            e.printStackTrace();
            ResultUtils.sendError(response);
        }
    }

    @RequestMapping("/query2")
    public void query(HttpServletResponse response, UserD user){
        try{
            logger.info(GsonUtils.toJsonString(user));
            Query query = new Query();
            if (StringUtils.isNotBlank(user.getName())){
                MongoQueryUtils.like(query,"name", user.getName());
            }
            List<UserD> users = mongoTemplate.find(query, UserD.class);
            ResultUtils.sendData(response, users);
        } catch (Exception e){
            e.printStackTrace();
            ResultUtils.sendError(response);
        }
    }
}
