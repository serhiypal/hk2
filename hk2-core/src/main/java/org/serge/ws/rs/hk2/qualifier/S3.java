package org.serge.ws.rs.hk2.qualifier;

import org.glassfish.hk2.api.AnnotationLiteral;

public @interface S3 {

    class OneDriveImpl extends AnnotationLiteral<S3> implements S3{}

    S3 LITERAL = new OneDriveImpl();

}
