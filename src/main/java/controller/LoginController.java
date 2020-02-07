package controller;

import dao.IUserDao;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.ibatis.session.SqlSession;
import patterns.memento.Memento;
import patterns.singleton.SingleRegisterStage;
import utils.DialogUtil;
import utils.MybatisUtil;

import java.io.IOException;

public class LoginController {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TextField userid;

    @FXML
    private PasswordField userpwd;

    // 获取SqlSession对象
    private SqlSession sqlSession = MybatisUtil.getSession();
    // 创建userDao接口，然后通过反射来获取代理对象
    private IUserDao userDao = sqlSession.getMapper(IUserDao.class);

    @FXML
    void loginClick() throws IOException {
        String id = userid.getText();
        String pwd = userpwd.getText();
        if (id == null || pwd == null || id.equals("") || pwd.equals("")) {
            DialogUtil.showDialog("ERROR", "输入不能为空！");
        } else {
            int cnt = userDao.selectHasUser(id);
            if (cnt == 0) {
                DialogUtil.showDialog("WARNING", "用户不存在，请先注册！");
            } else {
                Memento user = userDao.findUserById(id);
                String mypwd = user.getUserpwd();
                if (mypwd.equals(pwd)) {
                    // 跳转到主界面
                    FXMLLoader Loader = new FXMLLoader(getClass().getResource("/views/mainPage.fxml"));
                    Parent parent = Loader.load();
                    MainPageController mocl = Loader.getController();
                    mocl.initPersonData(id);
                    Stage stage = new Stage();
                    stage.setTitle("小型仓库管理系统");
                    stage.setScene(new Scene(parent, 900, 700));
                    stage.setResizable(false);
                    stage.show();
                    // 关闭登录窗口，并且关闭会话资源
                    sqlSession.close();
                    ((Stage) rootPane.getScene().getWindow()).close();
                } else {
                    DialogUtil.showDialog("ERROR", "密码错误！");
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
