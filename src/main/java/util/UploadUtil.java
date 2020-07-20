package util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;

public class UploadUtil {
    private static final Logger log = LoggerFactory.getLogger(UploadUtil.class);

    public static String postFile(String url, Map<String, Object> param, File file) throws ClientProtocolException,
            IOException {
        String res = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(url);
        httppost.setEntity(getMutipartEntry(param, file));
        CloseableHttpResponse response = httpClient.execute(httppost);
        HttpEntity entity = response.getEntity();
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            res = EntityUtils.toString(entity, "UTF-8");
            response.close();
        } else {
            res = EntityUtils.toString(entity, "UTF-8");
            response.close();
            throw new IllegalArgumentException(res);
        }
        return res;
    }

    private static MultipartEntity getMutipartEntry(Map<String, Object> param, File file) throws UnsupportedEncodingException {
        if (file == null) {
            throw new IllegalArgumentException("文件不能为空");
        }
        FileBody fileBody = new FileBody(file);
        FormBodyPart filePart = new FormBodyPart("file", fileBody);
        MultipartEntity multipartEntity = new MultipartEntity();
        multipartEntity.addPart(filePart);
        for (Map.Entry<String, Object> entry : param.entrySet()) {
            FormBodyPart field = new FormBodyPart(entry.getKey(), (ContentBody) entry.getValue());
            multipartEntity.addPart(field);
        }
        return multipartEntity;
    }

}