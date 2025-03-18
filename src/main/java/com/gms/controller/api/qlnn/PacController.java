package com.gms.controller.api.qlnn;

import com.gms.entity.bean.Response;
import com.gms.entity.db.PacPatientInfoEntity;
import com.gms.service.PacPatientService;
import java.util.Base64;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "api_pac_qlnn")
public class PacController extends BaseController {

    @Autowired
    private PacPatientService patientService;

    @RequestMapping(value = "/v1/pac-get.api", method = RequestMethod.GET)
    public Response<?> actionSave(@RequestHeader("checksum") String checksum,
            @RequestParam(name = "confirm_code") String confirmCode,
            @RequestParam(name = "card_id") String cardId,
            @RequestParam(name = "province") String province) {
        try {
            if (!checksum(checksum, confirmCode, cardId)) {
                return new Response<>(false, "INCORRECT_CHECKSUM", "Mã xác thực không chính xác");
            }

            List<PacPatientInfoEntity> items = patientService.findByConfirms(confirmCode, province);
            if (items == null || items.isEmpty()) {
                throw new Exception("Không tìm thấy data");
            }
            PacPatientInfoEntity item = items.get(0);
            String body = Base64.getEncoder().encodeToString(gson.toJson(item).getBytes());
            return new Response<>(true, "Dữ liệu QLNN", body);
        } catch (Exception e) {
            return new Response<>(false, "EXCEPTION", e.getMessage());
        }
    }

}
