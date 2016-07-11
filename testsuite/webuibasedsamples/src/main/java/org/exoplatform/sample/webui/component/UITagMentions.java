package org.exoplatform.sample.webui.component;

import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.core.UIContainer;
import org.exoplatform.webui.core.lifecycle.UIFormLifecycle;

@ComponentConfig(lifecycle = UIFormLifecycle.class, template = "app:/groovy/webui/component/UITagMentions.gtmpl")
public class UITagMentions extends UIContainer {

    public UITagMentions() throws Exception {
       
    }

}
