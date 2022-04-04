package jee.rest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.ws.rs.NameBinding;
//namebing allows us to bind the annotation to a jaxrs interceptor provider or filter
@NameBinding
@Retention(RetentionPolicy.RUNTIME)
//element type means this annotation can be used on a method or class
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Authz {

}
