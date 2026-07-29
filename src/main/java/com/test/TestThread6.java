package com.test;

import com.core.PropertyManager;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;


/**
 * where begintime >= '随机起始时间' and begintime <= '随机结束时间'
 * begintime范围: '2025-01-01 00:00:00' 到 '2028-01-01 00:00:00'
 */
public class TestThread6 implements Runnable {
    private static final Logger logger = Logger.getLogger(com.test.TestThread.class);

    String name;

    ArrayList<String> numberList;
    ArrayList<String> tableList;

    Random random = new Random();

    // begintime范围: 2025-01-01 00:00:00 到 2028-01-01 00:00:00
    private static final long MIN_TIME = getTimeMillis("2025-01-01 00:00:00");
    private static final long MAX_TIME = getTimeMillis("2028-01-01 00:00:00");
    // 固定查询2年时间范围
    private static final long TWO_YEARS_MILLIS = 2L * 365 * 24 * 60 * 60 * 1000;

    public TestThread6(String name, ArrayList<String> numberList, ArrayList<String> tableList) {
        this.name = name;
        this.numberList = numberList;
        this.tableList = tableList;
    }

    private static long getTimeMillis(String dateStr) {
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.parse(dateStr).getTime();
        } catch (Exception e) {
            return 0;
        }
    }

    private String formatTime(long mills) {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new java.util.Date(mills));
    }


    @Override
    public void run() {
        try {
            Class.forName(PropertyManager.getInstance().getProperty("resultdb.driver"));
            String url = PropertyManager.getInstance().getProperty("resultdb.Url");
            String user = PropertyManager.getInstance().getProperty("resultdb.User");
            String password = PropertyManager.getInstance().getProperty("resultdb.Pwd");

            String dbName = PropertyManager.getInstance().getProperty("resultdb.dbType");

            try (Connection connection = DriverManager.getConnection(url, user, password)) {

                if (dbName != null && !dbName.isEmpty() && dbName.equalsIgnoreCase("ivorysql")) {
                    try (Statement initStmt = connection.createStatement()) {
                        initStmt.execute("SET plan_cache_mode = 'force_generic_plan'");
                        logger.info("thread " + this.name + " set plan_cache_mode = force_generic_plan successfully.");
                    } catch (Exception e) {
                        logger.error("thread " + this.name + "failed to set plan_cache_mode", e);
                    }
                }


                long begintime, returnTime, countFinishTime, count;
                while (true) {
                    try {
                        String uuid = UUID.randomUUID().toString().replaceAll("-", "").substring(10);
                        logger.info("thread " + this.name + " begin mission-" + uuid);

                        // 随机生成查询区间（begintime范围: 2025-01-01 到 2028-01-01），固定2年范围
                        long maxStartTime = MAX_TIME - TWO_YEARS_MILLIS;
                        long randomStartTime = MIN_TIME + (long)(random.nextDouble() * (maxStartTime - MIN_TIME));
                        long randomEndTime = randomStartTime + TWO_YEARS_MILLIS;
                        String startTimeStr = formatTime(randomStartTime);
                        String endTimeStr = formatTime(randomEndTime);

                        //获取随机table
                        String tableName = getRandomTable();
                        //生成执行sql - 按begintime范围查询
                        String sql = String.format("select * from %s WHERE begintime >= '%s' AND begintime <= '%s'",
                                tableName, startTimeStr, endTimeStr);
                        logger.info(String.format("sql [%s]", sql));
                        try (Statement statement = connection.createStatement()) {
                            statement.setFetchSize(1000);
                            begintime = System.currentTimeMillis();
                            //执行
                            try (ResultSet resultSet = statement.executeQuery(sql)) {
                                returnTime = System.currentTimeMillis();
                                logger.info("Get ResultSet...");
                                count = 0;
                                while (resultSet.next()) {
                                    resultSet.getString("begintime");
                                    count++;
                                }
                                countFinishTime = System.currentTimeMillis();
                            }
                            logger.info("thread " + this.name + " query mission-" + uuid + " finish return cost " + (returnTime - begintime) + " count cost = " + (countFinishTime - begintime));
                        }
                        // 记录查询条件和结果
                        String queryCondition = startTimeStr + " to " + endTimeStr;
                        try (PreparedStatement preparedStatement = connection.prepareStatement("insert into tb_test_record_sql6(thread_name,missionid,query_num,return_time,count_finish_time,count,record_time)values(?,?,?,?,?,?,now())")) {
                            preparedStatement.setString(1, this.name);
                            preparedStatement.setString(2, uuid);
                            preparedStatement.setString(3, queryCondition);
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
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }

    }

    private String getRandomTable() {
        if (this.tableList.size() == 1)
            return tableList.get(0);
        return this.tableList.get(random.nextInt(tableList.size() - 1));
    }
}


