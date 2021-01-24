package com.ivini.saidasjuntas.acesso.validacao;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Retention(RUNTIME)
@Target({ TYPE, ANNOTATION_TYPE })
@Constraint(validatedBy = MesmosValoresValidator.class)
public @interface MesmosValores {
	String message();
    Class<?>[] groups() default {}; 
    Class<? extends Payload>[] payload() default {};
    String primeiro();
    String segundo();
    
    @Target({TYPE, ANNOTATION_TYPE})
    @Retention(RUNTIME)
    @Documented
    @interface Lista {
    	MesmosValores[] value();
    }
}
