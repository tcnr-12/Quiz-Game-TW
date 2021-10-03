package tw.tcnrcloud110.quiz;

public class Q0414 {
    public String TransDate;
    public String CropName;
    public String MarketName;
    public String Upper_Price;
    public String Middle_Price;
    public String Lower_Price;
    public String Avg_Price;
    public String Trans_Quantity;

    public Q0414(String TransDate, String CropName,
                 String MarketName, String Upper_Price, String Middle_Price, String Lower_Price, String Avg_Price, String Trans_Quantity) {

        this.TransDate = TransDate;
        this.CropName = CropName;
        this.MarketName = MarketName;
        this.Upper_Price = Upper_Price;
        this.Middle_Price = Middle_Price;
        this.Lower_Price = Lower_Price;
        this.Avg_Price = Avg_Price;
        this.Trans_Quantity = Trans_Quantity;
    }
}
