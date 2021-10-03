package tw.tcnr12.m2207;

public class Q0401a_Post {
    public String Name;
    public String Picture1;
    public String Description;
    public String Context;
    public String Text_url;

    public Q0401a_Post(String name, String description, String content, String img_url, String text_url) {

        this.Name = name;//----------------------------------------品種名稱
        this.Description = description;//----------------------------簡述
        this.Context = content;//-----------------------------------描述
        this.Picture1 = img_url;//-----------------------------------圖片網址
        this.Text_url = text_url;//-----------------------------------描述
    }
}