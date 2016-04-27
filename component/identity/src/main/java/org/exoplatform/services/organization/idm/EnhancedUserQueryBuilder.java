package org.exoplatform.services.organization.idm;

import java.util.Set;

import org.picketlink.idm.api.query.UserQueryBuilder;

import org.exoplatform.services.organization.Membership;

public interface EnhancedUserQueryBuilder extends UserQueryBuilder {
  public Set<Membership> getMembership();

  public void addMembership(Set<Membership> Membership);
}
