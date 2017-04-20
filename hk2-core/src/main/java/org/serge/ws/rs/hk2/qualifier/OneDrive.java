package org.serge.ws.rs.hk2.qualifier;

import org.glassfish.hk2.api.AnnotationLiteral;

public @interface OneDrive {

    class OneDriveImpl extends AnnotationLiteral<OneDrive> implements OneDrive{}

    OneDrive LITERAL = new OneDriveImpl();

}
