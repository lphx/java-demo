package cn.phlos.reptile.jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * @Author: Penghong Li
 * @Date: Create in 10:44 2020/6/28
 * jsoup最简单的入门爬虫--demo1
 */

public class Demo1 {

    public static void main(String[] args) {
        try {
            //下面这行代码是连接我们的目标站点，并且get到他的静态HTML代码
            Document document= Jsoup.connect("https://www.runoob.com/java/java-tutorial.html").get();
            System.out.println("html静态网页代码：-->{} \n" + document);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
