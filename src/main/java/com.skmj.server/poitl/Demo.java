package com.skmj.server.poitl;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.util.PoitlIOUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author lc
 */
public class Demo {

    public static void main(String[] args) throws IOException {
        try {
            XWPFTemplate template = XWPFTemplate.compile("D:\\myfile\\github-project\\skmj\\poi-tl\\src\\main\\resources\\doc\\demo.docx").render(
                    new HashMap<String, Object>(1){{
                        put("title", "Hi, poi-tl Word模板引擎");
                    }});
            FileOutputStream fileOutputStream = new FileOutputStream("D:\\output.docx");
            template.write(fileOutputStream);
            PoitlIOUtils.closeQuietlyMulti(template, fileOutputStream);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
