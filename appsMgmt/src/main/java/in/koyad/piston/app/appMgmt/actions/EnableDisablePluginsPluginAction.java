/*
 * Copyright (c) 2012-2017 Shailendra Singh <shailendra_01@outlook.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package in.koyad.piston.app.appMgmt.actions;

import java.text.MessageFormat;
import java.util.Arrays;

import in.koyad.piston.app.api.annotation.AnnoPluginAction;
import in.koyad.piston.app.api.model.Request;
import in.koyad.piston.app.api.model.Response;
import in.koyad.piston.app.api.plugin.BasePluginAction;
import in.koyad.piston.app.appMgmt.forms.EnableDisablePluginsPluginForm;
import in.koyad.piston.cache.store.PortalStaticCache;
import in.koyad.piston.client.api.PortalClient;
import in.koyad.piston.common.basic.constant.FrameworkConstants;
import in.koyad.piston.common.basic.exception.FrameworkException;
import in.koyad.piston.common.util.LogUtil;
import in.koyad.piston.core.sdk.impl.PortalClientImpl;

@AnnoPluginAction(
	name = "enableDisablePlugins"
)
public class EnableDisablePluginsPluginAction extends BasePluginAction {
	
	private final PortalClient portalClient = PortalClientImpl.getInstance();

	private static final LogUtil LOGGER = LogUtil.getLogger(EnableDisablePluginsPluginAction.class);
	
	@Override
	public String execute(Request req, Response resp) throws FrameworkException {
		LOGGER.enterMethod("execute");
		
		//update data in db
		EnableDisablePluginsPluginForm form = req.getPluginForm(EnableDisablePluginsPluginForm.class);
		portalClient.enableDisablePlugins(Arrays.asList(form.getPluginIds()), form.getAction());
		
		//update data in cache
		for(String pluginId : form.getPluginIds()) {
			switch(form.getAction()) {
				case "enable":
					PortalStaticCache.plugins.get(pluginId).setEnabled(true);
				case "disable":
					PortalStaticCache.plugins.get(pluginId).setEnabled(false);
				default:
					throw new FrameworkException(MessageFormat.format("Unsupported action {0}.", form.getAction()));
			}
		}
		
		LOGGER.exitMethod("execute");
//		String nextAction = (String)RequestContextUtil.getRequestAttribute(FrameworkConstants.PISTON_PLUGIN_NEXTACTION);
		return FrameworkConstants.PREFIX_FORWARD + ListPluginsPluginAction.ACTION_NAME;
	}

}
