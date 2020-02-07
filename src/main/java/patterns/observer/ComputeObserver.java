package patterns.observer;


import patterns.prototype.Goods;

import java.util.ArrayList;

// 观察者具体实现类
public class ComputeObserver implements Observer {

    private ArrayList<Goods> list;

    public ComputeObserver(ArrayList<Goods> list) {
        this.list = list;
    }

    @Override
    public String response() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0, len = list.size(); i < len; ++i) {
            builder.append((i + 1) + ". " + list.get(i).getgName() + "; 放在" + list.get(i).getgShelf() + "货架; 进货" + list.get(i).getgCnt() + "件; 单价" + list.get(i).getgPrice() + "元; 成本为" + ((Math.round(list.get(i).getgPrice() * list.get(i).getgCnt() * 100)) / 100.0) + "元。\n");
        }
        return builder.toString();
    }

    @Override
    public double cal() {
        double res = 0.0;
        for (Goods goods : list) {
            res += goods.getgPrice() * goods.getgCnt();
        }
        return res;
    }
}
