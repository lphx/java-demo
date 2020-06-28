package cn.phlos.reptile.jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author: Penghong Li
 * @Date: Create in 14:37 2020/6/28
 *
 */

public class Demo3_baidutipian {


    public static void main(String[] args) {

            //下面这行代码是连接我们的目标站点，并且get到他的静态HTML代码
            String word = "唯美";
            Set<String> setUrls = new HashSet<>();
            //要获取到多少页的数据
            for(int i = 0; i <= 2; i++)
            {
                //关于这个链接怎么样获取的，可以使用控制台-->NetWork,查看该网站请求的URL即可
                String strUrl = "https://image.baidu.com/search/acjson?tn=resultjson_com&ipn=rj&ct=201326592&is=&fp=result&queryWord=%E5%94%AF%E7%BE%8E&cl=2&lm=-1&ie=utf-8&oe=utf-8&adpicid=&st=-1&z=&ic=0&hd=&latest=&copyright=&word="+word+"&s=&se=&tab=&width=&height=&face=0&istype=2&qc=&nc=1&fr=&expermode=&force=&pn="+i*30+"&rn=30";
                setUrls.add(strUrl);
            }

            get_html(setUrls);


    }

    private static void get_html(Set<String> setUrls) {
        try{
            int count = 0;
            for (String url: setUrls) {
                Document doc= Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0; MALC)")
                        .timeout(999999999)
                        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                        .header("Accept-Encoding", "gzip, deflate")
                        .header("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3")
                        .header("Connection", "keep-alive")
                        .header("Host", "image.baidu.com")
                        .header("Referer", "http://image.baidu.com/")
                        //是忽略请求类型
                        .ignoreContentType(true)
                        .get();
                        /*
                         * Accept:告知服务器，客户端可以接受的数据类型（MIME类型）
                         *        文件系统：通过文件的扩展名区分不同的文件的。txt jpeg
                         *        MIME类型：大类型/小类型。
                         *        txt--->text/plain   html---->text/html js---->text/javascript
                         *        (具体对应关系：Tomcat\conf\web.xml)
                         *Accept-Encoding：告知服务器，客户端可以接受的压缩编码。比如gzip
                         *Accept-Language：告知服务器，客户端支持的语言。
                         *  Referer：告知服务器，从哪个页面过来的。
                         *Content-Type：告知服务器，请求正文的MIME类型
                         *         默认类型：application/x-www-form-urlencoded(表单enctype属性的默认取值)
                         *         具体体现：username=abc&password=123
                         *         其他类型：multipart/form-data(文件上传时用的)
                         *User-Agent:告知服务器，浏览器的类型
                         *Content-Length：请求正文的数据长度
                         *Cookie：会话管理有关
                         */
                String str = doc.text();
                int start = str.indexOf("\"thumbURL\":\"");
                int end = str.indexOf("\",\"middleURL");
                String need = null;

                while (start != -1) {
                    need = str.substring(start + 12, end);
                    //System.out.println("need = " + need);
//                    System.out.println(need);
                    downImages("d:/img1", need);

                    start = str.indexOf("\"thumbURL\":\"", start + 1);
                    end = str.indexOf("\",\"middleURL", end + 1);
                    count++;
                }
            }


            System.out.println("count = " + count);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    //下载图片
    private static void downImages(String filePath, String imgUrl) {
        // 若指定文件夹没有，则先创建
        File dir = new File(filePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        // 截取图片文件名
        String fileName = imgUrl.substring(imgUrl.lastIndexOf('/') + 1, imgUrl.length());
        try {
            // 文件名里面可能有中文或者空格，所以这里要进行处理。但空格又会被URLEncoder转义为加号
            String urlTail = URLEncoder.encode(fileName, "UTF-8");
            // 因此要将加号转化为UTF-8格式的%20
            imgUrl = imgUrl.substring(0, imgUrl.lastIndexOf('/') + 1)
                    + urlTail.replaceAll("\\+", "\\%20");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 写出的路径
        File file = new File(filePath + File.separator + fileName);
        try {
            // 获取图片URL
            URL url = new URL(imgUrl);
            // 获得连接
            URLConnection connection = url.openConnection();
            // 设置10秒的相应时间
            connection.setConnectTimeout(10 * 1000);
            // 获得输入流
            InputStream in = connection.getInputStream();
            // 获得输出流
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
            // 构建缓冲区
            byte[] buf = new byte[1024];
            int size;
            // 写入到文件
            while (-1 != (size = in.read(buf))) {
                out.write(buf, 0, size);
            }
            out.close();
            in.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

