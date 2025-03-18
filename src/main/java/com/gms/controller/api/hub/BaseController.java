package com.gms.controller.api.hub;

import com.gms.controller.api.ApiController;
import com.gms.entity.db.PqmApiTokenEntity;
import com.gms.entity.db.PqmLogApiEntity;
import com.gms.repository.PqmLogApiRepository;
import com.gms.service.PqmApiTokenService;
import java.util.Date;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author vvthanh
 */
public abstract class BaseController extends ApiController {

    protected String secret = "gimasys-hub";

    @Autowired
    private PqmApiTokenService tokenService;
    @Autowired
    private PqmLogApiRepository apiRepository;

    protected PqmLogApiEntity log(
            String type,
            String requestMethod,
            Long siteID,
            String checksum,
            String requestBody,
            boolean status,
            String message,
            String exr
    ) {
        PqmLogApiEntity model = new PqmLogApiEntity();
        model.setTime(new Date());

        model.setType(type);
        model.setRequestMethod(requestMethod);
        model.setSiteID(siteID);
        model.setChecksum(checksum);
        model.setPublicKey("None");
//        model.setRequestBody(requestBody);
        model.setStatus(status);
        model.setMessage(message);
//        model.setExr(exr);
        model.setType(type);

        return apiRepository.save(model);
    }

//    protected boolean checksum(String hashkey, String publicKey, String... contents) {
//        try {
//
//            String key = "";
//            for (String content : contents) {
//                key += "/" + content;
//            }
//            PqmApiTokenEntity token = tokenService.findByKeyToken(publicKey);
//            if (token == null) {
//                throw new Exception("Public key chưa được đăng ký");
//            }
//            //string: [secret]/[string1]/[string2]
//            key = String.format("%s%s", token.getSecret(), key);
//            String checksum = DigestUtils.md5Hex(key).toUpperCase();
//            getLogger().info("hashkey: {} - checksum: {} - key {}", hashkey, checksum, key);
//            return checksum.equals(hashkey.toUpperCase());
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
    protected boolean checksum(String secret) {
        try {
            secret = secret.toUpperCase().trim();
            String checksum = DigestUtils.md5Hex(this.secret).toUpperCase();
            getLogger().info("secret: {} - checksum: {} - result {}", secret, checksum, checksum.equals(secret.toUpperCase()));
            return checksum.equals(secret);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
