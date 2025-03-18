package com.gms.components.onesignal;

import com.google.gson.Gson;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 *
 * @author vvthanh
 */
@Service
public class Onesignal {

    private static final Logger logger = LoggerFactory.getLogger(Onesignal.class);

    @Value("${app.onesignal.id}")
    private String appID;
    @Value("${app.onesignal.key}")
    private String appKey;
    @Value("${app.baseUrl}")
    private String baseUrl;

    @Autowired
    private Gson gson;

    public Onesignal() {
    }

    public Onesignal(String appID, String appKey) {
        this.appID = appID;
        this.appKey = appKey;
    }

    public void send(String title, String content, List<String> playerIds, String web_url) {
        String jsonResponse;
        try {
            URL url = new URL("https://onesignal.com/api/v1/notifications");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setUseCaches(false);
            con.setDoOutput(true);
            con.setDoInput(true);

            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setRequestProperty("Authorization", "Basic " + appKey);
            con.setRequestMethod("POST");

            Map<String, Object> request = new HashMap<>();
            request.put("app_id", appID);
            request.put("url", baseUrl + web_url);
            request.put("include_player_ids", playerIds);

            Map<String, String> headings = new HashMap<>();
            headings.put("vi", title);
            headings.put("en", title);
            request.put("headings", headings);

            Map<String, String> message = new HashMap<>();
            message.put("vi", content);
            message.put("en", content);
            request.put("contents", message);

            String strJsonBody = gson.toJson(request);
            logger.debug("json body {}", strJsonBody);

            byte[] sendBytes = strJsonBody.getBytes("UTF-8");
            con.setFixedLengthStreamingMode(sendBytes.length);
            OutputStream outputStream = con.getOutputStream();
            outputStream.write(sendBytes);

            int httpResponse = con.getResponseCode();
            if (httpResponse >= HttpURLConnection.HTTP_OK
                    && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                scanner.close();
            } else {
                Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                scanner.close();
            }
            logger.debug("jsonResponse {}", jsonResponse);
        } catch (Exception e) {
            logger.error("jsonResponse {}", e.getMessage());
        }
    }
}
