package com.lagou.dao.impl;

import cn.hutool.json.JSONUtil;
import com.lagou.bean.Grade;
import com.lagou.bean.Student;
import com.lagou.bean.Teacher;
import com.lagou.dao.StudentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author 15579
 * 2019/6/13 14:33
 * 文件说明：
 */
@Component("studentDao")
public class StudentDaoImpl implements StudentDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    //新增
    @Override
    public void saveStudent(Student student) {
        mongoTemplate.save(student);
    }

    //新增
    @Override
    public void saveGrade(Grade grade) {
        mongoTemplate.save(grade);
    }

    //新增
    @Override
    public void saveTeacher(Teacher teacher) {
        mongoTemplate.save(teacher);
    }

    //删除
    @Override
    public void removeStudent(Integer id) {
        Query query=new Query(Criteria.where("_id").is(id));
        mongoTemplate.remove(query,Student.class);
    }

    //修改
    @Override
    public void updateStudent(Student student) {
        Query query=new Query(Criteria.where("_id").is(student.getId()));
        Update update=new Update();
        update.set("name",student.getName());
        update.set("sex",student.getSex());
        update.set("gradeId",student.getGradeId());
        mongoTemplate.updateFirst(query,update,Student.class);
    }

    //根据编号查询
    @Override
    public Student findById(Integer id) {
        Query query=new Query(Criteria.where("_id").is(id));//可累加条件
        Student student = mongoTemplate.findOne(query, Student.class);
        return student;
    }

    //查询所有
    @Override
    public List<Student> findAll() {
        return mongoTemplate.findAll(Student.class);
    }

    /**
     * 多表表联查
     *
     * @return
     */
    @Override
    public Object findStudentAndGrade() {
        LookupOperation lookupOperation=LookupOperation.newLookup().
                from("grade").  //关联从表名
                localField("gradeId").     //主表关联字段
                foreignField("_id").//从表关联的字段
                as("grade");   //查询结果名
        LookupOperation lookupOperation2=LookupOperation.newLookup().
                from("teacher").
                localField("teacherId").     //主表关联字段
                foreignField("_id").//从表关联的字段
                as("teacher");   //查询结果名
//带条件查询可以选择添加下面的条件
 //       AggregationOperation match1= Aggregation.match(qqq);
//        AggregationOperation match = Aggregation.match(criteria);
//        Aggregation counts = Aggregation.newAggregation(match1,loxzokupOperation,match);
        Criteria criteria=Criteria.where("_id").is("def9cacb-7572-4fc1-a1f0-11c9f99d46b8");
        AggregationOperation match = Aggregation.match(criteria);
        SkipOperation skip = Aggregation.skip(3l); // 分页
        LimitOperation limit = Aggregation.limit(10); // 分页
        Aggregation aggregation=Aggregation.newAggregation(lookupOperation,lookupOperation2,skip,limit);
        AggregationResults<Student> aggregate = mongoTemplate.aggregate(aggregation, Student.class, Student.class);
        List<Student> mappedResults = aggregate.getMappedResults();
//        List<Map> results = mongoTemplate.aggregate(aggregation,"student", Map.class).getMappedResults();
        //上面的student必须是查询的主表名
        System.out.println(JSONUtil.toJsonStr(mappedResults));
        return mappedResults;
    }
}
