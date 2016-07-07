package org.exoplatform.sample.webui.component;

import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.core.UIContainer;
import org.exoplatform.webui.core.lifecycle.UIFormLifecycle;

@ComponentConfig(lifecycle = UIFormLifecycle.class, template = "app:/groovy/webui/component/UISampleMentions.gtmpl")
public class UISampleMentions extends UIContainer {

    public UISampleMentions() throws Exception {
       
    }

}
