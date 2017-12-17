package com.cloud.dms;

import static com.cloud.dms.ApiUtils.listQueues;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;
import com.cloud.dms.access.AccessServiceUtils;

/**
 * 
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  jWX421652
 * @version  [版本号, 2017年3月11日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class DMSHttpClient
{
    private static String endpointUrl = "";

    private static String region = "";

    private static String serviceName = "dms";

    private static String aKey = "";

    private static String sKey = "";

    private static String projectId = "546e52331ea74cd49722fda4fb23bf55";

    private static String queueId = "11e3e154-ac52-4b2a-a892-76ed1c087940";

    private static String queueGroupId = "g-3ddaacc1-2156-48d8-bcd3-80f658a21fcc";

    /*
     * Read Configure File And Initialize Variables
     */
    static
    {
        URL configPath = ClassLoader.getSystemResource("dms-service-config.properties");
        Properties prop = AccessServiceUtils.getPropsFromFile(configPath.getFile());
        region = prop.getProperty(Constants.DMS_SERVICE_REGION);
        aKey = prop.getProperty(Constants.DMS_SERVICE_AK);
        sKey = prop.getProperty(Constants.DMS_SERVICE_SK);
        endpointUrl = prop.getProperty(Constants.DMS_SERVICE_ENDPOINT_URL);
        if (endpointUrl.endsWith("/"))
        {
            endpointUrl = endpointUrl + "v1.0/";
        }
        else
        {
            endpointUrl = endpointUrl + "/v1.0/";
        }
        projectId = prop.getProperty(Constants.DMS_SERVICE_PROJECT_ID);
    }
    
    public static void main(String[] args)
    {
    	runAllApiMethods();
    }
    
    public static void runAllApiMethods()
    {
        listQueues(projectId, endpointUrl, serviceName, region, aKey, sKey);
        MsgAttri msg = new MsgAttri();
        msg.setaKey(aKey);
        msg.setEndpointUrl(endpointUrl);
        msg.setProjectId(projectId);
        msg.setQueueId(queueId);
        msg.setsKey(sKey);
        msg.setRegion(region);
        msg.setServiceName(serviceName);
        msg.setMsgLimit("10");
        msg.setGroupId(queueGroupId);
        MsgProducer msgProducer = new MsgProducer(msg);
        MsgConsumer pay = new MsgConsumer(msg, "pay");
        MsgConsumer order = new MsgConsumer(msg, "order");
        MsgConsumer logistics = new MsgConsumer(msg, "logisitics");
        Thread producer = new Thread(msgProducer);
        Thread payThread = new Thread(pay);
        Thread orderThread = new Thread(order);
        Thread logisticsThread = new Thread(logistics);
        producer.setName("producer");
        payThread.setName("pay");
        orderThread.setName("order");
        logisticsThread.setName("logistics");
        producer.start();
        payThread.start();
        orderThread.start();
        logisticsThread.start();
    }
}
