package emp;

public class EmpGoods {

    private String message;
    private double tolPrice;

    public EmpGoods(String message, double tolPrice) {
        this.message = message;
        this.tolPrice = tolPrice;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public double getTolPrice() {
        return tolPrice;
    }

    public void setTolPrice(double tolPrice) {
        this.tolPrice = tolPrice;
    }

    @Override
    public String toString() {
        return "EmpGoods{" +
                "message='" + message + '\'' +
                ", tolPrice=" + tolPrice +
                '}';
    }
}
