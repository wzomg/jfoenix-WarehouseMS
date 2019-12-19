package singleton;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import normal.Dialog;
import org.springframework.jdbc.core.JdbcTemplate;
import utils.JDBCUtils;

public class RegisterController {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TextField userid;

    @FXML
    private TextField userpwd;

    @FXML
    private TextField userphone;

    @FXML
    private TextField useraddress;

    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    @FXML
    void clearClick() {
        userid.setText("");
        userpwd.setText("");
        userphone.setText("");
        useraddress.setText("");
    }
    private boolean check(String str) {
        return str == null || str.equals("");
    }

    @FXML
    void registerClick() {
        String id = userid.getText();
        String pwd = userpwd.getText();
        String phoneNum = userphone.getText();
        String address = useraddress.getText();
        if(check(id) || check(pwd) || check(phoneNum) || check(address)) {
            Dialog.showDialog("ERROR", "输入不能为空！");
        } else {
            String sql = "SELECT COUNT(*) FROM user WHERE userid = ?";
            Long cnt = template.queryForObject(sql, Long.class, id);
            if(cnt > 0) {
                // 提示用户名已注册
                Dialog.showDialog("WARNING", "用户名已存在，请重新输入另一个用户名！");
            } else {
                sql = "INSERT INTO user VALUES (?, ?, ?, ?)";
                int num = template.update(sql, id, pwd, phoneNum, address);
                if(num > 0) {
                    Dialog.showDialog("INFORMATION", "注册成功！");
                    // 并且销毁当前stage
                    ((Stage) rootPane.getScene().getWindow()).close();
                } else {
                    Dialog.showDialog("ERROR", "注册失败！");
                }
            }
        }
    }
}