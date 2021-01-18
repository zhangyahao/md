package util.response;

import com.ccdt.ott.util.ConstValue;
import org.springframework.http.ResponseEntity;

import java.util.Map;

/**
 * @program: markdown
 * @description:
 * @author: Zhang
 * @create: 2021-01-18 10:28
 **/
public class ResultInfo {

    public static ResponseEntity<JsonMessage> dealWithResult(int result) {
        JsonMessage mes;
        if (result== ConstValue.HTTP_STATE_CODE_200) {
            mes = new JsonMessage(ConstValue.HTTP_STATE_CODE_200, "suc");
            return ResponseEntity.ok(mes);
        } else if (result==ConstValue.HTTP_STATE_CODE_205) {
            mes = new JsonMessage(ConstValue.HTTP_STATE_CODE_205, ConstValue.ERROR_205);
            return ResponseEntity.ok(mes);
        }else if (result==ConstValue.HTTP_STATE_CODE_400) {
            mes = new JsonMessage(ConstValue.HTTP_STATE_CODE_400, ConstValue.ERROR_400);
            return ResponseEntity.ok(mes);
        }
        else {
            mes = new JsonMessage(ConstValue.HTTP_STATE_CODE_500, ConstValue.ERROR_500);
            return ResponseEntity.ok(mes);
        }
    }

    public static ResponseEntity<JsonMessage> dealWithResult(Map<String, Object> info) {
        JsonMessage mes = new JsonMessage(ConstValue.HTTP_STATE_CODE_200,info);
        return ResponseEntity.ok(mes);
    }


}
