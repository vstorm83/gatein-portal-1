package org.exoplatform.webui.core;

import org.exoplatform.commons.serialization.api.annotations.Serialized;
import org.exoplatform.webui.config.annotation.ComponentConfig;

@ComponentConfig(lifecycle = UIComponentDecorator.UIComponentDecoratorLifecycle.class)
@Serialized
public class UILazyTab extends UIComponentDecorator {
    @Override
    public UIComponent setUIComponent(UIComponent uicomponent) {
      UIComponent old = this.uicomponent_;
      this.uicomponent_ = uicomponent;
      return old;
    }
}
