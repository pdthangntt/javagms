package com.gms.entity.constant;

/**
 *
 * @author vvthanh
 */
public class RegexpEnum {

    public final static String SPECIAL_CHAR_WITHOUT_SPACE = "(^[^-](?!.*--)[a-zA-Z0-9\\-]+[^-]$)";
    public final static String DD_MM_YYYY = "^(?=\\d{2}([-.,\\/])\\d{2}\\1\\d{4}$)(?:0[1-9]|1\\d|[2][0-8]|29(?!.02.(?!(?!(?:[02468][1-35-79]|[13579][0-13-57-9])00)\\d{2}(?:[02468][048]|[13579][26])))|30(?!.02)|31(?=.(?:0[13578]|10|12))).(?:0[1-9]|1[012]).\\d{4}$";
    public final static String YYYY = "^\\d{4}$";
    public final static String CODE = "(^[a-zA-Z0-9](?!.*\\-\\-)[a-zA-Z0-9\\-]+[a-zA-Z0-9]$)";
    public final static String CODE_CONFIRM = "(^[a-zA-Z0-9](?!.*\\-\\-)[a-zA-Z0-9\\-\\/]+[a-zA-Z0-9]$)";
    public final static String VN_CHAR = "(^[a-zA-Z0-9\\s\\-\\_\\'\\.]+$)";
    public final static String ADDRESS = "(^[a-zA-Z0-9\\s\\-\\_\\,]+$)";
    public final static String HI_CHAR = "(^[a-zA-Z0-9][a-zA-Z0-9\\-\\_]+[a-zA-Z0-9]$)";
    public final static String CMND = "(^[a-zA-Z0-9\\s\\-\\_]+$)";
    public final static String NUMBER = "^(0*[1-9][0-9]*(\\.[0-9]+)?|0+\\.[0-9]*[1-9][0-9]*)$";
    public final static String NUMBER_ONLY = "^[0-9]+$";
    public final static String SAMPLE_SAVE_CODE = "(^[a-zA-Z0-9](?!.*\\-\\-)[a-zA-Z0-9\\-\\/]+[a-zA-Z0-9]$)";// Mã lưu mẫu
    
}
