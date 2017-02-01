/*
 * Copyright (C) 2003-2017 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
package org.exoplatform.services.organization.idm.cache;

import java.io.Serializable;
import java.util.Collection;

import org.apache.commons.lang.SerializationUtils;

import org.exoplatform.services.cache.ExoCache;
import org.exoplatform.services.organization.Group;
import org.exoplatform.services.organization.Membership;
import org.exoplatform.services.organization.cache.MembershipCacheKey;
import org.exoplatform.services.organization.cache.OrganizationCacheHandler;
import org.exoplatform.services.organization.idm.MembershipDAOImpl;
import org.exoplatform.services.organization.idm.PicketLinkIDMOrganizationServiceImpl;
import org.exoplatform.services.organization.idm.PicketLinkIDMService;

public class CacheableMembershipHandlerImpl extends MembershipDAOImpl {

  private final ExoCache<Serializable, Membership> membershipCache;

  @SuppressWarnings("unchecked")
  public CacheableMembershipHandlerImpl(OrganizationCacheHandler organizationCacheHandler,
                                        PicketLinkIDMOrganizationServiceImpl orgService,
                                        PicketLinkIDMService service) {
    super(orgService, service);
    this.membershipCache = organizationCacheHandler.getMembershipCache();
  }

  /**
   * {@inheritDoc}
   */
  public Membership findMembership(String id) throws Exception {
    Membership membership = (Membership) membershipCache.get(id);
    if (membership == null) {
      membership = super.findMembership(id);
  
      if (membership != null) {
        membershipCache.put(membership.getId(), membership);
        membershipCache.put(new MembershipCacheKey(membership), membership);
      }
    }

    return membership == null ? null : (Membership) SerializationUtils.clone((Serializable) membership);
  }

  /**
   * {@inheritDoc}
   */
  public Membership findMembershipByUserGroupAndType(String userName, String groupId, String type) throws Exception {
    Membership membership = (Membership) membershipCache.get(new MembershipCacheKey(userName, groupId, type));
    if (membership == null) {
      membership = super.findMembershipByUserGroupAndType(userName, groupId, type);
  
      if (membership != null) {
        membershipCache.put(membership.getId(), membership);
        membershipCache.put(new MembershipCacheKey(membership), membership);
      }
    }

    return membership == null ? null : (Membership) SerializationUtils.clone((Serializable) membership);
  }

  /**
   * {@inheritDoc}
   */
  public Collection<Membership> findMembershipsByGroup(Group group) throws Exception {
    @SuppressWarnings("unchecked")
    Collection<Membership> memberships = super.findMembershipsByGroup(group);
    for (Membership membership : memberships) {
      membershipCache.put(membership.getId(), membership);
      membershipCache.put(new MembershipCacheKey(membership), membership);
    }

    return memberships;
  }

  /**
   * {@inheritDoc}
   */
  public Collection<Membership> findMembershipsByUser(String userName) throws Exception {
    @SuppressWarnings("unchecked")
    Collection<Membership> memberships = super.findMembershipsByUser(userName);
    for (Membership membership : memberships) {
      membershipCache.put(membership.getId(), membership);
      membershipCache.put(new MembershipCacheKey(membership), membership);
    }

    return memberships;
  }

  /**
   * {@inheritDoc}
   */
  public Collection<Membership> findMembershipsByUserAndGroup(String userName, String groupId) throws Exception {
    @SuppressWarnings("unchecked")
    Collection<Membership> memberships = super.findMembershipsByUserAndGroup(userName, groupId);
    for (Membership membership : memberships) {
      membershipCache.put(membership.getId(), membership);
      membershipCache.put(new MembershipCacheKey(membership), membership);
    }

    return memberships;
  }

  /**
   * {@inheritDoc}
   */
  public Membership removeMembership(String id, boolean broadcast) throws Exception {
    Membership membership = super.removeMembership(id, broadcast);
    if (membership != null) {
      membershipCache.remove(membership.getId());
      membershipCache.remove(new MembershipCacheKey(membership));
    }

    return membership;
  }

  /**
   * {@inheritDoc}
   */
  public Collection<Membership> removeMembershipByUser(String username, boolean broadcast) throws Exception {
    @SuppressWarnings("unchecked")
    Collection<Membership> memberships = super.removeMembershipByUser(username, broadcast);

    for (Membership membership : memberships) {
      membershipCache.remove(membership.getId());
      membershipCache.remove(new MembershipCacheKey(membership));
    }

    return memberships;
  }

}
