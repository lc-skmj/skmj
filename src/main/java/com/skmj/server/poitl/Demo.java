package com.skmj.server.poitl;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.PictureRenderData;
import com.deepoove.poi.data.Pictures;
import com.deepoove.poi.util.PoitlIOUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lc
 */
public class Demo {

    public static void main(String[] args) throws IOException {
        try {
            XWPFTemplate template = XWPFTemplate.compile("demo.docx").render(
                    new HashMap<String, Object>(1){{
                        put("title", "Hi, poi-tl Word模板引擎");
                    }});
            Map<String, Object> map = new HashMap<String, Object>();
            //人员信息
            map.put("name", "张三");
            map.put("sex", "男");
            map.put("age", "18");
            map.put("address", "北京");
            map.put("phone", "12345678901");
            map.put("email", "12345678901@163.com");
            map.put("date", "2020-01-01");
            map.put("remark", "备注");
            //头像
            PictureRenderData pictureRenderData = Pictures.ofUrl("a/png")
                    .size(50, 50).create();
            map.put("avatar", pictureRenderData);
            //部门信息
            map.put("department", "技术部");
            map.put("position", "Java开发");
            map.put("work_date", "2020-01-01");
            map.put("salary", "5000");
            map.put("bonus", "500");
            map.put("tax", "500");
            map.put("total", "5500");
            //培训信息
            map.put("training", "培训");
            map.put("training_name", "培训名称");
            map.put("training_date", "2020-01-01");
            map.put("training_content", "培训内容");
            map.put("training_result", "培训结果");
            map.put("training_remark", "培训备注");
            template.render(map);

            FileOutputStream fileOutputStream = new FileOutputStream("D:\\output.docx");
            template.write(fileOutputStream);
            PoitlIOUtils.closeQuietlyMulti(template, fileOutputStream);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
