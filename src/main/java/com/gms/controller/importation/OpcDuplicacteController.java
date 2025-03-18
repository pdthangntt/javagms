package com.gms.controller.importation;

import com.gms.components.ExcelUtils;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.db.OpcArvEntity;
import com.gms.entity.db.OpcPatientEntity;
import com.gms.entity.db.OpcStageEntity;
import com.gms.entity.db.OpcTestEntity;
import com.gms.entity.db.OpcViralLoadEntity;
import com.gms.entity.db.OpcVisitEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.form.ImportForm;
import com.gms.entity.form.OpcArvDuplicateForm;
import com.gms.service.OpcArvService;
import com.gms.service.OpcPatientService;
import com.gms.service.OpcStageService;
import com.gms.service.OpcTestService;
import com.gms.service.OpcViralLoadService;
import com.gms.service.OpcVisitService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author pdThang
 */
@Controller(value = "importation_opc_duplicate")
public class OpcDuplicacteController extends BaseController<OpcArvDuplicateForm> {

    private static int firstRow = 2;
    private static String textTitle = "";

    @Autowired
    private OpcArvService opcArvService;
    @Autowired
    private OpcStageService opcStageService;
    @Autowired
    private OpcVisitService opcVisitService;
    @Autowired
    private OpcTestService opcTestService;
    @Autowired
    private OpcViralLoadService opcViralLoadService;
    @Autowired
    private OpcPatientService opcPatientService;

    @Override
    public HashMap<String, HashMap<String, String>> getOptions() {
        LoggedUser currentUser = getCurrentUser();
        Set<String> parameterTypes = new HashSet<>();
        parameterTypes.add(ParameterEntity.GENDER);
        HashMap<String, HashMap<String, String>> options = parameterService.getOptionsByTypes(parameterTypes, getFinalParameter());
        return options;

    }

    @Override
    public ImportForm initForm() {
        final ImportForm form = new ImportForm();
        form.setUploadUrl("/fix-opc-arv/index.html");
        form.setSmallUrl("/fix-opc-arv/index.html");
        form.setReadUrl("/fix-opc-arv/index.html");
        form.setTitle("Sửa bệnh án trùng");
        form.setSmallTitle("Điều trị ngoại trú");
        form.setTemplate(fileTemplate("arv_duplicate.xlsx"));
        return form;
    }

    /**
     * Mapping cols - cell excel
     *
     * @return
     */
    public Map<String, String> cols() {
        Map<String, String> cols = new HashMap<>();
        cols.put("0", "");
        cols.put("1", "code");
        cols.put("2", "child");
        return cols;
    }

    @GetMapping(value = {"/fix-opc-arv/index.html"})
    public String actionUpload(Model model,
            RedirectAttributes redirectAttributes) {
        return renderUpload(model);
    }

    @PostMapping(value = "/fix-opc-arv/index.html")
    public String actionRead(Model model,
            @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) throws Exception {
        HashMap<String, HashMap<String, String>> options = getOptions();
        LoggedUser currentUser = getCurrentUser();
        ImportForm form = initForm();
        int index = 0;
        try {
            form.setData(readFile(ExcelUtils.getWorkbook(file.getInputStream(), file.getOriginalFilename()), 0, model));
            form.setFileName(file.getOriginalFilename());
            form.setFilePath(fileTmp(file));
            HashMap<String, String> errors = new LinkedHashMap<>();
            List<OpcArvDuplicateForm> items = new LinkedList<>();
            for (Map.Entry<Integer, List<String>> row : form.getData().entrySet()) {
                index++;
                if (index < firstRow) {
                    continue;
                }
                try {
                    if (StringUtils.isEmpty(row.getValue().get(1))) {
                        continue;
                    }
                } catch (Exception e) {
                    continue;
                }

                row.getValue();
                OpcArvDuplicateForm item = mapping(cols(), row.getValue());
                item.setRow(String.valueOf(index));
                items.add(item);
            }

            int success = 0;
            List<OpcArvDuplicateForm> lists = new ArrayList<>();

            for (OpcArvDuplicateForm item : items) {
                List<String> childs = new ArrayList<>();
                if (StringUtils.isNotEmpty(item.getChild())) {
                    childs = Arrays.asList(item.getChild().split(",", -1));
                }
                item.setChilds(childs);
                lists.add(item);
            }
            //Xử lý
            OpcArvEntity arvChild;
            for (OpcArvDuplicateForm list : lists) {
                if (StringUtils.isEmpty(list.getCode()) || list.getChild() == null || list.getChild().isEmpty()) {
                    errors.put(list.getRow(), "Bản ghi nhập không có mã bệnh án cha con");
                    continue;
                }
                OpcArvEntity arv = opcArvService.findBySiteAndCode(currentUser.getSite().getID(), list.getCode());
                if (arv == null) {
                    errors.put(list.getRow(), "Bệnh án cha mã: #" + list.getCode() + " không tồn tại trong hệ thống");
                    continue;
                }
                List<String> codeChilds = new ArrayList<>();
                for (String code : list.getChilds()) {
                    arvChild = opcArvService.findBySiteAndCode(currentUser.getSite().getID(), code);
                    if (arvChild == null) {
                        codeChilds.add(code);
                        continue;
                    }
                    //Giai đoạn
                    List<OpcStageEntity> stageModels = opcStageService.findByArvID(arvChild.getID(), false);
                    if (stageModels != null) {
                        for (OpcStageEntity stage : stageModels) {
                            stage.setArvID(arv.getID());
                            stage.setPatientID(arv.getPatientID());
                            opcStageService.saveVNPT(stage);
                        }
                    }
                    //Test
                    List<OpcTestEntity> tests = opcTestService.findByArvID(arvChild.getID(), false);
                    if (tests != null) {
                        for (OpcTestEntity test : tests) {
                            test.setArvID(arv.getID());
                            test.setPatientID(arv.getPatientID());
                            opcTestService.save(test);
                        }
                    }
                    //visit
                    List<OpcVisitEntity> visits = opcVisitService.findByArvID(arvChild.getID(), false);
                    if (visits != null) {
                        for (OpcVisitEntity visit : visits) {
                            visit.setArvID(arv.getID());
                            visit.setPatientID(arv.getPatientID());
                            opcVisitService.saveVNPT(visit);
                        }
                    }
                    //viral
                    List<OpcViralLoadEntity> virals = opcViralLoadService.findByArvID(arvChild.getID(), false);
                    if (virals != null) {
                        for (OpcViralLoadEntity viral : virals) {
                            viral.setArvID(arv.getID());
                            viral.setPatientID(arv.getPatientID());
                            opcViralLoadService.saveVNPT(viral);
                        }
                    }
                    //Xóa dữ liệu cũ
                    opcArvService.remove(arvChild.getID());

                }
                if (!codeChilds.isEmpty()) {
                    errors.put(list.getRow(), "Bệnh án con mã: " + codeChilds.toString() + " không tồn tại trong hệ thống");
                    continue;
                }

                success++;
            }

            model.addAttribute("success", "Đã tiến hành import excel thành công");
            model.addAttribute("errorsw", errors);
            model.addAttribute("total", items.size());
            model.addAttribute("successx", success);
            model.addAttribute("form", form);

            return "importation/opc-arv/opc_duplicate";
        } catch (IOException ex) {
            redirectAttributes.addFlashAttribute("error", "Không đọc được nội dung tập tin excel " + file.getOriginalFilename() + ". Error " + ex.getMessage());
            return redirect(form.getUploadUrl());
        }
    }

    @Override
    public OpcArvDuplicateForm mapping(Map<String, String> cols, List<String> cells) {
        OpcArvDuplicateForm item = new OpcArvDuplicateForm();
        for (int i = 0; i < cells.size(); i++) {
            String col = cols.getOrDefault(String.valueOf(i), null);
            if (col == null) {
                continue;
            }
            try {
                switch (col) {
                    default:
                        item.set(col, cells.get(i));
                }
            } catch (Exception ex) {
//                ex.printStackTrace();
                getLogger().warn(ex.getMessage());
            }
        }

        return item;
    }

}
