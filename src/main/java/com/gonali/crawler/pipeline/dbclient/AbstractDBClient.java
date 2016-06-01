package com.gonali.crawler.pipeline.dbclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by TianyuanPan on 5/4/16.
 */
public abstract class AbstractDBClient implements DBClient {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected boolean connOpen;
    protected Connection connection;


    protected void setConnOpen(boolean connOpen) {
        this.connOpen = connOpen;
    }


    public void setConnection(Connection connection) {
        this.connection = connection;
    }

}
