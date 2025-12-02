/*
import com.core.PropertyManager;

import org.junit.Test;



public class test {

    @Test
    public void test1(){
        PropertyManager.getInstance();
    }

    @Test
    public void test2() {
        try {
            Class.forName("org.highgo.jdbc.Driver");
            System.out.println(111);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }






//    public static void main(String[] args) throws InterruptedException {
////        String classPath = TableMigration.class.getProtectionDomain().getCodeSource().getLocation().getPath();
//        String classPath = TableMigration.class.getProtectionDomain().getCodeSource().getLocation().getPath();
//        classPath = classPath.replaceAll("tableMigration.jar","");
//        PropertyConfigurator.configure(classPath + ConfigUtil.LOG4J_PATH);
//        Connection connection = null;
//        Statement statement = null;
//
//        String funName = "FUNC_TEST_"+UUID.randomUUID().toString().replaceAll("-","").substring(15,25);
//        System.out.println(funName);
//        try {
////            Connection connection = ConnectionManager.getInstance().getResultConnection();
////
//            connection = ConnectionManager.getInstance().getSrcConnection();
//            statement  = connection.createStatement();
////            PreparedStatement preparedStatement = connection1.prepareStatement("insert into FEIZ_BLOB_TEST16(photo) values(?)");
////            ResultSet rs =  statement.executeQuery("select * from tmp_FEIZ_BLOB_TEST99");
//            long beginTime = System.currentTimeMillis();
//            String createFuncSql = "BEGIN " +
//                    "execute immediate " +
//                    "\'CREATE OR REPLACE FUNCTION &func_name& (&tableName& IN VARCHAR2,split_chr  IN VARCHAR2)\n" +
//                    "   RETURN split_chr_table\n" +
//                    "   PIPELINED\n" +
//                    "   --PARALLEL_ENABLE\n" +
//                    "IS\n" +
//                    "   p_split_chr_table          split_chr_table;\n" +
//                    "   p_split_chr_type           split_chr_type;\n" +
//                    "   p_split_clob_table         split_clob_table;\n" +
//                    "   p_split_clob_type          split_clob_type;\n" +
//                    "   j                          INT := 0;\n" +
//                    "   i                          INT := 1;\n" +
//                    "   len                        INT := 0;\n" +
//                    "   len1                       INT := 0;\n" +
//                    "BEGIN\n" +
//                    "   FOR c IN (select rowid row_id, &pcolumn& column_value from &tableName&)\n" +
//                    "   LOOP\n" +
//                    "      len := LENGTH (c.column_value);\n" +
//                    "      len1 := LENGTH (split_chr);\n" +
//                    "      j := 0;\n" +
//                    "      i := 1;\n" +
//                    "\n" +
//                    "      ---solution of null value of pcolumn\n" +
//                    "     IF len IS NULL\n" +
//                    "     THEN\n" +
//                    "        p_split_chr_type :=\n" +
//                    "           split_chr_type (c.row_id, '');\n" +
//                    "        PIPE ROW (p_split_chr_type);\n" +
//                    "     END IF;\n" +
//                    "\n" +
//                    "     ---If len is null then exit random because of null values can not compare\n" +
//                    "      WHILE j < len\n" +
//                    "      LOOP\n" +
//                    "         j := INSTR (c.column_value, split_chr, i);\n" +
//                    "\n" +
//                    "         IF j = 0\n" +
//                    "         THEN\n" +
//                    "            j := len;\n" +
//                    "            p_split_chr_type :=\n" +
//                    "               split_chr_type (c.row_id,\n" +
//                    "                               to_char(SUBSTR (c.column_value, i)));\n" +
//                    "            PIPE ROW (p_split_chr_type);\n" +
//                    "\n" +
//                    "            IF i >= len\n" +
//                    "            THEN\n" +
//                    "               EXIT;\n" +
//                    "            END IF;\n" +
//                    "         ELSE\n" +
//                    "            p_split_chr_type :=\n" +
//                    "               split_chr_type (\n" +
//                    "                  c.row_id,\n" +
//                    "                  to_char(SUBSTR (c.column_value, i, j - i)));\n" +
//                    "            i := j + len1;\n" +
//                    "            PIPE ROW (p_split_chr_type);\n" +
//                    "         END IF;\n" +
//                    "      END LOOP;\n" +
//                    "   END LOOP;\n" +
//                    "END fn_split_chr_test;\n\'" +
//                    "END ";
//
//            createFuncSql =  createFuncSql.replaceAll("&func_name&",funName)
//                    .replaceAll("&pcolumn&","MERGE_FIELD_20190611171325")
//                    .replaceAll("&tableName&","FXZX_CX_TB_7963F09680C2E7381");
//            statement.execute(createFuncSql);
//            long finishTime2 = System.currentTimeMillis();
//            System.out.println("first finish : "+ (finishTime2-beginTime));
//            String createTableSql ="Create table tb_cache_result parallel 8 \n" +
//                    " as \n" +
//                    "select * from &func_name& ('FXZX_CX_TB_7963F09680C2E7381' ,'-')";
//            createTableSql =  createTableSql.replaceAll("&func_name&",funName);
//            statement.execute(createTableSql);
//            long finishTime3 = System.currentTimeMillis();
//            System.out.println("second finish : "+ (finishTime3-finishTime2));
//
////            preparedStatement.executeBatch();
//        }catch (Exception e){
//            System.out.println(e.toString());
//        }finally {
//            try {
//                statement.execute("Drop table tb_cache_result");
////                statement.execute("Drop function "+funName);
//            }catch (Exception e){
//
//            }
//        }
//
//    }
}
*/
