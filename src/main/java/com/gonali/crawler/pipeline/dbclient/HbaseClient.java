package com.gonali.crawler.pipeline.dbclient;

import com.gonali.crawler.model.CrawlerData;
import com.gonali.crawler.utils.ConfigUtils;
import com.gonali.crawler.utils.CrawlerDataUtils;
import com.gonali.crawler.utils.HbasePoolUtils;
import com.gonali.crawler.utils.RandomUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by TianyuanPan on 5/18/16.
 */
public class HbaseClient extends AbstractDBClient {


    private String tableName;
    private String columnFamilyName;
//    private HConnection connection;
    private HTableInterface myTable;


    private List<CrawlerData> dataList;

    public HbaseClient() {

        this.dataList = new ArrayList<>();
        this.tableName = ConfigUtils.getResourceBundle().getString("HBASE_TABLE_NAME");
        this.columnFamilyName = ConfigUtils.getResourceBundle().getString("HBASE_COLUMNFAMILY_NAME");
    }

    public String getTableName() {
        return tableName;
    }

    public String getColumnFamilyName() {
        return columnFamilyName;
    }

    @Override
    public Object getConnection() {
        return null;
    }

    @Override
    public void closeConnection() {

    }

    @Override
    public int doSetInsert() {
        int count = 0;

        for (int i = 0; i < dataList.size(); ++i) {

            try {
                count += this.insertRecord(tableName, RandomUtils.getRandomString(50) + "_" + new Date().getTime(), columnFamilyName, dataList.get(i));
            } catch (Exception ex) {
                logger.warn("HbaseClient doSetInsert Exception!!! Message: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
//        this.dataList.clear();

        this.dataList = new ArrayList<>();

        return count;
    }

    @Override
    public boolean isConnOpen() {
        return false;
    }


    public void add(CrawlerData data) {

        this.dataList.add(data);
    }


    public synchronized int insertRecord(String tableName, String rowKey, String columnFamilyName, CrawlerData data) {

        String columnQualifier = null;
        String value = null;
        String type = null;


//        try {
//            this.connection = HbasePoolUtils.getHConnection();
            myTable = HbasePoolUtils.getHTable(tableName);//this.connection.getTable(tableName);
//        } catch (Exception ex) {
//            logger.error("Hbase get connection Error or get table Error!!, Message: " + ex.getMessage());
//            ex.printStackTrace();
//        }


        Put put = new Put(Bytes.toBytes(rowKey));// 设置rowkey

        CrawlerDataUtils utils = CrawlerDataUtils.getCrawlerDataUtils(data);

        List<Map<String, Object>> myDataList = utils.getAttributeInfoList();
        try {

            for (int i = 0; i < myDataList.size(); ++i) {
                try {
                    columnQualifier = myDataList.get(i).get("name").toString();
                    type = myDataList.get(i).get("type").toString();

                    switch (type) {

                        case "int":
                            value = Integer.toString((int) myDataList.get(i).get("value"));
                            break;
                        case "long":
                            value = Long.toString((long) myDataList.get(i).get("value"));
                            break;
                        case "boolean":
                            value = Boolean.toString((boolean) myDataList.get(i).get("value"));
                            break;
                        default:
                            value = (String) myDataList.get(i).get("value");
                            break;
                    }

                } catch (Exception ex) {

                    logger.warn("get data Exception! columnQualifier = " + columnQualifier + ", value = " + value);
                    ex.printStackTrace();

                }

                if (value == null)
                    value = "null";

                if (columnQualifier != null)
                    put.add(Bytes.toBytes(columnFamilyName),
                            Bytes.toBytes(columnQualifier),
                            Bytes.toBytes(value));
            }

            myTable.put(put);

        } catch (Exception ex) {

            try {
                myTable.close();
//                this.connection.close();
            } catch (Exception exc) {
                logger.warn("Hbase table.close() error!!! Message: " + exc.getMessage());
                exc.printStackTrace();
            }

            logger.warn("HBase Put data Exception!!! Message: " + ex.getMessage());
            ex.printStackTrace();
            return 0;
        }


        try {

            myTable.close();
//            this.connection.close();

        } catch (Exception ex) {
            logger.warn("Hbase table.close() error!!! Message: " + ex.getMessage());
            ex.printStackTrace();
        }
//        System.out.println("add data Success!");
//        logger.debug("Insert data Success!");

        return 1;
    }

}
