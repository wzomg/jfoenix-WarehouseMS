package patterns.observer;

import emp.EmpGoods;

import java.util.ArrayList;

public abstract class Subject {

    protected ArrayList<Observer> observers = new ArrayList<>();

    // 添加观察者
    public abstract void attach(Observer observer);

    // 删除观察者
    public abstract void detach(Observer observer);

    // 通知处理数据返回一个字符串
    public abstract EmpGoods transform();
}
