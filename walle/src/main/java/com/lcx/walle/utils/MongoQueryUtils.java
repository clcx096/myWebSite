package com.lcx.walle.utils;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

public class MongoQueryUtils {

    public static Query equal(Query query, String key, String name){
        if (query == null){
            query = new Query();
        }
        Criteria criteria=new Criteria(key);
        criteria.is(name);
        query.addCriteria(criteria);
        return query;
    }

    public static Query like(Query query, String key, String name){
        if (query == null){
            query = new Query();
        }
        Criteria criteria=new Criteria(key);
        criteria.regex("^.*"+name+".*$", "i");
        query.addCriteria(criteria);
        return query;
    }
}
