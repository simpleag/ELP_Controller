package com.elp.controller;

import com.elp.model.*;
import com.elp.service.*;
import com.elp.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by ASUS on 2017/7/14.
 */
@RestController
@CrossOrigin
public class AddTest {
    @Autowired
    private UserService userService;
    @Autowired
    private OfficeService officeService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private LessonService lessonService;
    @Autowired
    private CourseRecordService courseRecordService;
    @Autowired
    private CourseRelationOfficeService courseRelationOfficeService;
    @Autowired
    private LessonRecordService lessonRecordService;
    @Autowired
    private DiscussService discussService;
    @Autowired
    private DepartmentService departmentService;

    @PostMapping("/addtest")
    public Result addtest(){
        String[] departments = {"设计", "开发"};
        String[] userName = {"林罡" ,
                "陈文军" ,
                "石丹云" ,
                "朱克然" ,
                "苏春福" ,
                "李英爽" ,
                "洪滨" ,
                "杨攻" ,
                "朱小龙" ,
                "张秀洁" ,
                "连君龙" ,
                "常蕾" ,
                "邹志坚" ,
                "李培凤" ,
                "张忠柱" ,
                "金益芝" ,
                "赵东" ,
                "吴丽琴" ,
                "周国祥" ,
                "刘萌" ,
                "郭建德" ,
                "陶威" ,
                "袁佳慧" ,
                "韩悦" ,
                "孙斐" ,
                "张凯" ,
                "张金山" ,
                "邓琦" ,
                "刘春萍" ,
                "薛承浪" ,
                "何伟明" ,
                "陈佳华" ,
                "郑妙铃" ,
                "张和风" ,
                "赵旭东" ,
                "薛新鹏"};
        String[] offices = {"设计", "产品", "业务", "android开发", "IOS开发", "前端开发", "后端开发", "数据库开发", "算法分析"};
        String[] courseName = {"设计冲刺", "产品分析", "需求分析", "android开发", "ios开发", "react开发", "springCould","数据库优化","算法分析"};
        String[] courseLv = {"入门", "进阶", "实战"};
        for (int i=0;i<2;i++) {
            Department department = new Department();
            department.setDepartmentInfo(departments[i]);
            departmentService.add(department);
        }
        List<Department> departmentList = departmentService.findAll();
        for (int i=0;i<9;i++) {
            Office office = new Office();
            if (i<3) {
                office.setDepartmentNum(departmentList.get(0).getObjectId());
            } else {
                office.setDepartmentNum(departmentList.get(1).getObjectId());
            }
            office.setOfficeName(offices[i]);
            office.setOfficeInfo(offices[i]);
            officeService.add(office);
        }
        List<Office> officeList = officeService.findAll();
        for (int i=0;i<36;i++) {
            User user = new User();
            if (i == 0) {
                user.setUserType("admin");
            } else {
                user.setUserType("user");
            }
            user.setLogId("user"+i);
            user.setPwd("user"+i);
            user.setUserPower(3);
            user.setOfficeNum(officeList.get(i%9).getObjectId());
            user.setUserName(userName[i]);
            userService.add(user);
        }
        List<User> userList = userService.findAll();
        for (int i=0;i<9;i++) {
            for (int j=0;j<3;j++){
                Course course = new Course();
                course.setCourseSumLesson(4);
                course.setCoursePower(3);
                course.setAdminNum(userList.get(0).getObjectId());
                course.setCourseName(courseName[i]+courseLv[j]);
                courseService.add(course);
            }
        }
        List<Course> courseList = courseService.findAll();
        for (int i=0;i<27;i++) {
            CourseRelationOffice courseRelationOffice = new CourseRelationOffice();
            courseRelationOffice.setOfficeNum(officeList.get(i/3).getObjectId());
            courseRelationOffice.setCourseNum(courseList.get(i).getObjectId());
            courseRelationOfficeService.add(courseRelationOffice);
            for (int j=0;j<2;j++){
                Lesson lesson = new Lesson();
                lesson.setCourseNum(courseList.get(i).getObjectId());
                lesson.setLessonName("第"+(j+1)+"章-第"+1+"课时");
                lesson.setLessonOrder(j);
                lesson.setLessonInfo(courseList.get(i).getCourseName()+"课程");
                lesson.setLessonType("mp4");
                lessonService.add(lesson);
            }
        }
        for(int i=0;i<20;i++) {
            CourseRecord courseRecord = new CourseRecord();
            courseRecord.setCourseComplete(0);
            courseRecord.setUserNum(userList.get(i).getObjectId());
            courseRecord.setCourseNum(courseList.get(i).getObjectId());
            courseRecordService.add(courseRecord);
            List<Object[]> list = lessonService.findByCourseNumWithLessonRecord(courseList.get(i).getObjectId(),userList.get(i).getObjectId());
            for (int j=0;j<list.size();j++) {
                Object[]  tempobject = list.get(j);
                Lesson tempLesson = new Lesson((String)tempobject[0],(Date)tempobject[1],(Date)tempobject[2], (Date)tempobject[3],
                        (String)tempobject[4],(String)tempobject[5],(String)tempobject[6],(String)tempobject[7],(String)tempobject[8],(double)tempobject[9]);
                LessonRecord lessonRecord = new LessonRecord();
                lessonRecord.setLessonNum(tempLesson.getObjectId());
                lessonRecord.setUserNum(userList.get(i).getObjectId());
                lessonRecord.setLessonRecord("0");
                lessonRecordService.add(lessonRecord);
                Discuss discuss = new Discuss();
                discuss.setLessonNum(tempLesson.getObjectId());
                discuss.setTalkUserNum(userList.get(i).getObjectId());
                discuss.setDiscussContent("学习"+courseList.get(i).getCourseName()+"感觉用处很大");
                discussService.add(discuss);
            }
        }


        return Result.success();
    }
}
