//package ua.com.itproekt.gup.service.offers.price;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Locale;
//import java.util.Map;
//import java.util.TimeZone;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.kendoui.spring.models.Task;
//import com.kendoui.spring.models.TaskDao;
//
//@Controller("scheduler-home-controller")
//@RequestMapping(value="/scheduler/")
//public class IndexController {
//    @Autowired
//    private TaskDao task;
//
//    @RequestMapping(value = {"/", "/index"}, method = RequestMethod.GET)
//    public String index(Locale locale, Model model) {
//        return "scheduler/index";
//    }
//
//    @RequestMapping(value = "/index/read", method = RequestMethod.POST)
//    public @ResponseBody List<Task> read() {
//        return task.getList();
//    }
//
//    @RequestMapping(value = "/index/create", method = RequestMethod.POST)
//    public @ResponseBody List<Task> create(@RequestBody ArrayList<Map<String, Object>> models) throws ParseException {
//        List<Task> tasks = new ArrayList<Task>();
//
//        for (Map<String, Object> model : models) {
//            Task task = new Task();
//
//            task.setDescription((String)model.get("description"));
//            task.setTitle((String)model.get("title"));
//
//            SimpleDateFormat iso8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
//            iso8601.setTimeZone(TimeZone.getTimeZone("UTC"));
//
//            task.setStart(iso8601.parse((String)model.get("start")));
//            task.setEnd(iso8601.parse((String)model.get("end")));
//            task.setIsAllDay((boolean)model.get("isAllDay"));
//            task.setRecurrenceRule((String)model.get("recurrenceRule"));
//            task.setRecurrenceException((String)model.get("recurrenceException"));
//            task.setRecurrenceId((Integer)model.get("recurrenceId"));
//            task.setOwnerId((Integer)model.get("ownerId"));
//
//            tasks.add(task);
//        }
//
//        task.saveOrUpdate(tasks);
//
//        return tasks;
//    }
//
//    @RequestMapping(value = "/index/update", method = RequestMethod.POST)
//    public @ResponseBody List<Task> update(@RequestBody ArrayList<Map<String, Object>> models) throws ParseException {
//        List<Task> tasks = new ArrayList<Task>();
//
//        for (Map<String, Object> model : models) {
//            Task task = new Task();
//
//            task.setTaskId((int)model.get("taskId"));
//            task.setDescription((String)model.get("description"));
//            task.setTitle((String)model.get("title"));
//
//            SimpleDateFormat iso8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
//            iso8601.setTimeZone(TimeZone.getTimeZone("UTC"));
//
//            task.setStart(iso8601.parse((String)model.get("start")));
//            task.setEnd(iso8601.parse((String)model.get("end")));
//            task.setIsAllDay((boolean)model.get("isAllDay"));
//            task.setRecurrenceRule((String)model.get("recurrenceRule"));
//            task.setRecurrenceException((String)model.get("recurrenceException"));
//            task.setRecurrenceId((Integer)model.get("recurrenceId"));
//            task.setOwnerId((Integer)model.get("ownerId"));
//
//            tasks.add(task);
//        }
//
//        task.saveOrUpdate(tasks);
//
//        return tasks;
//    }
//
//    @RequestMapping(value = "/index/destroy", method = RequestMethod.POST)
//    public @ResponseBody List<Task> destroy(@RequestBody ArrayList<Map<String, Object>> models) throws ParseException {
//        List<Task> tasks = new ArrayList<Task>();
//
//        for (Map<String, Object> model : models) {
//            Task task = new Task();
//
//            task.setTaskId((int)model.get("taskId"));
//
//            SimpleDateFormat iso8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
//            iso8601.setTimeZone(TimeZone.getTimeZone("UTC"));
//            task.setStart(iso8601.parse((String)model.get("start")));
//            task.setEnd(iso8601.parse((String)model.get("end")));
//
//            tasks.add(task);
//        }
//
//        task.delete(tasks);
//
//        return tasks;
//    }
//}
