package com.gms.controller.importation;

import com.gms.components.UrlUtils;
import com.gms.entity.bean.Response;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.form.ImportForm;
import com.gms.entity.form.ParameterForm;
import com.gms.service.ParameterService;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller(value = "importation_parameter")
public class ParameterController extends BaseController<ParameterEntity> {

    @Autowired
    private ParameterService parameterService;

    @Override
    public HashMap<String, HashMap<String, String>> getOptions() {
        HashMap<String, HashMap<String, String>> options = new HashMap<>();
        //type
        options.put("type", new HashMap<String, String>());
        options.get("type").put("", "--");
        List<ParameterEntity> models = parameterService.findByType(ParameterEntity.SYSTEMS_PARAMETER);
        for (ParameterEntity parameter : models) {
            String[] splitCode = parameter.getCode().split("_");
            if (splitCode.length < 2 || !"title".equals(splitCode[1])) {
                continue;
            }
            options.get("type").put(splitCode[0], parameter.getValue());
        }
        //Trạng thái
        options.put("status", new HashMap<String, String>());
        options.get("status").put("", "--");
        options.get("status").put("0", "Tạm khoá");
        options.get("status").put("1", "Hoạt động");
        return options;
    }

    @Override
    public ImportForm initForm() {
        final ImportForm form = new ImportForm();
        form.setUploadUrl("/import-parameter/index.html");
        form.setReadUrl("/import-parameter/read.html");
        form.setValidateUrl("/import-parameter/validate.html");
        form.setSaveUrl("/import-parameter/save.json");
        form.setTitle("Thêm tham số sử dụng excel");
        form.setSmallTitle("Tham số");
        form.setSmallUrl(UrlUtils.parameterIndex());
        form.setTemplate(fileTemplate("parameter.xlsx"));
        form.setCols((new ParameterEntity()).getAttributeLabels());
        return form;
    }

    @GetMapping(value = {"/import-parameter/index.html", "/import-parameter/read.html", "/import-parameter/validate.html"})
    public String actionUpload(Model model,
            RedirectAttributes redirectAttributes) {
        return renderUpload(model);
    }

    @PostMapping(value = "/import-parameter/read.html")
    public String actionRead(Model model,
            @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) {
        ImportForm form = initForm();
        try {
            return renderRead(model, form, file);
        } catch (IOException ex) {
            redirectAttributes.addFlashAttribute("error", "Không đọc được nội dung tập tin excel " + file.getOriginalFilename() + ". Error " + ex.getMessage());
            return redirect(form.getUploadUrl());
        }
    }

    @PostMapping(value = "/import-parameter/validate.html")
    public String actionValidate(Model model,
            @ModelAttribute("form") ImportForm input,
            RedirectAttributes redirectAttributes) {
        ImportForm form = initForm();
        form.setMapCols(input.getMapCols());
        form.setFilePath(input.getFilePath());
        try {
            return renderValidate(model, "importation/parameter/view", form);
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return redirect(form.getUploadUrl());
        }
    }

    @PostMapping(value = "/import-parameter/save.json", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Response<?> actionSave(@Valid @RequestBody ParameterForm form,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new Response<>(false, bindingResult.getAllErrors());
        }
        ParameterEntity parameterEntity = parameterService.findOne(form.getType(), form.getCode());
        if (parameterEntity == null) {
            parameterEntity = new ParameterEntity();
            parameterEntity.setType(form.getType());
            parameterEntity.setCode(form.getCode());
        }
        parameterEntity.setValue(StringUtils.isEmpty(form.getValue()) ? parameterEntity.getValue() : form.getValue());
        parameterEntity.setElogCode(StringUtils.isEmpty(form.getElogCode()) ? parameterEntity.getElogCode() : form.getElogCode());
        parameterEntity.setHivInfoCode(StringUtils.isEmpty(form.getHivInfoCode()) ? parameterEntity.getHivInfoCode() : form.getHivInfoCode());
        parameterEntity.setNote(StringUtils.isEmpty(form.getNote()) ? parameterEntity.getNote() : form.getNote());
        ParameterEntity entity = parameterService.save(parameterEntity);
        if (entity == null) {
            return new Response<>(false, "Không cập nhật được thông tin! Vui lòng thử lại sau.");
        }
        return new Response<>(true, entity);
    }

    @Override
    public ParameterEntity mapping(Map<String, String> cols, List<String> cells) {
        HashMap<String, String> typeOptions = getOptions().get("type");
        ParameterEntity item = new ParameterEntity();
        for (int i = 0; i < cells.size(); i++) {
            String col = cols.getOrDefault(String.valueOf(i), null);
            if (col == null) {
                continue;
            }
            try {
                switch (col) {
                    case "status":
                        item.setStatus(new Integer(cells.get(i).replaceFirst("\\.0+$", "")));
                        break;
                    case "code":
                        item.setCode(cells.get(i).replaceFirst("\\.0+$", ""));
                        break;
                    case "elogCode":
                        item.setElogCode(cells.get(i).replaceFirst("\\.0+$", ""));
                        break;
                    case "type":
                    default:
                        item.set(col, cells.get(i));
                }
            } catch (Exception ex) {
                getLogger().warn(ex.getMessage());
            }
        }
        return item;
    }

}
