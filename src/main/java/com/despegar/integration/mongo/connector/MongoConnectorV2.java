package com.despegar.integration.mongo.connector;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;

import java.net.UnknownHostException;

/**
 * Created by marina.rivero on 17/10/2016.
 */
public class MongoConnectorV2 extends MongoDBConnection {

    private String dbName;
    private String replicaSet;
    private String credentials;

    public MongoConnectorV2(String dbName, String replicaSet, String credentials) throws UnknownHostException {
        super(dbName, replicaSet);
        this.dbName = dbName;
        this.replicaSet = replicaSet;
        this.credentials = credentials;
    }


    @Override
    DB getDB() {
        MongoClient mongo = new MongoClient(new MongoClientURI("mongodb://"+credentials+"@"+replicaSet+"/"+dbName));
        return mongo.getDB(this.dbName);
    }
}
