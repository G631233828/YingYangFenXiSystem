package zhongchiedu.app.controller.uedit;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.baidu.ueditor.ActionEnter;



@Controller
public class UEditorController {
	

	    @RequestMapping(value="/config")
	    public void config(HttpServletRequest request, HttpServletResponse response) {
	        response.setContentType("application/json");
	        String rootPath = request.getSession().getServletContext().getRealPath("/");
	        try {
	            String exec = new ActionEnter(request, rootPath).exec();
	            PrintWriter writer = response.getWriter();
	            writer.write(exec);
	            writer.flush();
	            writer.close();
	        } catch (IOException | JSONException e) {
	            e.printStackTrace();
	        }

	    }

}
