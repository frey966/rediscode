package com.lagou.dao;

import com.lagou.bean.Grade;
import com.lagou.bean.Student;
import com.lagou.bean.Teacher;

import java.util.List;

/**
 * @author 15579
 * 2019/6/13 14:28
 * 文件说明：
 */
public interface StudentDao {
    void saveTeacher(Teacher teacher);
    void saveGrade(Grade student);
    //新增
    void saveStudent(Student student);

    //删除
    void removeStudent(Integer id);

    //修改
    void updateStudent(Student student);

    //根据编号查询
    Student findById(Integer id);

    //查询所有
    List<Student> findAll();

    /**
     * 多表联查
     * @return
     */
    Object findStudentAndGrade();
}

