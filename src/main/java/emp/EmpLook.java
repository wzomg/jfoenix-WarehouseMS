package emp;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class EmpLook {

    private SimpleIntegerProperty goodid = new SimpleIntegerProperty();
    private SimpleStringProperty goodname = new SimpleStringProperty();
    private SimpleStringProperty goodShelfid = new SimpleStringProperty();
    private SimpleIntegerProperty goodTotal = new SimpleIntegerProperty();
    private SimpleDoubleProperty goodPrice = new SimpleDoubleProperty();


    public EmpLook() {
    }

    public EmpLook(Integer goodid, String goodname, String goodShelfid, Integer goodTotal, Double goodPrice) {
        this.goodid.set(goodid);
        this.goodname.set(goodname);
        this.goodShelfid.set(goodShelfid);
        this.goodTotal.set(goodTotal);
        this.goodPrice.set(goodPrice);
    }

    public int getGoodid() {
        return goodid.get();
    }

    public SimpleIntegerProperty goodidProperty() {
        return goodid;
    }

    public void setGoodid(int goodid) {
        this.goodid.set(goodid);
    }

    public String getGoodname() {
        return goodname.get();
    }

    public SimpleStringProperty goodnameProperty() {
        return goodname;
    }

    public void setGoodname(String goodname) {
        this.goodname.set(goodname);
    }

    public String getGoodShelfid() {
        return goodShelfid.get();
    }

    public SimpleStringProperty goodShelfidProperty() {
        return goodShelfid;
    }

    public void setGoodShelfid(String goodShelfid) {
        this.goodShelfid.set(goodShelfid);
    }

    public int getGoodTotal() {
        return goodTotal.get();
    }

    public SimpleIntegerProperty goodTotalProperty() {
        return goodTotal;
    }

    public void setGoodTotal(int goodTotal) {
        this.goodTotal.set(goodTotal);
    }

    public double getGoodPrice() {
        return goodPrice.get();
    }

    public SimpleDoubleProperty goodPriceProperty() {
        return goodPrice;
    }

    public void setGoodPrice(double goodPrice) {
        this.goodPrice.set(goodPrice);
    }

    @Override
    public String toString() {
        return "EmpLook{" +
                "goodid=" + goodid +
                ", goodname=" + goodname +
                ", goodShelfid=" + goodShelfid +
                ", goodTotal=" + goodTotal +
                ", goodPrice=" + goodPrice +
                '}';
    }
}
