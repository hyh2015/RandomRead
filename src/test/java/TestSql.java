import java.sql.*;

/**
 * @author Tens
 * @create 2023/9/15 17:22
 */
public class TestSql {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        String driver = "oracle.jdbc.OracleDriver";
        String url = "jdbc:oracle:thin:@192.168.2.169:1521/tdr";
        String user = "mycat";
        String password = "mycat";
        String sql = "insert into tb_yuzx(col1) values(?)";
        Class.forName(driver);
        Connection connection = DriverManager.getConnection(url, user, password);
        PreparedStatement psmt = connection.prepareStatement(sql);
        psmt.setObject(1, Types.VARCHAR, 4000);
        System.out.println(connection);
    }
}
