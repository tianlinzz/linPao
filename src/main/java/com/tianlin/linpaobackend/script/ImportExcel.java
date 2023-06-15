package com.tianlin.linpaobackend.script;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * 导入Excel
 * @author 张添琳
 */
public class ImportExcel {
    /**
     * 最简单的读
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link CenterUserInfo}
     * <p>
     * 2. 由于默认一行行的读取excel，所以需要创建excel一行一行的回调监听器，参照{@link TableListener}
     * <p>
     * 3. 直接读即可
     */
    public static void main(String[] args) {
        String fileName = "D:\\LearnSpace\\linPao\\server\\src\\main\\resources\\static\\userTest.xlsx";
//        readByListener(fileName);
        synchronousRead(fileName);
    }

    /**
     * 读取Excel, 通过监听器
     * @param fileName 文件名
     * 这个方法更加灵活，可以读取多个sheet，一条一条的读取，不需要获取完整的数据就可以处理，对于大数据量的处理更加友好
     */
    public static void readByListener(String fileName) {
        // 写法1：JDK8+ ,不用额外写一个CenterUserInfoListener
        // since: 3.0.0-beta1
        // 这里默认每次会读取100条数据 然后返回过来 直接调用使用数据就行
        EasyExcel.read(fileName, CenterUserInfo.class, new TableListener()).sheet().doRead();
    }

    /**
     * 读取Excel, 同步读取
     * @param fileName 文件名
     * 这个方法更简单粗暴，但是不支持读取多个sheet，需要获取完整的数据后再处理，对于大数据量的处理不友好
     */
    public static void synchronousRead(String fileName) {
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 同步读取会自动finish
        List<CenterUserInfo> userlist = EasyExcel.read(fileName).head(CenterUserInfo.class).sheet().doReadSync();
        // 把相同用户昵称的用户放在同一组
        Map<String, List<CenterUserInfo>> listMap = userlist.stream()
                .filter(user -> StringUtils.isNotEmpty(user.getUsername()))
                .collect(Collectors.groupingBy(CenterUserInfo::getUsername));
        // 遍历每一组用户
        for (Map.Entry<String, List<CenterUserInfo>> entry : listMap.entrySet()) {
            // 获取用户组
            List<CenterUserInfo> users = entry.getValue();
            if (users.size() > 1) {
                System.out.println("用户组: " + users.get(0).getUsername());
            }
        }
        System.out.println("用户总数: " + listMap.size());
    }
}
