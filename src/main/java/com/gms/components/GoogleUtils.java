package com.gms.components;

import com.google.gson.Gson;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import java.io.IOException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author vvthanh
 */
@Component
public class GoogleUtils {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(GoogleUtils.class);

//    @Value("${app.google.key}")
    private String googleKey = "AIzaSyAuuQryf_h6-MNWoyvUpIMp8fLMLHMSsH0";

    public static void main(String[] args) throws ApiException, InterruptedException, IOException {
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("AIzaSyAuuQryf_h6-MNWoyvUpIMp8fLMLHMSsH0").build();
        GeocodingResult[] results = GeocodingApi.geocode(context, "33/7/ Long Hải, Xã Trường Tây, Huyện Hòa Thành, Tỉnh Tây Ninh").await();
        Gson gson = new Gson();
        System.out.println(gson.toJson(results[0]));
    }

    /**
     * Get geo from address string
     *
     * @auth vvthanh
     * @param addresss
     * @return
     */
    public GeocodingResult geocode(String addresss) {
        GeoApiContext context = new GeoApiContext.Builder().apiKey(googleKey).build();
        try {
            GeocodingResult[] results = GeocodingApi.geocode(context, addresss).await();
            return results[0];
        } catch (Exception ex) {
            logger.warn(ex.getMessage());
            return null;
        }
    }
}
