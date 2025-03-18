package com.gms.controller.report;

import com.gms.components.ExcelUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Base64;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author DSNAnh
 */
@Controller(value = "index_report")
public class IndexController extends BaseController {

    @GetMapping(value = {"/index/dowload-excel.html"})
    public ResponseEntity<InputStreamResource> actionVisitBookExcel(Model model,
            @RequestParam(name = "path") String path) throws Exception {
        String pathFile = new String(Base64.getDecoder().decode(path));
        
        File file = new File(pathFile);
        FileInputStream fis = new FileInputStream(file);
        DataInputStream inputStream = new DataInputStream(fis);

        String fileName = file.getName();

        Workbook workbook = ExcelUtils.getWorkbook(inputStream, file.getName());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", String.format("attachment; filename=%s", fileName));
        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new InputStreamResource(in));
    }
}
