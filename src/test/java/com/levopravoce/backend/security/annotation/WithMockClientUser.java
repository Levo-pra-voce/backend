package com.levopravoce.backend.security.annotation;

import com.levopravoce.backend.security.WithMockClientUserSecurityContextFactory;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockClientUserSecurityContextFactory.class)
public @interface WithMockClientUser {
}
