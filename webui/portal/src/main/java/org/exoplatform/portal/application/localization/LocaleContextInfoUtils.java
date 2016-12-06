package org.exoplatform.portal.application.localization;

import org.exoplatform.portal.config.UserACL;
import org.exoplatform.portal.config.UserPortalConfig;
import org.exoplatform.portal.config.UserPortalConfigService;
import org.exoplatform.portal.config.model.PortalConfig;
import org.exoplatform.services.resources.LocaleConfig;
import org.exoplatform.services.resources.LocaleConfigService;
import org.exoplatform.services.resources.LocaleContextInfo;
import org.gatein.common.i18n.LocaleFactory;
import org.exoplatform.commons.utils.CommonsUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * This class is used to ease {@link LocaleContextInfo} object build
 */
public class LocaleContextInfoUtils {
  
  private static final String PREV_LOCALE_SESSION_ATTR = "org.gatein.LAST_LOCALE";
  
  /**
   *  Helper method for setters invocation on {@link LocaleContextInfo} object
   * @param request
   * @return a built {@link LocaleContextInfo} object
   * @throws Exception
   */
  public static LocaleContextInfo buildLocaleContextInfo(HttpServletRequest request) throws Exception {
    // get user portal locale
    String username = request.getRemoteUser();
    if (username == null) {
      UserACL userACL = CommonsUtils.getService(UserACL.class);
      username = userACL.getSuperUser();
    }
    UserPortalConfigService userPortalConfigService = CommonsUtils.getService(UserPortalConfigService.class);
    UserPortalConfig userPortalConfig = userPortalConfigService.getUserPortalConfig(userPortalConfigService.getDefaultPortal(), username);
    Locale portalLocale = null;
    if (userPortalConfig != null) {
      PortalConfig pConfig = userPortalConfig.getPortalConfig();
      if (pConfig != null)
        portalLocale = LocaleFactory.DEFAULT_FACTORY.createLocale(pConfig.getLocale());
    }
    //
    LocaleConfigService localeConfigService = CommonsUtils.getService(LocaleConfigService.class);
    Set<Locale> supportedLocales = new HashSet();
    for (LocaleConfig lc : localeConfigService.getLocalConfigs()) {
      supportedLocales.add(lc.getLocale());
    }
    // get session locale
    String lastLocaleLangauge = request.getSession().getAttribute(PREV_LOCALE_SESSION_ATTR).toString();
    Locale sessionLocale = lastLocaleLangauge == null ? LocalizationLifecycle.getSessionLocale(request) : new Locale(lastLocaleLangauge);
    //
    LocaleContextInfo localeCtx = new LocaleContextInfo();
    localeCtx.setSupportedLocales(supportedLocales);
    localeCtx.setBrowserLocales(Collections.list(request.getLocales()));
    localeCtx.setCookieLocales(LocalizationLifecycle.getCookieLocales(request));
    localeCtx.setSessionLocale(sessionLocale);
    localeCtx.setRemoteUser(request.getRemoteUser());
    localeCtx.setPortalLocale(portalLocale);
    return localeCtx;
  }
}
