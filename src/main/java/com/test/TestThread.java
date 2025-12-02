package com.test;

import com.core.PropertyManager;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

/**
 *where usernum = ?
 */
public class TestThread implements Runnable {
    private static final Logger logger = Logger.getLogger(TestThread.class);

    String name ;

    ArrayList<String> numberList;
    ArrayList<String> tableList;

    Random random = new Random();

    public TestThread(String name,ArrayList<String> numberList,ArrayList<String> tableList){
        this.name = name;
        this.numberList = numberList;
        this.tableList = tableList;
    }


    @Override
    public void run() {
        try {
            Class.forName(PropertyManager.getInstance().getProperty("resultdb.driver"));
            String url = PropertyManager.getInstance().getProperty("resultdb.Url");
            String user = PropertyManager.getInstance().getProperty("resultdb.User");
            String password = PropertyManager.getInstance().getProperty("resultdb.Pwd");

            try (Connection connection = DriverManager.getConnection(url, user, password)) {
                long begintime, returnTime, countFinishTime, count;
                while (true) {
                    try {
                        String uuid = UUID.randomUUID().toString().replaceAll("-", "").substring(10);
                        logger.info("thread " + this.name + " begin mission-" + uuid);
                        //获取随机number值
                        String number = getRandomNum();
                        //获取随机table
                        String tableName = getRandomTable();
                        //生成执行sql
                        String sql = String.format(String.format("select * from %s WHERE USERNUM = '%s'", tableName, number));
                        logger.info(String.format("sql [%s]",sql));
                        try (Statement statement = connection.createStatement()) {
                            statement.setFetchSize(1000);
                            begintime = System.currentTimeMillis();
                            //执行
                            try (ResultSet resultSet = statement.executeQuery(sql)) {
                                returnTime = System.currentTimeMillis();
                                logger.info("Get ResultSet...");
                                count = 0;
                                while (resultSet.next()) {
                                    resultSet.getString("USERNUM");
                                    count++;
                                }
                                countFinishTime = System.currentTimeMillis();
                            }
                            logger.info("thread " + this.name + " query mission-" + uuid + " finish return cost " + (returnTime - begintime) + " count cost = " + (countFinishTime - begintime));
                        }
                        try (PreparedStatement preparedStatement = connection.prepareStatement("insert into tb_test_record_sql1(thread_name,missionid,query_num,return_time,count_finish_time,count,record_time)values(?,?,?,?,?,?,now())")) {
                            preparedStatement.setString(1, this.name);
                            preparedStatement.setString(2, uuid);
                            preparedStatement.setString(3, number);
                            preparedStatement.setLong(4, returnTime - begintime);
                            preparedStatement.setLong(5, countFinishTime - begintime);
                            preparedStatement.setLong(6, count);
                            preparedStatement.execute();
                            logger.info("thread " + this.name + " record mission-" + uuid + " finsih");
                        }
                    } catch (Exception e) {
                        logger.error(e.toString(), e);
                    }
                }

            } catch (Exception e) {
                logger.error(e.toString(), e);
            }
        }catch (Exception e) {
            logger.error(e.toString(), e);
        }

    }

    private String getRandomTable() {
        if (this.tableList.size() == 1)
            return tableList.get(0);
        return this.tableList.get(random.nextInt(tableList.size()-1));
    }

    private String getRandomNum() {
        return this.numberList.get(random.nextInt(numberList.size()-1));
    }
}
