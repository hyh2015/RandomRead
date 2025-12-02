package com.test;

import com.core.PropertyManager;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;


public class TestMission {

    private static final Logger logger = Logger.getLogger(TestMission.class);

    private String missionType="1" ;

    private ExecutorService dataTaskPool = Executors.newFixedThreadPool(1000,new ThreadFactory(){
        AtomicInteger atomic=new AtomicInteger();
        @Override
        public Thread newThread(Runnable r){
            return new Thread(r,"Thread_Pool_"+atomic.getAndIncrement());
        }
    });

    public void run(){
        String maxThread = PropertyManager.getInstance().getProperty("MaxThread");
        int threadNum = Integer.parseInt(maxThread);
        missionType = PropertyManager.getInstance().getProperty("query.type");
        logger.info("Thread num = "+threadNum);
        ArrayList<String> numberList = getNumberList();
        ArrayList<String> tableList = getTableList();

        switch (missionType) {
            case "2":test2(threadNum, numberList, tableList);
                break;
            case "3":test3(threadNum, numberList, tableList);
                break;
            case "4":test4(threadNum, numberList, tableList);
                break;
            case "5":test5(threadNum, numberList, tableList);
                break;
            default:test(threadNum, numberList, tableList);
        }

    }

    private void test(int threadNum, ArrayList<String> numberList, ArrayList<String> tableList) {
        for (int i = 0 ; i < threadNum ; i++){
            TestThread testThread = new TestThread(""+i,numberList, tableList);
            this.dataTaskPool.execute(testThread);
        }
    }

    private void test2(int threadNum, ArrayList<String> numberList, ArrayList<String> tableList) {
        for (int i = 0 ; i < threadNum ; i++){
            TestThread2 testThread2 = new TestThread2(""+i,numberList, tableList);
            this.dataTaskPool.execute(testThread2);
        }
    }

    private void test3(int threadNum, ArrayList<String> numberList, ArrayList<String> tableList) {
        for (int i = 0 ; i < threadNum ; i++){
            TestThread3 testThread3 = new TestThread3(""+i,numberList, tableList);
            this.dataTaskPool.execute(testThread3);
        }
    }

    private void test4(int threadNum, ArrayList<String> numberList, ArrayList<String> tableList) {
        for (int i = 0 ; i < threadNum ; i++){
            TestThread4 testThread4 = new TestThread4(""+i,numberList, tableList);
            this.dataTaskPool.execute(testThread4);
        }
    }

    private void test5(int threadNum, ArrayList<String> numberList, ArrayList<String> tableList) {
        for (int i = 0 ; i < threadNum ; i++){
            TestThread5 testThread5 = new TestThread5(""+i,numberList, tableList);
            this.dataTaskPool.execute(testThread5);
        }
    }


    private ArrayList<String> getTableList() {
        ArrayList<String> tableList  = new ArrayList<>();
        try {
            String driver = PropertyManager.getInstance().getProperty("resultdb.driver");
            Class.forName(PropertyManager.getInstance().getProperty("resultdb.driver"));
            String url = PropertyManager.getInstance().getProperty("resultdb.Url");
            String user = PropertyManager.getInstance().getProperty("resultdb.User");
            String password = PropertyManager.getInstance().getProperty("resultdb.Pwd");
            logger.info("begin to get tables");
            logger.info(String.format("driver:%s, url:%s, user:%s, password:%s", driver, url, user, password));
            try (Connection connection = DriverManager.getConnection(url, user, password)) {
                try(Statement statement = connection.createStatement()){
                    statement.setFetchSize(1000);
                    try(ResultSet resultSet = statement.executeQuery("select * from tb_table_list")){
                        while (resultSet.next()){
                            tableList.add(resultSet.getString(1));
                        }
                        logger.info(String.format("Get tableList ok! size is [%s]", tableList.size()));
                    }
                }
            }
        }catch (Exception e){
            logger.error(e.toString(),e);
        }
        if (tableList.size() == 0) {
            logger.error("Query table is null!!");
        }
        return tableList;
    }

    private ArrayList<String> getNumberList() {
        ArrayList<String> numberList  = new ArrayList<>();
        try {
            String driver = PropertyManager.getInstance().getProperty("resultdb.driver");
            Class.forName(PropertyManager.getInstance().getProperty("resultdb.driver"));
            String url = PropertyManager.getInstance().getProperty("resultdb.Url");
            String user = PropertyManager.getInstance().getProperty("resultdb.User");
            String password = PropertyManager.getInstance().getProperty("resultdb.Pwd");
            logger.info("begin to get number");
            logger.info(String.format("driver:%s, url:%s, user:%s, password:%s", driver, url, user, password));
            try (Connection connection = DriverManager.getConnection(url, user, password)) {
                try(Statement statement = connection.createStatement()){
                    statement.setFetchSize(1000);
                    String limit = driver.toUpperCase().contains("ORACLE") ? " where rownum <  1000000":" limit 1000000";
                    try(ResultSet resultSet = statement.executeQuery("select * from tb_usernum_list "+limit)){
                        int count = 0;
                        while (resultSet.next()){
                            numberList.add(resultSet.getString(1));
                            if (++count % 10_0000 == 0) logger.info("get count= "+count);
                        }
                        logger.info(String.format("Get numberlist ok! size is [%s]", numberList.size()));
                    }
                }
            }
        }catch (Exception e){
            logger.error(e.toString(),e);
        }
        return numberList;
    }


}
