package com.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertyManager {

    private static PropertyManager propertyManager;

    private Properties properties = new Properties();

    private final String COLUMN_MAP_FILE= "/columnMap.xml";

    private final String CONFIG_FILE= "config.properties";

    private final String DB_SQL_FILE= "/dbsql.xml";

    private static final Logger logger = LoggerFactory.getLogger(PropertyManager.class);

    public static PropertyManager getInstance(){
        if (null != propertyManager) {
            return propertyManager;
        } else {
            synchronized (PropertyManager.class) {
                if (null == propertyManager) {
                    propertyManager = new PropertyManager();
                    return propertyManager;
                } else {
                    return propertyManager;
                }
            }
        }
    }

    private PropertyManager(){
        /*  从内部读取config配置文件
        try {
            InputStream in =  Object.class.getResourceAsStream(CONFIG_FILE);
            properties.load(in);
            loadXml();
        }catch (Exception e){
            logger.error(e.toString());
        }*/

        try (FileInputStream in = new FileInputStream(CONFIG_FILE)) {
            properties.load(in);
        } catch (IOException e) {
            logger.error(e.toString());
        }

    }

    public String getProperty(String property){
        return properties.getProperty(property);
    }




    public void loadXml() throws Exception {
        loadDbSql();
        loadColumnMap();
    }

    private void loadColumnMap() throws Exception {
        InputStream xml = null;
        try {
            xml = PropertyManager.class.getResourceAsStream(COLUMN_MAP_FILE);
            Element root = getDocument(xml).getDocumentElement();
            NodeList list = root.getElementsByTagName("src");
            for (int i = 0, n = list.getLength(); i < n; i++) {
                Element schemaElement = (Element) list.item(i);
                //读取各个属性
                String srcDbType = getAttributeCheckExist(schemaElement,"dbType");
                NodeList destlist = schemaElement.getElementsByTagName("dest");
                for (int j = 0; j < destlist.getLength(); j ++){
                    Element destDb = (Element)destlist.item(j);
                    String destDbType = getAttributeCheckExist(destDb,"dbType");
                    String dbKey = srcDbType + "-" + destDbType;
                    NodeList columnTypeList = destDb.getElementsByTagName("columnType");
                    for (int z = 0;z < columnTypeList.getLength();z++){
                        Element columnType = (Element)columnTypeList.item(z);
                        String destType = getAttributeCheckExist(columnType,"destType");
                        String srcType = getAttributeCheckExist(columnType,"srcType");
                    }
                }
            }
            logger.info("load "+COLUMN_MAP_FILE+" finish!");
        } catch (Exception e) {
            throw e;
        } finally {
            if (xml != null) {
                try {
                    xml.close();
                } catch (IOException e) {
                }
            }
        }
    }

    private void loadDbSql() throws Exception {
        InputStream xml = null;
        try {
            xml = PropertyManager.class.getResourceAsStream(DB_SQL_FILE);
            Element root = getDocument(xml).getDocumentElement();
            NodeList list = root.getElementsByTagName("db");
            for (int i = 0, n = list.getLength(); i < n; i++) {
                Element schemaElement = (Element) list.item(i);
                //读取各个属性
                String type = getAttributeCheckExist(schemaElement,"type");

            }
            logger.info("load "+DB_SQL_FILE+" finish!");
        } catch (Exception e) {
            throw e;
        } finally {
            if (xml != null) {
                try {
                    xml.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public String getTagElementString(Element element,String tagName) throws Exception {
        NodeList nodeList = element.getElementsByTagName(tagName);
        if (nodeList == null || nodeList.getLength() < 1){
            throw new Exception("tagName " +tagName +" not exist!");
        }
        return element.getElementsByTagName(tagName).item(0).getTextContent();
    }


    public Document getDocument(InputStream xml) throws ParserConfigurationException,
            SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true);
        factory.setNamespaceAware(false);
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(xml);
    }


    public String getAttributeCheckExist(Element element,String attribute) throws Exception {
        try{
            String tmp  =  element.getAttribute(attribute);
            if (tmp == null || tmp.equalsIgnoreCase("")){
                throw new Exception("attribute " +attribute +" not exist!");
            }
            return tmp;
        }catch (Exception e){
            logger.error("attribute "+attribute+" is not exist or null! plz check the xml file");
            throw  e;
        }

    }

    public String getPropertyCheckExist(Properties properties,String attribute) throws Exception {
        try{
            String tmp  =  properties.getProperty(attribute);
            if (tmp == null || tmp.equalsIgnoreCase("")){
                throw new Exception("property " +attribute +" not exist!");
            }
            return tmp;
        }catch (Exception e){
            logger.error("property "+attribute+" is not exist or null! plz check the xml file");
            throw  e;
        }

    }


}
