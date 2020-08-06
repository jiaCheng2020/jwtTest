package demo1.tools;


import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class HttpUtils {

    public static  void write(HttpServletResponse response,String s) throws IOException {
        if (response.isCommitted()){
            log.debug("response has been committed");
            return;
        }
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();
        writer.write(s);
        writer.flush();
        writer.close();
    }

}
