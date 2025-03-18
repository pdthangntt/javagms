package com.gms.controller.api.qlnn;

import com.gms.controller.api.ApiController;
import org.apache.commons.codec.digest.DigestUtils;

/**
 *
 * @author vvthanh
 */
public abstract class BaseController extends ApiController {

    protected String secret = "gimasys-qlnn";

    protected boolean checksum(String hashkey, String... contents) {
        String key = "";
        for (String content : contents) {
            key += "/" + content;
        }
        //string: [secret]/[string1]/[string2]
        key = String.format("%s%s", secret, key);
        String checksum = DigestUtils.md5Hex(key).toUpperCase();
        getLogger().info("hashkey: {} - checksum: {} - key {}", hashkey, checksum, key);
        return checksum.equals(hashkey.toUpperCase());
    }
}
