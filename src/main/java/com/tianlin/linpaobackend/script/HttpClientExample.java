package com.tianlin.linpaobackend.script;

import com.google.gson.Gson;
import com.tianlin.linpaobackend.model.domain.User;
import com.tianlin.linpaobackend.service.impl.UserServiceImpl;
import lombok.Data;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class HttpClientExample {

    // 定义常见的标签数组
    private static final String[] commonLabels =  {
            "java", "python", "JavaScript", "C++", "C#", "PHP", "Ruby", "Go", "Swift",
            "大专", "本科", "硕士", "博士", "211", "985", "双一流", "高职", "专科",
            "计算机科学", "软件工程", "网络工程", "信息管理", "电子信息", "自动化", "物联网", "人工智能",
            "机械", "土木", "电子", "化学", "生物", "医学", "法律", "金融", "经济", "市场营销", "翻译", "教育",
            "心理学", "历史", "哲学", "艺术", "音乐", "体育", "美术", "设计", "摄影", "影视", "动漫", "游戏",
            "男", "女", "单身", "已婚", "有孩子", "宠物爱好者",
            "互联网", "电商", "金融", "教育", "医疗", "房产", "汽车", "旅游", "餐饮", "娱乐", "体育",
            "动漫", "健身", "健康", "养生", "星座", "生肖", "血型", "学生", "程序员",
            "教师", "医生", "护士", "律师", "会计", "公务员", "军人", "警察", "厨师", "服务员", "司机",
            "前端", "后端", "全栈", "前端工程师", "后端工程师", "全栈工程师" , "移动开发", "数据库", "网络工程师", "安全工程师", "人工智能", "大数据", "云计算",
            "运维", "测试", "游戏开发", "嵌入式", "计算机视觉", "机器学习", "深度学习", "自然语言处理",
            "微服务", "Spring", "Spring Boot", "Spring Cloud", "MyBatis", "Hibernate", "Vue", "React",
            "Angular", "Node.js", "Django", "Flask", "Ruby on Rails", "ASP.NET", "Laravel", "Express",
            "Spring MVC", "Spring Security", "Spring Data", "Spring Integration", "Spring Batch",
            "Spring AMQP", "Spring Session", "Spring Social", "Spring Web Services", "Spring HATEOAS",
    };

    private static final String[] urlList = {
            "https://api.zsxq.com/v2/hashtags/28851422588481/topics?count=20",
            "https://api.zsxq.com/v2/hashtags/28851422588481/topics?count=20&end_time=2023-01-20T21%3A49%3A57.095%2B0800",
            "https://api.zsxq.com/v2/hashtags/28851422588481/topics?count=20&end_time=2022-08-08T14%3A49%3A57.523%2B0800",
            "https://api.zsxq.com/v2/hashtags/28851422588481/topics?count=20&end_time=2022-05-24T10%3A57%3A55.688%2B0800",
            "https://api.zsxq.com/v2/hashtags/28851422588481/topics?count=20&end_time=2022-05-01T14%3A40%3A49.854%2B0800",
            "https://api.zsxq.com/v2/hashtags/28851422588481/topics?count=20&end_time=2022-04-04T11%3A42%3A26.142%2B0800",
            "https://api.zsxq.com/v2/hashtags/28851422588481/topics?count=20&end_time=2022-02-27T15%3A25%3A45.336%2B0800",
            "https://api.zsxq.com/v2/hashtags/28851422588481/topics?count=20&end_time=2022-02-05T01%3A39%3A56.701%2B0800",
    };

    public static void main(String[] args) {
        // 创建数据库连接
        Connection connection = null;
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/tianlin?useUnicode=true&characterEncoding=utf8";
        String username = "root";
        String password = "root";
        Connection conn = null;

        try {
            Class.forName(driver); //
            conn = (Connection) DriverManager.getConnection(url, username, password);
            System.out.println("数据库连接成功");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        // 创建HttpClient实例
        HttpClient httpClient = HttpClients.custom().build();

        // 创建HttpGet请求对象，并设置URL
        HttpGet httpGet = new HttpGet(urlList[7]);

        // 设置请求头
        httpGet.setHeader("User-Agent", "Mozilla/5.0");
        httpGet.addHeader("authority", "api.zsxq.com");
        httpGet.addHeader("accept", "application/json, text/plain, */*");
        httpGet.addHeader("accept-language", "zh-CN,zh;q=0.9");
        httpGet.setHeader("Cookie", "zsxq_access_token=EFF58059-A5A9-539B-93E2-DC0A62C222D2_5EA042F40C990470; sensorsdata2015jssdkcross=%7B%22distinct_id%22%3A%22185448882851152%22%2C%22first_id%22%3A%221882d98b716a5-084551f64ecd388-26031a51-2073600-1882d98b717e36%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E7%9B%B4%E6%8E%A5%E6%B5%81%E9%87%8F%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC_%E7%9B%B4%E6%8E%A5%E6%89%93%E5%BC%80%22%2C%22%24latest_referrer%22%3A%22%22%7D%2C%22identities%22%3A%22eyIkaWRlbnRpdHlfY29va2llX2lkIjoiMTg4MmQ5OGI3MTZhNS0wODQ1NTFmNjRlY2QzODgtMjYwMzFhNTEtMjA3MzYwMC0xODgyZDk4YjcxN2UzNiIsIiRpZGVudGl0eV9sb2dpbl9pZCI6IjE4NTQ0ODg4Mjg1MTE1MiJ9%22%2C%22history_login_id%22%3A%7B%22name%22%3A%22%24identity_login_id%22%2C%22value%22%3A%22185448882851152%22%7D%2C%22%24device_id%22%3A%221882d98b716a5-084551f64ecd388-26031a51-2073600-1882d98b717e36%22%7D; abtest_env=product; zsxqsessionid=b58040318bf060ffc3fcdbe63ded845a");

        try {
            // 发送请求，并获取响应
            HttpResponse response = httpClient.execute(httpGet);

            // 获取响应的状态码
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                // 获取响应的内容
                String responseBody = EntityUtils.toString(response.getEntity());
                Gson gson = new Gson();

                // 将JSON字符串反序列化为Java对象
                ResponseObject responseObject = gson.fromJson(responseBody, ResponseObject.class);

                // 处理响应结果
                Connection finalConn = conn;
                responseObject.getResp_data().getTopics().forEach(topic -> {
                    Long user_id = topic.getTalk().getOwner().getUser_id(); // userCode
                    String name = topic.getTalk().getOwner().getName(); // username
                    String avatar_url = topic.getTalk().getOwner().getAvatar_url(); // 头像
                    String location = topic.getTalk().getOwner().getLocation(); // 算作一个标签
                    String text = topic.getTalk().getText(); // 从中提取一些标签
                    // 存储符合条件的定制标签
                    List<String> customizedLabels = new ArrayList<>();
                    // 遍历常见标签，检查是否在分词结果中
                    for (String label : commonLabels) {
                        if (text.contains(label.toLowerCase())) {
                            customizedLabels.add(label);
                        }
                    }
                    customizedLabels.add(location);
                    // customizedLabels JSON化
                    String customizedLabelsJson = gson.toJson(customizedLabels);
                    User user = new User();
                    user.setUserCode(user_id);
                    user.setUsername(name);
                    user.setTags(customizedLabelsJson);
                    user.setGender(1);
                    user.setAvatarUrl(avatar_url);
                    user.setUserAccount(user_id.toString());
                    user.setUserPassword("b07293a87bcbc77ac7e90543141d068c");
                    // 插入数据库
                    try {
                        if (finalConn != null) {
                            Statement sql = finalConn.createStatement();
                            String sqlStr = "insert into user(userCode, username, tags, avatarUrl, userAccount, userPassword) values(" + user.getUserCode() + ", '" + user.getUsername() + "', '" + user.getTags() + "', '" + user.getAvatarUrl() + "', '" + user.getUserAccount() + "', '" + user.getUserPassword() + "')";
                            sql.executeUpdate(sqlStr);
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
                return;
            }
            System.out.println("响应状态码：" + statusCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

@Data
class ResponseObject {
    private RespData resp_data;
    private boolean succeeded;
}

@Data
class RespData {
    private List<Topic> topics;
}

@Data
class Topic {
    private Talk talk;
}

@Data
class Talk {
    private Owner owner;
    private String text;
}

@Data
class Owner {
    private String name;
    private String avatar_url;
    private Long user_id;
    private String location;
}

