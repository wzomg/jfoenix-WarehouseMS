package controller;

import dao.IUserDao;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.ibatis.session.SqlSession;
import patterns.memento.Memento;
import utils.DialogUtil;
import utils.MybatisUtil;


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

    // 获取SqlSession对象
    private SqlSession sqlSession = MybatisUtil.getSession();
    // 创建userDao接口，然后通过反射来获取代理对象
    private IUserDao userDao = sqlSession.getMapper(IUserDao.class);

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
        if (check(id) || check(pwd) || check(phoneNum) || check(address)) {
            DialogUtil.showDialog("ERROR", "输入不能为空！");
        } else {
            int cnt = userDao.selectHasUser(id);
            if (cnt > 0) {
                // 提示用户名已注册
                DialogUtil.showDialog("WARNING", "用户名已存在，请重新输入另一个用户名！");
            } else {
                // 注册用户，添加一名用户
                Memento user = new Memento(id, pwd, phoneNum, address);
                cnt = userDao.addUser(user);
                //注意提交事务
                sqlSession.commit();
                if (cnt > 0) {
                    DialogUtil.showDialog("INFORMATION", "注册成功！");
                    // 并且销毁当前stage，销毁时要先关闭会话资源
                    sqlSession.close();
                    ((Stage) rootPane.getScene().getWindow()).close();
                } else {
                    DialogUtil.showDialog("ERROR", "注册失败！");
                }
            }
        }
    }
}