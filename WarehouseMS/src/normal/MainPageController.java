package normal;
import emp.EmpGoods;
import emp.EmpLook;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import memento.CareTaker;
import memento.UserInfoOriginator;
import observer.ComputeObserver;
import observer.ConcreteSubject;
import observer.Observer;
import observer.Subject;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import prototype.Goods;
import proxy.IPrintLog;
import proxy.PrintLogProxy;
import proxy.PrintLogSon;
import utils.JDBCUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainPageController {

    @FXML
    private TabPane tabpane;

    @FXML
    private TextField goodName;

    @FXML
    private TextField goodShelfid;

    @FXML
    private TextField goodTotal;

    @FXML
    private TextField goodPrice;

    @FXML
    private Label showValue;

    @FXML
    private TextArea textArea;

    @FXML
    private TextField userid;

    @FXML
    private TextField userphone;

    @FXML
    private TextField useraddress;

    @FXML
    private TextField oldpwd;

    @FXML
    private TextField newpwd;

    @FXML
    private TableView<EmpLook> tableView;

    @FXML
    private TableColumn<EmpLook, Integer> colid;

    @FXML
    private TableColumn<EmpLook, String> colname;

    @FXML
    private TableColumn<EmpLook, String> colshelf;

    @FXML
    private TableColumn<EmpLook, Integer> colnum;

    @FXML
    private TableColumn<EmpLook, Double> colprice;

    @FXML
    private TextField fillid;

    @FXML
    private TextField fillname;

    @FXML
    private TextField fillshelf;

    @FXML
    private TextField fillnum;

    @FXML
    private TextField fillPrice;

    // 将所有货物作为参数传递给观察者
    private ArrayList<Goods> goodsArrayList = new ArrayList<>();

    // 创建一个抽象被观察者的实例
    private Subject subject = new ConcreteSubject();

    // 每次点击添加时去创建一个新的
    private Observer observer;

    // 第一次初始化，以后直接clone
    private Goods headGoods = new Goods();

    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    private String userIDDD = "";

    private ObservableList<EmpLook> data = FXCollections.observableArrayList();

    private EmpLook empLook;

    private boolean check(String str) {
        return str == null || str.equals("");
    }

    // 创建一个备忘录管理员
    private CareTaker careTaker = new CareTaker();

    private UserInfoOriginator uiog = new UserInfoOriginator();

    // tab标签的切换
    @FXML
    void tabClick() {
        if(tabpane.getSelectionModel().isSelected(2)) {
            System.out.println("点击了个人信息！");
        } else if(tabpane.getSelectionModel().isSelected(1)) {
            System.out.println("点击了货物出库！");
            data.clear();
            initGoods();
        }
    }

    public void initPersonData(String useriddd) {
        userIDDD = useriddd;
        String sql = "SELECT * FROM user WHERE userid = ?";
        Map<String, Object> map = template.queryForMap(sql, useriddd);
        updateData((String) map.get("userid"), (String) map.get("userpwd"), (String) map.get("userphone"), (String) map.get("useraddress"));

        uiog.setUseraddress((String) map.get("useraddress"));
        uiog.setUserid((String) map.get("userid"));
        uiog.setUserphone((String) map.get("userphone"));
        uiog.setUserpwd((String) map.get("userpwd"));
        careTaker.add(uiog.saveToMemento());
    }

    private void updateData(String uid, String upwd, String uph, String uad) {
        userid.setText(uid);
        oldpwd.setText(upwd);
        userphone.setText(uph);
        useraddress.setText(uad);
    }

    @FXML
    void addClick() {
        String gName = goodName.getText();
        String gShelf = goodShelfid.getText();
        String gTotal = goodTotal.getText();
        String gPrice = goodPrice.getText();
        if(check(gName) || check(gShelf) || check(gTotal) || check(gPrice)) {
            Dialog.showDialog("ERROR", "输入不能为空！");
        } else {
            // 判断是否为非负整数
            String reg1 = "^\\d+$";
            if(!gTotal.matches(reg1)) {
                Dialog.showDialog("ERROR", "输入的货物数量必须为正整数！");
                return;
            }
            // 判断是否为非负浮点数
            String reg2 = "^\\d+(\\.\\d+)?$";
            if(!gPrice.matches(reg2)) {
                Dialog.showDialog("ERROR", "输入的货物单价必须为非负浮点数！");
                return;
            }
            else {
                // 使用原型模式先克隆一个货物信息对象，并且重新赋值
                Goods curGoods = (Goods) headGoods.clone();
                curGoods.setgName(gName);
                curGoods.setgShelf(gShelf);
                curGoods.setgCnt(Integer.parseInt(gTotal));
                curGoods.setgPrice(Double.parseDouble(gPrice));
                goodsArrayList.add(curGoods);

                // 使用观察者更新清单和总价格
                observer = new ComputeObserver(goodsArrayList);
                // 添加观察者
                subject.attach(observer);
                // 通知观察者更新
                EmpGoods ans = subject.transform();
                // 将结果显示在列表上
                textArea.setText(ans.getMessage());
                showValue.setText("￥" + (Math.round(ans.getTolPrice() * 100)  / 100.0));
                // 注销当前对象
                subject.detach(observer);
                // 清空表单数据
                clearClick();
            }
        }
    }

    @FXML
    void clearClick() {
        goodName.setText("");
        goodShelfid.setText("");
        goodTotal.setText("");
        goodPrice.setText("");
    }


    // 把所有商品都插入到数据库
    @FXML
    void addToDBAClick() {
        if(check(textArea.getText())) {
            return;
        }
        // 插入到数据库中
        String sql = "INSERT INTO goods(goodname, goodShelfid, goodTotal, goodPrice) VALUES(?, ?, ?, ?)";
        int cnt = 0;
        for (Goods goods : goodsArrayList) {
            cnt += template.update(sql, goods.getgName(), goods.getgShelf(), goods.getgCnt(), goods.getgPrice());
        }
        if(cnt == goodsArrayList.size()) {
            IPrintLog printLog = new PrintLogSon();
            PrintLogProxy proxy = new PrintLogProxy(printLog);
            proxy.output(textArea.getText());
            Dialog.showDialog("INFORMATION", "入库成功！清单已打印到控制台中！");
            showValue.setText("￥0.00");
            textArea.setText("");
        } else {
            Dialog.showDialog("ERROR", "入库失败！");
        }
    }

    @FXML
    void resetClick() {
        userphone.setText("");
        useraddress.setText("");
        oldpwd.setText("");
        newpwd.setText("");
    }

    @FXML
    void revokeClick() {
        // 保留的状态大于1才可进行撤销操作
        int n = careTaker.getMementoList().size();
        if(n > 1) {
            // 撤销返回上一级操作
            uiog.restoreFromMemento(careTaker.getMementoList().get(n - 2));
            // 并且删除掉最后一个
            careTaker.getMementoList().remove(n - 1);
            // 更新数据库
            String sql = "UPDATE user SET userpwd = ?, userphone = ?, useraddress = ? WHERE userid = ?";
            int cnt = template.update(sql, uiog.getUserpwd(), uiog.getUserphone(), uiog.getUseraddress(), userIDDD);
            if(cnt > 0) {
                updateData(userIDDD , uiog.getUserpwd(), uiog.getUserphone(), uiog.getUseraddress());
                newpwd.setText("");
                Dialog.showDialog("INFORMATION", "撤销成功！");
                // 修改表单状态值
            } else {
                Dialog.showDialog("ERROR", "撤销失败！");
            }
        }
    }

    @FXML
    void updateClick() {
        // 先确定旧密码是否输入正确，若输入正确且新密码输入不为空，则直接修改
        String oldp = oldpwd.getText(), newp = newpwd.getText();
        if(check(oldp) || check(newp) || check(useraddress.getText()) || check(userphone.getText())) {
            Dialog.showDialog("ERROR", "输入不能为空！");
            return;
        }
        String sql = "SELECT userpwd FROM user where userid = ?";
        Map<String, Object> query = template.queryForMap(sql, userIDDD);
        if(!oldp.equals(String.valueOf(query.get("userpwd")))) {
            Dialog.showDialog("ERROR", "旧密码输入错误！");
            return;
        }
        sql = "UPDATE user SET userpwd = ?, userphone = ?, useraddress = ? where userid = ?";
        int cnt = template.update(sql, newp, userphone.getText(), useraddress.getText(), userIDDD);
        if(cnt > 0) {
            Dialog.showDialog("INFORMATION", "修改成功！");
            // 备忘录保存状态值
            uiog.setUserpwd(newp);
            uiog.setUserphone(userphone.getText());
            uiog.setUseraddress(useraddress.getText());
            careTaker.add(uiog.saveToMemento());
        } else {
            Dialog.showDialog("ERROR", "修改失败！");
        }
    }

    // 初始化货物表单数据
    private void initGoods() {
        String sql = "SELECT * FROM goods";
        List<EmpLook> query = template.query(sql, new BeanPropertyRowMapper<EmpLook>(EmpLook.class));
        data.addAll(query);
        tableView.setItems(data);
        colid.setCellValueFactory(new PropertyValueFactory<EmpLook, Integer>("goodid"));
        colname.setCellValueFactory(new PropertyValueFactory<EmpLook, String>("goodname"));
        colshelf.setCellValueFactory(new PropertyValueFactory<EmpLook, String>("goodShelfid"));
        colnum.setCellValueFactory(new PropertyValueFactory<EmpLook, Integer>("goodTotal"));
        colprice.setCellValueFactory(new PropertyValueFactory<EmpLook, Double>("goodPrice"));
    }

    // 直接删除书籍
    @FXML
    void deleteClick() {
        if(check(fillid.getText())) {
            return;
        }
        // 直接通过编号删除书籍信息
        String sql = "DELETE FROM goods WHERE goodid = ?";
        int cnt = template.update(sql, Integer.parseInt(fillid.getText()));
        if(cnt > 0) {
            data.remove(empLook);
            Dialog.showDialog("INFORMATION", "删除书籍成功！");
        } else {
            Dialog.showDialog("ERROR", "删除书籍失败！");
        }
    }

    // 出库一定数量的书籍
    @FXML
    void outboundClick() {
        if(check(fillid.getText())) {
            return;
        }
        String sql = "SELECT goodTotal FROM goods WHERE goodid = ?";
        Map<String, Object> map = template.queryForMap(sql, Integer.parseInt(fillid.getText()));
        int num1 = Integer.parseInt(fillnum.getText());
        int num2 = (Integer) map.get("goodTotal");
        if(num1 > num2) {
            Dialog.showDialog("ERROR", "输入数量不能超过原有货物的数量！");
        } else if(num1 < num2) {
            sql = "UPDATE goods SET goodTotal = goodTotal - ? WHERE goodid = ?";
            int cnt = template.update(sql, num1, Integer.parseInt(fillid.getText()));
            if(cnt > 0) {
                empLook.setGoodTotal(empLook.getGoodTotal() - num1);
                Dialog.showDialog("INFORMATION", "出库成功！");
            } else {
                Dialog.showDialog("ERROR", "出库失败！");
            }
        } else {
            sql = "DELETE FROM goods WHERE goodid = ?";
            int cnt = template.update(sql, Integer.parseInt(fillid.getText()));
            if(cnt > 0) {
                data.remove(empLook);
                Dialog.showDialog("INFORMATION", "出库成功！");
            } else {
                Dialog.showDialog("ERROR", "出库失败！");
            }
        }
    }

    // 双击选中
    @FXML
    void rowClick() {
        empLook = tableView.getSelectionModel().getSelectedItem();
        if(empLook == null) return;
        System.out.println("当前选中的是" + empLook);
        fillid.setText(String.valueOf(empLook.getGoodid()));
        fillname.setText(empLook.getGoodname());
        fillnum.setText(String.valueOf(empLook.getGoodTotal()));
        fillshelf.setText(empLook.getGoodShelfid());
        fillPrice.setText(String.valueOf(empLook.getGoodPrice()));
    }
}