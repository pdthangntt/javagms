package com.gms.components;

import com.gms.entity.db.ParameterEntity;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;

/**
 *
 * @author vvthanh
 */
public class ElogUtils {

    final int headIndex = 2;

    final int genderID = 5;
    final int genderLabel = 6;

    final int testResultID = 11;
    final int testResultLabel = 12;

    final int serviceAfterTestID = 14;
    final int serviceAfterTestLabel = 15;

    final int serviceID = 25;
    final int serviceLabel = 26;

    final int jobID = 36;
    final int jobLabel = 37;

    final int raceID = 39;
    final int raceLabel = 40;

    final int modesOfTransmisionID = 43;
    final int modesOfTransmisionLabel = 44;

    final int riskBehaviorID = 47;
    final int riskBehaviorLabel = 48;

    private XSSFSheet optionSheet;

    public ElogUtils(XSSFSheet optionSheet) {
        this.optionSheet = optionSheet;
    }

    private List<ParameterEntity> get(String type, int IDIndex, int labelIndex) {
        List<ParameterEntity> entitys = new ArrayList<>();
        ParameterEntity model;
        for (Row row : optionSheet) {
//            if (type.equals(ParameterEntity.JOD)) {
//                System.out.println(row.getCell(IDIndex));
//                System.out.println(row.getCell(labelIndex));
//            }
            if (row.getRowNum() <= headIndex || row.getCell(IDIndex) == null
                    || row.getCell(IDIndex).toString().equals("")) {
                continue;
            }
            String code = row.getCell(IDIndex).toString();
            if (!ParameterEntity.SERVICE_TEST.equals(type)) {
                double d = Double.parseDouble(code);
                int codePer = (int) d;
                code = String.valueOf(codePer);
            }

            model = new ParameterEntity();
            model.setPosition(row.getRowNum());
            model.setCode(String.valueOf(code));
            model.setType(type);
            model.setElogCode(String.valueOf(code));
            model.setValue(row.getCell(labelIndex).toString());
            entitys.add(model);
        }
        return entitys;
    }

    public List<ParameterEntity> getGenders() {
        return get(ParameterEntity.GENDER, genderID, genderLabel);
    }

    public List<ParameterEntity> getTestResult() {
        return get(ParameterEntity.TEST_RESULTS, testResultID, testResultLabel);
    }

    public List<ParameterEntity> getServiceAfterTest() {
        return get(ParameterEntity.SERVICE_AFTER_TEST, serviceAfterTestID, serviceAfterTestLabel);
    }

    public List<ParameterEntity> getServiceTest() {
        return get(ParameterEntity.SERVICE_TEST, serviceID, serviceLabel);
    }

    public List<ParameterEntity> getJobs() {
        return get(ParameterEntity.JOB, jobID, jobLabel);
    }

    public List<ParameterEntity> getRaces() {
        return get(ParameterEntity.RACE, raceID, raceLabel);
    }

    public List<ParameterEntity> getModesOfTransmision() {
        return get(ParameterEntity.MODE_OF_TRANSMISSION, modesOfTransmisionID, modesOfTransmisionLabel);
    }

    public List<ParameterEntity> getRiskBehaviort() {
        return get(ParameterEntity.RISK_BEHAVIOR, riskBehaviorID, riskBehaviorLabel);
    }

}
