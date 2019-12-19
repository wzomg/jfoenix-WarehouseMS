package observer;

import emp.EmpGoods;

public class ConcreteSubject extends Subject{

    // 注册方法
    @Override
    public void attach(Observer observer) {
        observers.add(observer);
    }

    // 注销方法
    @Override
    public void detach(Observer observer) {
        observers.remove(observer);
    }

    // 处理数据并返回单个字符串
    @Override
    public EmpGoods transform() {
        String res = "";
        double ans = 0.0;
        for (Observer observer : observers) {
            res = observer.response();
            ans = observer.cal();
        }
        return new EmpGoods(res, ans);
    }
}
