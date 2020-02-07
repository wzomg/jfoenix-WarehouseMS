package controller;

import dao.IGoodsDao;
import dao.IUserDao;
import emp.EmpGoods;
import emp.EmpLook;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.ibatis.session.SqlSession;
import patterns.bridge.*;
import patterns.memento.CareTaker;
import patterns.memento.Memento;
import patterns.memento.UserInfoOriginator;
import patterns.observer.ComputeObserver;
import patterns.observer.ConcreteSubject;
import patterns.observer.Observer;
import patterns.observer.Subject;
import patterns.prototype.Goods;
import patterns.proxy.IPrintLog;
import patterns.proxy.PrintLogProxy;
import patterns.proxy.PrintLogSon;
import utils.DialogUtil;
import utils.MybatisUtil;


import java.util.ArrayList;
import java.util.List;

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

    // 获取SqlSession对象
    private SqlSession sqlSession = MybatisUtil.getSession();
    // 创建userDao接口，然后通过反射来获取代理对象
    private IUserDao userDao = sqlSession.getMapper(IUserDao.class);
    // 创建goodsDao接口，然后通过反射来获取代理对象
    private IGoodsDao goodsDao = sqlSession.getMapper(IGoodsDao.class);

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
        if (tabpane.getSelectionModel().isSelected(2)) {
            System.out.println("点击了个人信息！");
        } else if (tabpane.getSelectionModel().isSelected(1)) {
            System.out.println("点击了货物出库！");
            data.clear();
            initGoods();
        }
    }

    public void initPersonData(String useriddd) {
        userIDDD = useriddd;
        Memento user = userDao.findUserById(useriddd);
        updateData(user.getUserid(), user.getUserpwd(), user.getUserphone(), user.getUseraddress());

        uiog.setUseraddress(user.getUseraddress());
        uiog.setUserid(user.getUserid());
        uiog.setUserphone(user.getUserphone());
        uiog.setUserpwd(user.getUserpwd());
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
        if (check(gName) || check(gShelf) || check(gTotal) || check(gPrice)) {
            DialogUtil.showDialog("ERROR", "输入不能为空！");
        } else {
            // 判断是否为非负整数
            String reg1 = "^\\d+$";
            if (!gTotal.matches(reg1)) {
                DialogUtil.showDialog("ERROR", "输入的货物数量必须为正整数！");
                return;
            }
            // 判断是否为非负浮点数
            String reg2 = "^\\d+(\\.\\d+)?$";
            if (!gPrice.matches(reg2)) {
                DialogUtil.showDialog("ERROR", "输入的货物单价必须为非负浮点数！");
                return;
            } else {
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
                showValue.setText("￥" + (Math.round(ans.getTolPrice() * 100) / 100.0));
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
        if (check(textArea.getText())) {
            return;
        }

        // 将所有商品信息都插入到数据库中
        int cnt = goodsDao.addGoods(goodsArrayList);
        // 注意提交事务，不然没有增加数据
        if (cnt == goodsArrayList.size()) {
            sqlSession.commit();
            // 打印日志
            /*IPrintLog printLog = new PrintLogSon();
            PrintLogProxy proxy = new PrintLogProxy(printLog);
            proxy.output(textArea.getText());*/

            ImpLog j = new JImpLog(); //建立java平台
            Log jl = new TextFileLog(j); //建立基于java 的文本文件日志写入
            jl.write(textArea.getText()); //写入日志文件

            DialogUtil.showDialog("INFORMATION", "入库成功！日志已成功写入！");
            showValue.setText("￥0.00");
            textArea.setText("");
        } else {
            DialogUtil.showDialog("ERROR", "入库失败！");
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
        if (n > 1) {
            // 撤销返回上一级操作
            uiog.restoreFromMemento(careTaker.getMementoList().get(n - 2));
            // 并且删除掉最后一个
            careTaker.getMementoList().remove(n - 1);
            // 更新用户信息
            Memento tmp = new Memento(userIDDD, uiog.getUserpwd(), uiog.getUserphone(), uiog.getUseraddress());
            int cnt = userDao.updateUserById(tmp);
            if (cnt > 0) {
                sqlSession.commit();
                updateData(userIDDD, uiog.getUserpwd(), uiog.getUserphone(), uiog.getUseraddress());
                newpwd.setText("");
                DialogUtil.showDialog("INFORMATION", "撤销成功！");
                // 修改表单状态值
            } else {
                DialogUtil.showDialog("ERROR", "撤销失败！");
            }
        }
    }

    @FXML
    void updateClick() {
        // 先确定旧密码是否输入正确，若输入正确且新密码输入不为空，则直接修改
        String oldp = oldpwd.getText(), newp = newpwd.getText();
        if (check(oldp) || check(newp) || check(useraddress.getText()) || check(userphone.getText())) {
            DialogUtil.showDialog("ERROR", "输入不能为空！");
            return;
        }
        Memento nowU = userDao.findUserById(userIDDD);

        if (!oldp.equals(nowU.getUserpwd())) {
            DialogUtil.showDialog("ERROR", "旧密码输入错误！");
            return;
        }
        Memento tmp = new Memento(userIDDD, newp, userphone.getText(), useraddress.getText());
        int cnt = userDao.updateUserById(tmp);
        if (cnt > 0) {
            sqlSession.commit();
            DialogUtil.showDialog("INFORMATION", "修改成功！");
            // 备忘录保存状态值
            uiog.setUserpwd(newp);
            uiog.setUserphone(userphone.getText());
            uiog.setUseraddress(useraddress.getText());
            careTaker.add(uiog.saveToMemento());
        } else {
            DialogUtil.showDialog("ERROR", "修改失败！");
        }
    }

    // 初始化货物表单数据
    private void initGoods() {
        List<Goods> allGoods = goodsDao.findAllGoods();
        System.out.println(allGoods);
        List<EmpLook> query = new ArrayList<>();
        for (Goods allGood : allGoods) {
            query.add(new EmpLook(allGood.getGid(), allGood.getgName(), allGood.getgShelf(), allGood.getgCnt(), allGood.getgPrice()));
        }
        data.addAll(query);
        tableView.setItems(data);
        colid.setCellValueFactory(new PropertyValueFactory<EmpLook, Integer>("goodid"));
        colname.setCellValueFactory(new PropertyValueFactory<EmpLook, String>("goodname"));
        colshelf.setCellValueFactory(new PropertyValueFactory<EmpLook, String>("goodShelfid"));
        colnum.setCellValueFactory(new PropertyValueFactory<EmpLook, Integer>("goodTotal"));
        colprice.setCellValueFactory(new PropertyValueFactory<EmpLook, Double>("goodPrice"));
    }

    //清空下方的表单
    void clearFill() {
        fillid.setText("");
        fillname.setText("");
        fillnum.setText("");
        fillPrice.setText("");
        fillshelf.setText("");
    }

    // 直接删除商品
    @FXML
    void deleteClick() {
        if (check(fillid.getText())) {
            return;
        }
        // 直接通过编号删除书籍信息、
        int cnt = goodsDao.deleteGoodById(Integer.parseInt(fillid.getText()));
        if (cnt > 0) {
            sqlSession.commit();
            clearFill();
            data.remove(empLook);
            DialogUtil.showDialog("INFORMATION", "删除商品成功！");
            // 清空表单
        } else {
            DialogUtil.showDialog("ERROR", "删除商品失败！");
        }
    }

    // 出库一定数量的书籍
    @FXML
    void outboundClick() {
        if (check(fillid.getText())) {
            return;
        }
        // 查询某个商品的信息
        Goods nowG = goodsDao.findGoodById(Integer.parseInt(fillid.getText()));
        String inputNum = fillnum.getText();
        //输入不能为非负整数
        String reg3 = "^\\d+$";
        if (!inputNum.matches(reg3)) {
            DialogUtil.showDialog("ERROR", "出库数量必须为非负整数！");
            return;
        }
        int num1 = Integer.parseInt(fillnum.getText());
        int num2 = nowG.getgCnt();
        if (num1 > num2) {
            DialogUtil.showDialog("ERROR", "输入数量不能超过原有货物的数量！");
        } else if (num1 < num2) {
            int cnt = goodsDao.updateGoodsById(Integer.parseInt(fillid.getText()), num1);
            if (cnt > 0) {
                sqlSession.commit();
                empLook.setGoodTotal(empLook.getGoodTotal() - num1);
                DialogUtil.showDialog("INFORMATION", "出库成功！");
            } else {
                DialogUtil.showDialog("ERROR", "出库失败！");
            }
        } else { //直接删除该商品
            int cnt = goodsDao.deleteGoodById(Integer.parseInt(fillid.getText()));
            if (cnt > 0) {
                sqlSession.commit();
                clearFill();
                data.remove(empLook);
                DialogUtil.showDialog("INFORMATION", "出库成功！");
            } else {
                DialogUtil.showDialog("ERROR", "出库失败！");
            }
        }
    }

    // 双击选中
    @FXML
    void rowClick() {
        empLook = tableView.getSelectionModel().getSelectedItem();
        if (empLook == null) return;
        System.out.println("当前选中的是" + empLook);
        fillid.setText(String.valueOf(empLook.getGoodid()));
        fillname.setText(empLook.getGoodname());
        fillnum.setText(String.valueOf(empLook.getGoodTotal()));
        fillshelf.setText(empLook.getGoodShelfid());
        fillPrice.setText(String.valueOf(empLook.getGoodPrice()));
    }
}