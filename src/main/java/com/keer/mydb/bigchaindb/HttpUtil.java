package com.keer.mydb.bigchaindb;

import com.bigchaindb.model.GenericCallback;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class HttpUtil {
    private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);

//    public static GenericCallback handleServerResponse() {
//        //define callback methods to verify response from BigchainDBServer
//        GenericCallback callback = new GenericCallback() {
//
//            @Override
//            public void transactionMalformed(Response response) {
//                logger.info("malformed - " + response.message());
//                onFailure();
//            }
//
//            @Override
//            public void pushedSuccessfully(Response response) {
//                try {
//                    logger.info("pushedSuccessfully - " + response.body().string());
//                    onSuccess(response);
//                } catch (IOException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void otherError(Response response) {
//                logger.info(response.body().toString());
//                logger.info("otherError" + response.message());
//                onFailure();
//            }
//        };
//
//        return callback;
//    }

    private static void onSuccess(Response response) throws IOException {
        //TODO : Add your logic here with response from server
        logger.info("Success : 操作成功");
    }

    private static void onFailure() {
        //TODO : Add your logic here
        logger.info("Transaction failed");
    }


    /**
     * 发起get请求
     *
     * @param url
     * @return
     */
    public static String httpGet(String url) {
        String result = null;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            result = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 发送httppost请求
     *
     * @param url
     * @param data  提交的参数为key=value&key1=value1的形式
     * @return
     */
    public static String httpPost(String url, String data) {
        String result = null;
        OkHttpClient httpClient = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/html;charset=utf-8"), data);
        Request request = new Request.Builder().url(url).post(requestBody).build();
        try {
            Response response = httpClient.newCall(request).execute();
            result = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }



}
