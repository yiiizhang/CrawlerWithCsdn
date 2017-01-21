package cn.csbbs.csdnCrawl;

import java.io.IOException;  
import java.util.HashMap;  
  
import cn.csbbs.crawler.CrawlListPageBase;  
    
public class CsdnCrawlList extends CrawlListPageBase{  
    private static HashMap<String, String> params;  
      
    /** 
     * 添加相关头信息，对请求进行伪装 
     */  
    static {  
        params = new HashMap<String, String>();  
        params.put("Referer", "http://www.csdn.net");  
        params.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.125 Safari/537.36");  
    }  
  
    public CsdnCrawlList(String urlStr) throws IOException {  
        super(urlStr, "utf-8", "get", params);    
    }  
  
    @Override  
    public String getUrlRegexString() {  
        //Blog列表页中文章链接地址的正则表达式  
        //return "<a href=\"http://blog.csdn.net/(.*?)/article/details/d{8}\"";  
    	return "<a href=\"(http://blog.csdn.net(.{1,40})/article/details/[0-9]{8})\"";
    }  
  
    @Override  
    public int getUrlRegexStringNum() {  
        // TODO Auto-generated method stub    
        //链接地址在正则表达式中的位置  
        return 1;  
    }  
  
    /**   
     * @param args 
     * @throws IOException  
     * @Author:YiZhang   
     * @Description:  测试用例 
     */  
    public static void main(String[] args) throws IOException {  
        // TODO Auto-generated method stub    
    	CsdnCrawlList csdn = new CsdnCrawlList("http://blog.csdn.net/?ref=toolbar&page=2");  
        for (String s : csdn.getPageUrls()) {  
            System.out.println(s);  
        }  
    }  
}  
