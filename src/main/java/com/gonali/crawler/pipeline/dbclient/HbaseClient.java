package com.gonali.crawler.pipeline.dbclient;

import com.gonali.crawler.model.CrawlerData;
import com.gonali.crawler.utils.ConfigUtils;
import com.gonali.crawler.utils.CrawlerDataUtils;
import com.gonali.crawler.utils.RandomUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by TianyuanPan on 5/18/16.
 */
public class HbaseClient extends AbstractDBClient {

    /**
     * 声明静态配置
     * 初始化配置
     */
    private static String hostnames;
    private static String port;
    private static String tableName;
    private static String columnFamilyName;
    private static HTablePool myPool = null;
    public static final Configuration conf;

    static {
        conf = HBaseConfiguration.create();
        hostnames = ConfigUtils.getResourceBundle().getString("HBASE_HOSTNAMES");
        port = ConfigUtils.getResourceBundle().getString("HBASE_PORT");
        tableName = ConfigUtils.getResourceBundle().getString("HBASE_TABLE_NAME");
        columnFamilyName = ConfigUtils.getResourceBundle().getString("HBASE_COLUMNFAMILY_NAME");
        conf.set("hbase.zookeeper.quorum", hostnames);
        myPool = new HTablePool(conf, 100);
    }


    private List<CrawlerData> dataList;

    public HbaseClient() {

        this.dataList = new ArrayList<>();

    }

    public static String getTableName() {
        return tableName;
    }

    public static String getColumnFamilyName() {
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
        int i = 0;

        for (CrawlerData o : dataList) {

            try {
                i += this.insertRecord(tableName, RandomUtils.getRandomString(50) + "_" + new Date().getTime(), columnFamilyName, o);
            } catch (Exception ex) {
                logger.warn("HbaseClient doSetInsert Exception!!! Message: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
        this.dataList.clear();

        return i;
    }

    @Override
    public boolean isConnOpen() {
        return false;
    }


    public void add(CrawlerData data) {

        this.dataList.add(data);
    }


    public int insertRecord(String tableName, String rowKey, String columnFamilyName, CrawlerData data) throws Exception {

        HTableInterface myTable = myPool.getTable(tableName);
        Put put = new Put(Bytes.toBytes(rowKey));// 设置rowkey

        String columnQualifier = null;
        String value = null;
        String type = null;

        CrawlerDataUtils utils = CrawlerDataUtils.getCrawlerDataUtils(data);

        List<Map<String, Object>> myDataList = utils.getAttributeInfoList();

        try {
            for (Map<String, Object> o : myDataList) {
                try {
                    columnQualifier = o.get("name").toString();
                    type = o.get("type").toString();

                    switch (type) {

                        case "int":
                            value = Integer.toString((int) o.get("value"));
                            break;
                        case "long":
                            value = Long.toString((long) o.get("value"));
                            break;
                        case "boolean":
                            value = Boolean.toString((boolean) o.get("value"));
                            break;
                        default:
                            value = (String) o.get("value");
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

            myTable.close();
            logger.warn("HBase Put data Exception!!! Message: " + ex.getMessage());
            ex.printStackTrace();
            return 0;
        }


        myTable.close();

        System.out.println("add data Success!");
        logger.debug("Insert data Success!");

        return 1;
    }

}
