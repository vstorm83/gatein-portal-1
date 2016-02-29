/**
 * Copyright (C) 2009 eXo Platform SAS.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.exoplatform.services.resources.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

import org.picocontainer.Startable;

import org.exoplatform.commons.cache.future.FutureCache;
import org.exoplatform.commons.cache.future.FutureExoCache;
import org.exoplatform.commons.cache.future.Loader;
import org.exoplatform.commons.utils.IOUtil;
import org.exoplatform.commons.utils.MapResourceBundle;
import org.exoplatform.commons.utils.PropertyManager;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.xml.InitParams;
import org.exoplatform.services.cache.ExoCache;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.resources.*;

/**
 * Created by The eXo Platform SAS Mar 9, 2007
 */
public abstract class BaseResourceBundleService implements ResourceBundleService, Startable {

    protected Log log_;

    protected volatile List<String> resourceBundleNames_;

    protected LocaleConfigService localeService_;

    protected ExoCache<String, ResourceBundle> cache_;

    private volatile FutureCache<String, ResourceBundle, ResourceBundleContext> futureCache_;

    private final Loader<String, ResourceBundle, ResourceBundleContext> loader_ = new Loader<String, ResourceBundle, ResourceBundleContext>() {
        /**
         * {@inheritDoc}
         */
        public ResourceBundle retrieve(ResourceBundleContext context, String key) throws Exception {
            return context.get(key);
        }
    };

    private volatile List<String> initResources_;

    @SuppressWarnings("unchecked")
    protected void initParams(InitParams params) {
        resourceBundleNames_ = params.getValuesParam("classpath.resources").getValues();

        // resources name can use for portlets
        resourceBundleNames_.addAll(params.getValuesParam("portal.resource.names").getValues());

        resourceBundleNames_.addAll(params.getValuesParam("init.resources").getValues());
    }

    /**
     * Add new resources bundles
     */
    public synchronized void addResourceBundle(BaseResourceBundlePlugin plugin) {
        List<String> classpathResources = plugin.getClasspathResources();
        if (classpathResources != null && !classpathResources.isEmpty()) {
            List<String> result = new ArrayList<String>(classpathResources);
            if (resourceBundleNames_ != null) {
                result.addAll(resourceBundleNames_);
            }
            this.resourceBundleNames_ = Collections.unmodifiableList(result);
        }
        List<String> portalResources = plugin.getPortalResources();
        if (portalResources != null && !portalResources.isEmpty()) {
            List<String> result = new ArrayList<String>(portalResources);
            if (resourceBundleNames_ != null) {
                result.addAll(resourceBundleNames_);
            }
            this.resourceBundleNames_ = Collections.unmodifiableList(result);
        }
        List<String> initResources = plugin.getInitResources();
        if (initResources != null && !initResources.isEmpty()) {
            List<String> result = new ArrayList<String>(initResources);
            if (resourceBundleNames_ != null) {
                result.addAll(resourceBundleNames_);
            }
            this.resourceBundleNames_ = Collections.unmodifiableList(result);
        }
    }

    /**
     * Loads all the "init" resource bundles
     *
     * @see org.picocontainer.Startable#start()
     */
    public void start() {
    }

    /**
     * @see org.picocontainer.Startable#stop()
     */
    public void stop() {
    }

    public ResourceBundle getResourceBundle(String[] name, Locale locale) {
        ClassLoader cl = PortalContainer.getInstance().getPortalClassLoader();
        return getResourceBundle(name, locale, cl);
    }

    public ResourceBundle getResourceBundle(String name, Locale locale) {
        ClassLoader cl = PortalContainer.getInstance().getPortalClassLoader();
        return getResourceBundle(name, locale, cl);
    }

    public String[] getSharedResourceBundleNames() {
        return resourceBundleNames_.toArray(new String[resourceBundleNames_.size()]);
    }

    public ResourceBundleData createResourceBundleDataInstance() {
        return new ResourceBundleData();
    }

    protected String getResourceBundleContent(String name, String language, String defaultLang, ClassLoader cl)
            throws Exception {
        String fileName = null;
        try {
            cl = new PropertiesClassLoader(cl, true);
            fileName = name + "_" + language + ".properties";
            URL url = cl.getResource(fileName);
            if (url == null && defaultLang.equals(language)) {
                url = cl.getResource(name + ".properties");
            }
            if (url != null) {
                InputStream is = url.openStream();
                try {
                    byte[] buf = IOUtil.getStreamContentAsBytes(is);
                    return new String(buf, "UTF-8");
                } finally {
                    try {
                        is.close();
                    } catch (IOException e) {
                        // Do nothing
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception("Error while reading the file: " + fileName, e);
        }
        return null;
    }

    /**
     * This method is used for country support
     *
     * @param baseName
     * @param locale
     * @param defaultLocale
     * @param cl
     * @return
     */
    protected String getResourceBundleContent(String baseName, Locale locale, Locale defaultLocale, ClassLoader cl)
            throws Exception {
        List<String> candidateFiles = new ArrayList<String>();

        String language = locale.getLanguage();
        String country = locale.getCountry().toUpperCase();
        String variant = locale.getVariant();

        String defaultLanguage = defaultLocale.getLanguage();
        String defaultCountry = defaultLocale.getCountry().toUpperCase();
        String defaultVariant = defaultLocale.getVariant();

        if (variant != null && variant.length() > 0) {
            candidateFiles.add(baseName + "_" + language + "_" + country + "_" + variant + ".properties");
        }

        if (country != null && country.length() > 0) {
            candidateFiles.add(baseName + "_" + language + "_" + country + ".properties");
        }

        if (language != null && language.length() > 0) {
            candidateFiles.add(baseName + "_" + language + ".properties");
        }

        if (defaultVariant != null && defaultVariant.length() > 0) {
            candidateFiles.add(baseName + "_" + defaultLanguage + "_" + defaultCountry + "_" + defaultVariant + ".properties");
        }

        if (defaultCountry != null && defaultCountry.length() > 0) {
            candidateFiles.add(baseName + "_" + defaultLanguage + "_" + defaultCountry + ".properties");
        }

        if (defaultLanguage != null && defaultLanguage.length() > 0) {
            candidateFiles.add(baseName + "_" + defaultLanguage + ".properties");
        }

        candidateFiles.add(baseName + ".properties");

        //fallback to en locale help to remove ant script that generates base properties file
        candidateFiles.add(baseName + "_" + Locale.ENGLISH.getLanguage() + ".properties");

        cl = new PropertiesClassLoader(cl, true);
        String fileName = null;

        try {
            URL url = null;
            for (String candidateFile : candidateFiles) {
                url = cl.getResource(candidateFile);
                if (url != null) {
                    fileName = candidateFile;
                    break;
                }
            }

            if (url != null) {
                InputStream is = url.openStream();
                try {
                    byte[] buf = IOUtil.getStreamContentAsBytes(is);
                    return new String(buf, "UTF-8");
                } finally {
                    try {
                        is.close();
                    } catch (IOException e) {
                        // Do nothing
                    }
                }
            }
        } catch (Exception ex) {
            throw new Exception("Error while reading the file: " + fileName, ex);
        }

        return null;

    }

    /**
     * Invalidate an entry in the cache at this level. Normally this is called by the subclass.
     *
     * @param name the bundle name
     */
    protected final void invalidate(String name) {
        cache_.remove(name);
    }

    public ResourceBundle getResourceBundle(String name, Locale locale, ClassLoader cl) {
        if (IdentityResourceBundle.MAGIC_LANGUAGE.equals(locale.getLanguage())) {
            return IdentityResourceBundle.getInstance();
        }

        String country = locale.getCountry();
        String variant = locale.getVariant();
        String id;
        if (variant != null && variant.length() > 0) {
            id = name + "_" + locale.getLanguage() + "_" + country + "_" + variant;
        } else if (country != null && country.length() > 0) {
            id = name + "_" + locale.getLanguage() + "_" + locale.getCountry();
        } else {
            id = name + "_" + locale.getLanguage();
        }

        boolean isCacheable = !PropertyManager.isDevelopping();
        if (isCacheable) {
            // Avoid naming collision
            id += "_" + cl.getClass() + "_" + System.identityHashCode(cl);
        }

        // Case 1: ResourceBundle of portlets, standard java API is used
        ResourceBundleFromCPContext ctx = new ResourceBundleFromCPContext(name, locale, cl);
        ResourceBundle result;
        // Cache classpath resource bundle while running portal in non-dev mode
        if (isCacheable) {
            result = getFutureCache().get(ctx, id);
        } else {
            result = ctx.get(id);
        }

        if (ctx.e != null) {
            // Throw the RuntimeException if it occurs to remain compatible with the old behavior
            throw ctx.e;
        } else {
            return result;
        }
    }

    public ResourceBundle getResourceBundle(String[] name, Locale locale, ClassLoader cl) {
        if (IdentityResourceBundle.MAGIC_LANGUAGE.equals(locale.getLanguage())) {
            return IdentityResourceBundle.getInstance();
        }
        StringBuilder idBuf = new StringBuilder("merge:");
        for (String n : name)
            idBuf.append(n).append("_");
        idBuf.append(locale);
        String id = idBuf.toString();
        return getFutureCache().get(new GetResourceBundleContext(name, locale, cl), id);
    }

    protected FutureCache<String, ResourceBundle, ResourceBundleContext> getFutureCache() {
        if (futureCache_ == null) {
            synchronized (this) {
                if (futureCache_ == null) {
                    futureCache_ = new FutureExoCache<String, ResourceBundle, ResourceBundleContext>(loader_, cache_);
                }
            }
        }
        return futureCache_;
    }

    /**
     * Generic class defining a context needed to get a ResourceBundle
     */
    private abstract static class ResourceBundleContext {
        /**
         * Get the resource bundle corresponding to the context
         */
        abstract ResourceBundle get(String id);
    }

    /**
     * The class defining the context required to load a ResourceBundle from classpath
     */
    private  class ResourceBundleFromCPContext extends ResourceBundleContext {
        private final String name;

        private final Locale locale;

        private final ClassLoader cl;

        private RuntimeException e;

        public ResourceBundleFromCPContext(String name, Locale locale, ClassLoader cl) {
            this.name = name;
            this.locale = locale;
            this.cl = cl;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        ResourceBundle get(String id) {
            ResourceBundle parent =  null, result = null;
            try {
                Locale defaultLocale = localeService_.getDefaultLocaleConfig().getLocale();

                String rootId = name + "_" + defaultLocale.getLanguage();
                parent = getContent(rootId, null, defaultLocale);
                result = getContent(name, parent, defaultLocale);
            } catch (Exception e) {
                this.e = new RuntimeException(e);
            }

            if (result != null) {
                return result;
            } else {
                return parent;
            }
        }

        private ResourceBundle getContent(String _name, ResourceBundle parent, Locale defaultLocale) throws Exception {
            String content = getResourceBundleContent(_name.replace('.', '/'), locale, defaultLocale, cl);
            if (content != null) {
                ResourceBundleData data = new ResourceBundleData();
                data.setName(_name);
                data.setLanguage(locale.getLanguage());
                data.setCountry(locale.getCountry());
                data.setVariant(locale.getVariant());
                data.setData(content);

                return new ExoResourceBundle(data, parent);
            }
            return null;
        }
    }

    /**
     * The class defining the context required to load a ResourceBundle thanks to the method
     * <code>getResourceBundle(String[] name, Locale locale, ClassLoader cl)</code>
     */
    private class GetResourceBundleContext extends ResourceBundleContext {
        private final String[] name;

        private final Locale locale;

        private final ClassLoader cl;

        public GetResourceBundleContext(String[] name, Locale locale, ClassLoader cl) {
            this.name = name;
            this.locale = locale;
            this.cl = cl;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        ResourceBundle get(String id) {
            MapResourceBundle outputBundled = null;
            try {
                outputBundled = new MapResourceBundle(locale);
                for (int i = 0; i < name.length; i++) {
                    ResourceBundle temp = getResourceBundle(name[i], locale, cl);
                    if (temp != null) {
                        outputBundled.merge(temp);
                        continue;
                    }
                    log_.warn("Cannot load and merge the bundle: " + name[i]);
                }
                outputBundled.resolveDependencies();
            } catch (Exception ex) {
                log_.error("Cannot load and merge the bundle: " + id, ex);
            }
            return outputBundled;
        }
    }
}
