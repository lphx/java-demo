package cn.phlos.reptile.jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author: Penghong Li
 * @Date: Create in 11:00 2020/6/28
 *
 * 使用jsoup爬取开源中国项目的信息
 */

public class Demo2_Oschina {

    public static void main(String[] args) throws IOException {
        // 1.可以使用set集合把URL保存起来
        Set<String> setUrls = new HashSet<>();
        for(int i = 1; i <= 100; i++)
        {
            //关于这个链接怎么样获取的，可以使用控制台-->NetWork,查看该网站请求的URL即可
            String strUrl = "https://www.oschina.net/project/widgets/_project_list?company=0&tag=0&lang=0&os=0&sort=time&recommend=false&cn=false&weekly=false&p="+i+"&type=ajax";
            setUrls.add(strUrl);
        }

        Set<String> setProjUrls = new HashSet<>();
        //2.遍历URL
        for(String stringUrl : setUrls)
        {
            if(stringUrl.isEmpty()) continue;

            //模拟浏览器获取到URL的静态HTML
            Document document = Jsoup.connect(stringUrl)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.117 Safari/537.36")
                    .get();
            //获取到div标签的内容
            Elements elements = document.select("div.item.project-item");
            for(Element element : elements)
            {
                Elements eleUrl = element.select("h3 > a");
                String strPrjUrl = eleUrl.attr("href");
                System.out.println(strPrjUrl);
                String strTitle = eleUrl.attr("title");
                System.out.println(strTitle);
                System.out.println("-------------------");
            }
        }


    }
}
