package tw.tcnrcloud110.quiz;

public class Q0511_Post {
    public String Name;
    public String ProduceOrg ;
    public String ContactTel;
    public String posterThumbnailUrl;
    public String SalePlace;
    public String Feature;
    public String Zipcode;

    public Q0511_Post(String Name, String ProduceOrg, String ContactTel,
                      String posterThumbnailUrl, String SalePlace, String Feature) {

        this.Name = Name;  //名稱
        this.ProduceOrg = ProduceOrg; // ProduceOrg
        this.ContactTel = ContactTel; // ContactTel
        this.posterThumbnailUrl = posterThumbnailUrl; //圖片
        this.SalePlace = SalePlace;  //住址
        this.Feature = Feature; //描述
//        this.Zipcode = Zipcode; //郵遞區號
    }
}
