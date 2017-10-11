package com.runcible.abbot.model;

import java.util.Set;

import javax.validation.ConstraintViolation;

import org.hibernate.validator.HibernateValidator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

public class ValidationTest
{

    protected void setupValidation()
    {
        localValidatorFactory = new LocalValidatorFactoryBean();
        localValidatorFactory.setProviderClass(HibernateValidator.class);
        localValidatorFactory.afterPropertiesSet();
    }

    
    protected <ModelClass> Set<ConstraintViolation<ModelClass>> validate(ModelClass m)
    {
        return localValidatorFactory.validate(m);  
    }

    protected LocalValidatorFactoryBean localValidatorFactory;
}
