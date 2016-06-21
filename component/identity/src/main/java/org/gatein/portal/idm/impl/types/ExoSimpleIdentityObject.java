package org.gatein.portal.idm.impl.types;

import org.picketlink.idm.impl.types.SimpleIdentityObject;
import org.picketlink.idm.spi.model.IdentityObject;
import org.picketlink.idm.spi.model.IdentityObjectType;

/**
 * Created by exo on 6/17/16.
 */
public class ExoSimpleIdentityObject extends SimpleIdentityObject {

    public ExoSimpleIdentityObject(String name, String id, IdentityObjectType type) {
        super(name.toLowerCase(), id, type);
    }

    public ExoSimpleIdentityObject(String name, IdentityObjectType type) {
        super(name.toLowerCase(), type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof IdentityObject))
        {
            return false;
        }

        IdentityObject that = (IdentityObject)o;

        if (super.getName() != null ? !super.getName().equalsIgnoreCase(that.getName()) : that.getName() != null)
        {
            return false;
        }
        if (super.getIdentityType() != null ? !super.getIdentityType().equals(that.getIdentityType()) : that.getIdentityType() != null)
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {

        int result = super.getName() != null ? super.getName().toLowerCase().hashCode() : 0;
        result = 31 * result + (super.getIdentityType() != null ? super.getIdentityType().hashCode() : 0);
        return result;
    }
}
