package org.gatein.portal.idm.impl.model.ldap;

import org.picketlink.idm.impl.model.ldap.LDAPIdentityObjectImpl;
import org.picketlink.idm.spi.model.IdentityObject;
import org.picketlink.idm.spi.model.IdentityObjectType;

/**
 * Created by exo on 6/17/16.
 */
public class ExoLDAPIdentityObjectImpl extends LDAPIdentityObjectImpl {

    public ExoLDAPIdentityObjectImpl(String dn, String id, IdentityObjectType type) {
        super(dn, id.toLowerCase(), type);
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

        if (super.getId() != null ? !super.getId().equalsIgnoreCase(that.getName()) : that.getName() != null)
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
        int result = super.getId() != null ? super.getId().toLowerCase().hashCode() : 0;
        result = 31 * result + (super.getIdentityType() != null ? super.getIdentityType().hashCode() : 0);
        return result;
    }
}
