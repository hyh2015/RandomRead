package com.test;

import com.core.PropertyManager;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

/**
 * where usernum in (?,?,?,?,?,?,?,?,?,?)
 */
public class TestThread2 implements Runnable {
    private static final Logger logger = Logger.getLogger(TestThread2.class);

    String name ;

    ArrayList<String> numberList;
    ArrayList<String> tableList;

    Random random = new Random();

    public TestThread2(String name, ArrayList<String> numberList, ArrayList<String> tableList){
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
                        String number = getRandomNum();
                        String tableName = getRandomTable();
                        String sql = String.format("select * from %s where usernum in ('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s')",
                                tableName, number, getRandomNum(), getRandomNum(), getRandomNum(), getRandomNum(), getRandomNum(),
                                getRandomNum(), getRandomNum(), getRandomNum(), getRandomNum());
                        logger.info(String.format("SQL is [%s]",sql));
                        try (Statement statement = connection.createStatement()) {
                            statement.setFetchSize(1000);
                            begintime = System.currentTimeMillis();
                            try (ResultSet resultSet = statement.executeQuery(sql)) {
                                returnTime = System.currentTimeMillis();
                                count = 0;
                                while (resultSet.next()) {
                                    resultSet.getString("USERNUM");
                                    count++;
                                }
                                countFinishTime = System.currentTimeMillis();
                            }
                            logger.info("thread " + this.name + " query mission-" + uuid + " finish return cost " + (returnTime - begintime) + " count cost = " + (countFinishTime - begintime));

                        }
                        try (PreparedStatement preparedStatement = connection.prepareStatement("insert into tb_test_record_sql2(thread_name,missionid,query_num,return_time,count_finish_time,count,record_time)values(?,?,?,?,?,?,now())")) {
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

    private String getRandomNum() {
        return this.numberList.get(random.nextInt(numberList.size()-1));
    }
    private String getRandomTable() {
        if (this.tableList.size() == 1)
            return tableList.get(0);
        return this.tableList.get(random.nextInt(tableList.size()-1));
    }
}
