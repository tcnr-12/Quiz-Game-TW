package tw.tcnrcloud110.quiz;

public class Q0512_Post {
    public String Name;
    public String Coordinate ;
    public String Tel;
    public String posterThumbnailUrl;
    public String Address;
    public String Introduction;


    public Q0512_Post(String Name, String Coordinate, String Tel,
                      String posterThumbnailUrl, String Address, String Introduction) {

        this.Name = Name;  //名稱
        this.Coordinate = Coordinate; // 座標
        this.Tel = Tel; // ContactTel
        this.posterThumbnailUrl = posterThumbnailUrl; //圖片
        this.Address = Address;  //住址
        this.Introduction = Introduction; //描述

    }
}
