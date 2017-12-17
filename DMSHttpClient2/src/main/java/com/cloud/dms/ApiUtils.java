package com.cloud.dms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sun.prism.shader.Mask_TextureRGB_AlphaTest_Loader;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cloud.dms.access.AccessServiceUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
public class ApiUtils
{
    /**
     * List All Queues
     * GET /v1.0/{project_id}/queues
     *
     * @param projectId         Your Project ID
     * @param dmsUrl            The DMS Request URL
     * @param serviceName       Service Name
     * @param region            Region
     * @param ak                Your Access Key ID
     * @param sk                Your Secret Access Key
     * @return ResponseMessage  The response content
     *
     */
    public static ResponseMessage listQueues(String projectId, String dmsUrl, String serviceName, String region,
            String ak, String sk)
    {
        ResponseMessage resMsg;
        String url = dmsUrl + projectId + Constants.SYMBOL_SLASH + Constants.DMS_SERVICE_QUEUES;
        resMsg = AccessServiceUtils.get(serviceName, region, ak, sk, url, projectId);

        if (!resMsg.isSuccess())
        {
            System.out.println("List all queues fail: " + resMsg.getStatusCode());
        }
        return resMsg;
    }

    public static ResponseMessage retrieveQueue(String qId, String projectId, String dmsUrl, String serviceName,
            String region, String ak, String sk)
    {
        ResponseMessage resMsg;

        String url = dmsUrl + projectId + Constants.SYMBOL_SLASH + Constants.DMS_SERVICE_QUEUES
                + Constants.SYMBOL_SLASH + qId;
        resMsg = AccessServiceUtils.get(serviceName, region, ak, sk, url, projectId);
        if (!resMsg.isSuccess())
        {
            System.out.println("Retrieve queue '" + qId + "' fail: " + resMsg.getStatusCode());
        }
        return resMsg;
    }

    /**
     * Send Message
     * POST /v1.0/{project_id}/queues/{queue_id}/messages
     *
     * @param messagesJson           messagesJson  Json String
     * @param qId               The Queue Id
     * @param projectId         Your Project ID
     * @param dmsUrl            The DMS Request URL
     * @param serviceName       Service Name
     * @param region            Region
     * @param ak                Your Access Key ID
     * @param sk                Your Secret Access Key
     * @return ResponseMessage  The response content
     */
    public static ResponseMessage sendMessages(String messagesJson, MsgAttri msgAttri)
    {
        ResponseMessage resMsg = new ResponseMessage();

        String url = msgAttri.getEndpointUrl() + msgAttri.getProjectId() + Constants.SYMBOL_SLASH + Constants.DMS_SERVICE_QUEUES
                + Constants.SYMBOL_SLASH + msgAttri.getQueueId() + Constants.SYMBOL_SLASH + Constants.DMS_SERVICE_MESSAGES;

        if (messagesJson != null && !"".equals(messagesJson))
        {
            resMsg = AccessServiceUtils.post(msgAttri.getServiceName(), msgAttri.getRegion(), msgAttri.getaKey(), msgAttri.getsKey(), url, messagesJson, msgAttri.getProjectId());
            if (!resMsg.isSuccess())
            {
                System.out.println("Send message fail: " + resMsg.getStatusCode());
            }
            else
            {
                System.out.println("Send message success!");
            }
        }
        else
        {
            System.out.println("Miss the required message body.");
        }
        return resMsg;
    }

    /**
     * Consume Message
     * GET /v1.0/{project_id}/queues/{queue_id}/groups/{consumer_group_id}/messages
     *
     * @param qId               The Queue Id
     * @param qGroupId          Consume group Id
     * @param msgLimit          number of consumed message
     * @param projectId         Your Project ID
     * @param dmsUrl            The DMS Request URL
     * @param serviceName       Service Name
     * @param region            Region
     * @param ak                Your Access Key ID
     * @param sk                Your Secret Access Key
     * @return ResponseMessage  The response content
     */
    public static ResponseMessage consumeMessages(MsgAttri msg, String type)
    {
        ResponseMessage resMsg;

        String url = msg.getEndpointUrl() + msg.getProjectId() + Constants.SYMBOL_SLASH + Constants.DMS_SERVICE_QUEUES
                + Constants.SYMBOL_SLASH + msg.getQueueId() + Constants.SYMBOL_SLASH + Constants.DMS_SERVICE_GROUPS
                + Constants.SYMBOL_SLASH + msg.getGroupId() + Constants.SYMBOL_SLASH + Constants.DMS_SERVICE_MESSAGES
                + Constants.SYMBOL_QUES_MARK + Constants.DMS_SERVICE_LIMIT + Constants.SYMBOL_EQUAL + msg.getMsgLimit()
                + Constants.SYMBOL_AMPERSAND + Constants.SYMBOL_TAG + Constants.SYMBOL_EQUAL + type;
        resMsg = AccessServiceUtils.get(msg.getServiceName(), msg.getRegion(), msg.getaKey(), msg.getsKey(), url, msg.getProjectId());
        if (!resMsg.isSuccess())
        {
            System.out.println("Consume message fail: " + resMsg.getStatusCode());
        }
        return resMsg;
    }

    public static ResponseMessage acknowledgeMessages(ArrayList<String> msgIds, MsgAttri msg)
    {
        ResponseMessage resMsg = new ResponseMessage();
        String url = msg.getEndpointUrl() + msg.getProjectId() + Constants.SYMBOL_SLASH + Constants.DMS_SERVICE_QUEUES
                + Constants.SYMBOL_SLASH + msg.getQueueId() + Constants.SYMBOL_SLASH + Constants.DMS_SERVICE_GROUPS
                + Constants.SYMBOL_SLASH + msg.getGroupId() + Constants.SYMBOL_SLASH + Constants.DMS_SERVICE_ACK;
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode messagesArray = mapper.createArrayNode();
        for (String msgId : msgIds)
        {
            ObjectNode msgNode = mapper.createObjectNode();
            msgNode.put(Constants.DMS_SERVICE_MESSAGE_ID, msgId);
            msgNode.put("status", "success");
            messagesArray.add(msgNode);
        }

        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.putPOJO(Constants.DMS_SERVICE_MESSAGE, messagesArray);

        String bodyJson = null;
        try
        {
            bodyJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectNode);
        }
        catch (JsonProcessingException e)
        {
            e.printStackTrace();
        }
        if (bodyJson != null)
        {
            resMsg = AccessServiceUtils.post(msg.getServiceName(), msg.getRegion(), msg.getaKey(), msg.getsKey(), url, bodyJson, msg.getProjectId());
            if (!resMsg.isSuccess())
            {
                System.out.println("Acknowledge Message fail: " + resMsg.getStatusCode());
            }
        }
        else
        {
            System.out.println("Miss the required message body.");
        }
        return resMsg;
    }

    public static String constructTempMessages()
    {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode topObjNode = mapper.createObjectNode();
        ArrayNode msgArray = mapper.createArrayNode(); 

        for (int i = 0; i < 3; i++)
        {
        	contbMsg(mapper, msgArray, "order");
        }
        for (int i = 0; i < 3; i++)
        {
        	contbMsg(mapper, msgArray, "pay");
        }
        for (int i = 0; i < 3; i++)
        {
        	contbMsg(mapper, msgArray, "logistics");
        }
        topObjNode.put(Constants.DMS_SERVICE_MESSAGES, msgArray);
        String bodyJson = "";
        try
        {
            bodyJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(topObjNode);
        }
        catch (JsonProcessingException e)
        {
            e.printStackTrace();
        }
        return bodyJson;
    }
    
    private static void contbMsg(ObjectMapper mapper, ArrayNode msgArray, String tag)
    {
    	//attribute Node
        ObjectNode attributesNode = mapper.createObjectNode();
        attributesNode.put(Constants.KEY_NAME, "ignore");
        attributesNode.put(Constants.DMS_SERVICE_TYPE, "string.start");
        attributesNode.put(Constants.DMS_SERVICE_VALUE, "test");

        //tag Node
        ArrayNode tagNode = mapper.createArrayNode();
        tagNode.add(tag);
        
        //message node
        ObjectNode msgObjectNode = mapper.createObjectNode();
        msgObjectNode.put(Constants.DMS_SERVICE_BODY, "msg_body_" + System.currentTimeMillis());
        msgObjectNode.putPOJO(Constants.DMS_SERVICE_ATTRIBUTES, attributesNode);
        msgObjectNode.put("tags", tagNode);
        msgArray.add(msgObjectNode);
    }

    public static ArrayList<String> parseHandlerIds(ResponseMessage consumeMessagesResMsg)
    {
        ArrayList<String> handlerIds = new ArrayList<String>();
        if (consumeMessagesResMsg.isSuccess())
        {
            if (consumeMessagesResMsg.getBody() != null)
            {
                try
                {
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode jsonArrayNode = mapper.readTree(consumeMessagesResMsg.getBody());
                    if (jsonArrayNode.isArray())
                    {
                        for (final JsonNode objNode : jsonArrayNode)
                        {
                            String handlerId = objNode.findValue("handler").textValue().toString();
                            if (!handlerIds.contains(handlerId))
                            {
                                handlerIds.add(handlerId);
                            }
                        }
                        if (handlerIds.size() <= 0)
                        {
                            System.out.println(
                                    "Warning!! Can not get handler ids from response body, "
                                            + "the ack message will be ignored");
                        }
                    }
                }
                catch (JsonProcessingException e)
                {
                    System.out.println("Catch JsonProcessingException when create group fail!");
                    e.printStackTrace();
                }
                catch (IOException e)
                {
                    System.out.println("Catch IOException when create group fail!");
                    e.printStackTrace();
                }

            }
        }
        return handlerIds;
    }
    
    public static List<String> decodeMsg (ResponseMessage consumeMessagesResMsg)
    {
    	List<String> retList = new ArrayList<>();
    	String body = consumeMessagesResMsg.getBody();
    	JSONArray ja = JSONArray.parseArray(body);
    	for (Object object: ja)
    	{
    		JSONObject jo = (JSONObject)object;
    		retList.add(jo.getString("message"));
    	}
    	return retList;
    }

}
