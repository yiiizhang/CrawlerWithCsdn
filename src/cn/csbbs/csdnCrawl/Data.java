package cn.csbbs.csdnCrawl;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;  
  
    
public class Data {  
    
	private static List<Info> infos;  
    private static HashMap<String, Integer> result;  
    
    //爬取策略
    private static int PageCount = 200;
    
    //爬取开始位置
    private static int temp = 1;
    
    private QueryRunner qr = new TxQueryRunner();  
    
    static {  
        infos = new ArrayList<Info>(); 
        for( ; temp<=PageCount; temp ++)
        infos.add(new Info("http://blog.csdn.net/?ref=toolbar&page="+temp, "page "+temp));  
    }  
      
    /** 
     *@Description:  抓取网址信息 
     *@Author:YiZhang   
     */  
    static class Info{  
        String url;  
        String type;  
        Info(String url, String type) {  
            this.url = url;  
            this.type = type;  
        }  
    }  
      
    /** 
     * @param info 
     * @Description: 抓取一个列表页面下的Blog信息 
     */  
    private void crawl(Info info) {  
        if (info == null) {  
            return;  
        }  
        try {  
            CsdnCrawlList csdnCrawlList = new CsdnCrawlList(info.url);  
            List<String> urls = csdnCrawlList.getPageUrls(); 
            HashSet h = new HashSet(urls); //使用hashset去重
            urls.clear();
            urls.addAll(h);
            for (String url : urls) { 
            	System.out.println(url + "爬取存储开始");  
                Blog blog = new Blog(url); 
                save(blog);
                System.out.println(url + "爬取存储结束");
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
      
    /** 
     * @param saveBlog 
     * @Description: 存储并对Blog信息进行去噪处理
     */
	public void save(Blog blog) throws SQLException {
		String blogid = CommonUtils.uuid();
		String userid = "F691D2A340DF420EA13856BE49AE48F6";
		String intro = blog.getContent().replaceAll("\n", "")  
                .replaceAll("<script.*?/script>", "")  
                .replaceAll("<style.*?/style>", "")  
                .replaceAll("<.*?>", "");
        intro=intro.length() > 250 ? intro.substring(0, 250) : intro;
		Date now =new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String Starttime = dateFormat.format( now );
		String sql = "insert into blog (blogid,userid,starttime,content,topic,image,tags,introduction,identy) values(?,?,?,?,?,?,?,?,?)";
		Object[] params = { blogid,userid, Starttime,
				blog.getContent(),blog.getTopic(),"images/csdnlogo.png",blog.getTag(),intro,blog.getTag() };
		qr.update(sql, params);

	}
    
    /** 
     * @Description: 启动入口 
     */  
    public void run() {  
         
        for (Info info : infos) {  
            System.out.println(info.url + "------start");  
            crawl(info);  
            System.out.println(info.url + "------end");  
        }  
        
    }  
      
    public static void main(String[] args) {  
        new Data().run();  
    }  
}  