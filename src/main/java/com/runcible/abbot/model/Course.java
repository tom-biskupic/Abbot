package com.runcible.abbot.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="COURSES")
public class Course implements Cloneable, ModelWithId
{
    public Course(Integer courseNumber, String courseName, Float courseLength)
    {
        this.courseNumber = courseNumber;
        this.courseDescription = courseName;
        this.courseLength = courseLength;
    }
    
    public Course clone()
    {
        return new Course(
                this.courseNumber,
                this.courseDescription,
                this.courseLength);
    }
    
    public Course() 
    {
        
	}

	@Id
    @GeneratedValue
    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    @Column(name="COURSE_NUMBER")
    public Integer getCourseNumber()
    {
        return courseNumber;
    }
    
    public void setCourseNumber(Integer courseNumber)
    {
        this.courseNumber = courseNumber;
    }
    
    @Column(name="COURSE_NAME")
    public String getCourseDescription()
    {
        return courseDescription;
    }
    
    public void setCourseDescription(String courseDescription)
    {
        this.courseDescription = courseDescription;
    }
    
    @Column(name="COURSE_LENGTH")
    public Float getCourseLength()
    {
        return courseLength;
    }

    public void setCourseLength(Float courseLength)
    {
        this.courseLength = courseLength;
    }


    private Integer id;
    private Integer courseNumber=0;
    private String  courseDescription=new String();
    private Float   courseLength=0.0f;
}
