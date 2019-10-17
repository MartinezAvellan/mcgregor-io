package com.quarktech.mcgregor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Arquivo {
	
    int ordem();
    String descricao() default "";
    int tamanho() default 0;
    int inicio() default 0;
    int fim() default 0;
    String tipo() default "";
    String formato() default "";
    boolean obrigatorio() default false;
    
}