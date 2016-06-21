package org.gatein.portal.idm.impl.model.hibernate;

import org.picketlink.idm.impl.model.hibernate.HibernateIdentityObject;
import org.picketlink.idm.impl.model.hibernate.HibernateIdentityObjectType;
import org.picketlink.idm.impl.model.hibernate.HibernateRealm;
import org.picketlink.idm.spi.model.IdentityObject;

/**
 * Created by exo on 6/17/16.
 */
public class ExoHibernateIdentityObject extends HibernateIdentityObject {

    public ExoHibernateIdentityObject() {

    }

    public ExoHibernateIdentityObject(String name, HibernateIdentityObjectType identityType, HibernateRealm realm) {
        super(name.toLowerCase(), identityType, realm);
    }

    @Override
    public void setName(String name) {
        super.setName(name.toLowerCase());
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
    public int hashCode()
    {
        int result = super.getName() != null ? super.getName().toLowerCase().hashCode() : 0;
        result = 31 * result + (super.getIdentityType() != null ? super.getIdentityType().hashCode() : 0);
        return result;
    }
}
