package com.gms.controller.api.opc_vnpt;

import com.gms.controller.api.*;
import org.apache.commons.codec.digest.DigestUtils;

/**
 *
 * @author vvthanh
 */
public abstract class BaseController extends ApiController {

    private final String secret = "gimasys-vnpt";

    protected boolean checksum(String hashkey, String... contents) {
        String key = "";
        for (String content : contents) {
            key += "/" + content;
        }
        //string: [secret]/[string1]/[string2]
        key = String.format("%s%s", secret, key);
        String checksum = DigestUtils.md5Hex(key).toUpperCase();
        getLogger().info("checksum: {} - key {}", checksum, key); 
        return checksum.equals(hashkey.toUpperCase());
    }
}
