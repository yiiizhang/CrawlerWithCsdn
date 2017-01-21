package cn.csbbs.csdnCrawl;

import java.io.IOException;  
import java.util.HashMap;  
  
import org.apache.commons.httpclient.HttpException;  
  
import cn.csbbs.crawler.CrawlBase;  
import cn.csbbs.util.DoRegex;  
    
public class Blog extends CrawlBase{  
    private String url;  
    private String content;  
    private String topic;  
    private String tag;  
    
    //爬取正则 
    private static String contentRegex = "<p.*?>(.*?)</p>";  
    private static String topicRegex = "<title>(.*?)</title>";  
    private static String tagRegex = ">(.{1,20})<em"; 
    
    private static int maxLength = 100; //topicmaxLength
      
    private static HashMap<String, String> params;  
    /** 
     * 添加相关头信息，对请求进行伪装 
     */  
    static {  
        params = new HashMap<String, String>();  
        params.put("Referer", "http://www.baidu.com");  
        params.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.125 Safari/537.36");  
    }  
      
    /** 
     * @Author:YiZhang   
     * @Description: 默认p标签内的内容为正文，如果正文长度查过设置的最大长度，则截取前半部分 
     */  
    private void setContent() {  
        String content = DoRegex.getString(getPageSourceCode(), contentRegex, 1);  
        this.content = content.replaceAll("\n", "")  
                                      .replaceAll("<script.*?/script>", "")  
                                      .replaceAll("<style.*?/style>", "")  
                                      .replaceAll("<.*?>", "");  
    }  
      
    /** 
     * @Author:lulei   
     * @Description: 默认titie标签内的内容为标题 
     */  
    private void setTopic() {  
        String topic = DoRegex.getString(getPageSourceCode(), topicRegex, 1);
        topic = topic.replaceAll("(.*?)- 博客频道 - CSDN.NET", "").replaceAll("\n", "");  
        this.topic = topic.length() > maxLength ? topic.substring(0, maxLength) : topic;  
    }
    private void setTag() {  
        this.tag = DoRegex.getTagString(getPageSourceCode(), tagRegex, 1); 
    }
      
    public Blog(String url) throws HttpException, IOException {  
        this.url = url;  
        readPageByGet(url, "utf-8", params);  
        setContent();  
        setTopic(); 
        setTag();
    }  
  
    public String getUrl() {  
        return url;  
    }  
  
    public void setUrl(String url) {  
        this.url = url;  
    }  
  
    public String getContent() {  
        return content;  
    }  
  
    public String getTopic() {
		return topic;
	}

	public String getTag() {
		return tag;
	}

	public static void setMaxLength(int maxLength) {  
        Blog.maxLength = maxLength;  
    }  
  
    /** 
     * @param args 
     * @throws HttpException 
     * @throws IOException 
     * @Author:YiZhang
     * @Description: 测试用例 
     */  
    public static void main(String[] args) throws HttpException, IOException {  
        // TODO Auto-generated method stub    
        Blog blog = new Blog("http://blog.csdn.net/a_running_wolf/article/details/52579178");  
        System.out.println(blog.getTopic());
        System.out.println(blog.getTag());
        System.out.println(blog.getContent());  
    }  
  
}  