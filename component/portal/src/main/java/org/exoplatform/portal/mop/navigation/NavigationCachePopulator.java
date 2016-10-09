package org.exoplatform.portal.mop.navigation;

import static org.exoplatform.portal.mop.Utils.objectType;

import java.util.Collection;

import org.gatein.common.logging.Logger;
import org.gatein.common.logging.LoggerFactory;
import org.gatein.mop.api.workspace.ObjectType;
import org.gatein.mop.api.workspace.Site;
import org.picocontainer.Startable;

import org.exoplatform.commons.chromattic.ChromatticManager;
import org.exoplatform.container.component.RequestLifeCycle;
import org.exoplatform.portal.config.UserPortalConfigService;
import org.exoplatform.portal.mop.SiteKey;
import org.exoplatform.portal.mop.SiteType;
import org.exoplatform.portal.pom.config.POMSession;
import org.exoplatform.portal.pom.config.POMSessionManager;

public class NavigationCachePopulator implements Startable {
  private final Logger      log = LoggerFactory.getLogger(NavigationCachePopulator.class);

  private POMSessionManager manager;

  private ChromatticManager chromatticManager;

  private NavigationService navigationService;

  public NavigationCachePopulator(UserPortalConfigService userPortalConfigService,
                                  POMSessionManager manager,
                                  ChromatticManager chromatticManager,
                                  NavigationService navigationService) {
    this.chromatticManager = chromatticManager;
    this.manager = manager;
    this.navigationService = navigationService;
    // UserPortalConfigService added to make sure that it's started before using sessions
  }

  @Override
  public void start() {
    new Thread(new Runnable() {
      @Override
      public void run() {
        RequestLifeCycle.begin(chromatticManager);
        try {
          POMSession session = manager.getSession();

          log.info("loading portal navigations");

          ObjectType<Site> objectType = objectType(SiteType.PORTAL);
          Collection<Site> sites = session.getWorkspace().getSites(objectType);
          for (Site site : sites) {
            SiteKey key = SiteKey.portal(site.getName());
            navigationService.loadNavigation(key);
          }

          log.info("finished loading portal navigations");

          log.info("loading spaces & groups navigations");
          objectType = objectType(SiteType.GROUP);
          sites = session.getWorkspace().getSites(objectType);
          for (Site site : sites) {
            SiteKey key = SiteKey.group(site.getName());
            navigationService.loadNavigation(key);
          }
          log.info("finished loading group navigations");
        } finally {
          RequestLifeCycle.end();
        }
      }
    }, "GatenNavigationLoader").start();
  }

  @Override
  public void stop() {
  }

}
