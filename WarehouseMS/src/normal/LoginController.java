package normal;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.springframework.jdbc.core.JdbcTemplate;
import singleton.SingleRegisterStage;
import utils.JDBCUtils;

import java.io.IOException;
import java.util.Map;

public class LoginController {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TextField userid;

    @FXML
    private PasswordField userpwd;

    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    @FXML
    void loginClick() throws IOException {
        String id = userid.getText();
        String pwd = userpwd.getText();
        if(id == null || pwd == null || id.equals("") || pwd.equals("")) {
            Dialog.showDialog("ERROR", "输入不能为空！");
        } else {
            // 查询数据库
            String sql = "SELECT COUNT(*) FROM user WHERE userid = ?";
            Long cnt = template.queryForObject(sql, Long.class, id);
            if(cnt == 0) {
                Dialog.showDialog("WARNING", "用户不存在，请先注册！");
            } else {
                sql = "SELECT userpwd FROM user WHERE userid = ?";
                Map<String, Object> rs = template.queryForMap(sql, id);
                String mypwd = String.valueOf(rs.get("userpwd"));
                if(mypwd.equals(pwd)) {
                    // 跳转到主界面
                    FXMLLoader Loader = new FXMLLoader(getClass().getResource("mainPage.fxml"));
                    Parent parent = Loader.load();
                    MainPageController mocl = Loader.getController();
                    mocl.initPersonData(id);
                    Stage stage = new Stage();
                    stage.setTitle("小型仓库管理系统");
                    stage.setScene(new Scene(parent, 900, 700));
                    stage.setResizable(false);
                    stage.show();
                    // 关闭登录窗口
                    ((Stage) rootPane.getScene().getWindow()).close();
                } else {
                    Dialog.showDialog("ERROR", "密码错误！");
                }
            }
        }
    }

    @FXML
    void registerClick() throws IOException {
        // 单例模式生成一个注册界面
        SingleRegisterStage.getInstance().show();
    }
}
