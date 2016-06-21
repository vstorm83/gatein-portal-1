package org.gatein.portal.idm.impl.api.session.managers;

import org.picketlink.idm.api.Group;
import org.picketlink.idm.api.User;
import org.picketlink.idm.common.exception.IdentityException;
import org.picketlink.idm.impl.api.session.IdentitySessionImpl;
import org.picketlink.idm.impl.api.session.managers.PersistenceManagerImpl;

import java.util.logging.Logger;

/**
 * Created by exo on 6/17/16.
 */
public class ExoPersistenceManagerImpl extends PersistenceManagerImpl {

    private static Logger log = Logger.getLogger(ExoPersistenceManagerImpl.class.getName());

    public ExoPersistenceManagerImpl(IdentitySessionImpl session) {
        super(session);
    }

    @Override
    public User createUser(String identityName) throws IdentityException {
        return super.createUser(identityName.toLowerCase());
    }

    @Override
    public Group createGroup(String groupName, String groupType) throws IdentityException {
        return super.createGroup(groupName.toLowerCase(), groupType);
    }

    @Override
    public String createGroupKey(String groupName, String groupType) {
        return super.createGroupKey(groupName.toLowerCase(), groupType);
    }

    @Override
    public String createUserKey(String id) {
        return id.toLowerCase();
    }



}
