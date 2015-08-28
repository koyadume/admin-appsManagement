/*
 * Copyright (c) 2012-2015 Shailendra Singh <shailendra_01@outlook.com>
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

import in.koyad.piston.app.appMgmt.forms.EnableDisableAppsPluginForm;
import in.koyad.piston.common.constants.FrameworkConstants;
import in.koyad.piston.common.exceptions.FrameworkException;
import in.koyad.piston.common.utils.LogUtil;
import in.koyad.piston.common.utils.ServiceManager;
import in.koyad.piston.controller.plugin.PluginAction;
import in.koyad.piston.controller.plugin.annotations.AnnoPluginAction;
import in.koyad.piston.core.sdk.api.PortalService;
import in.koyad.piston.servicedelegate.model.PistonModelCache;
import in.koyad.piston.ui.utils.FormUtils;

@AnnoPluginAction(
	name = EnableDisableAppsPluginAction.ACTION_NAME
)
public class EnableDisableAppsPluginAction extends PluginAction {
	
	public static final String ACTION_NAME = "enableDisableApps";
	
	private final PortalService portalService = ServiceManager.getService(PortalService.class);

	private static final LogUtil LOGGER = LogUtil.getLogger(EnableDisableAppsPluginAction.class);
	
	@Override
	protected String execute() throws FrameworkException {
		LOGGER.enterMethod("execute");
		
		//update data in db
		EnableDisableAppsPluginForm form = FormUtils.createFormWithReqParams(EnableDisableAppsPluginForm.class);
		portalService.enableDisableApps(Arrays.asList(form.getAppIds()), form.getAction());
		
		////update data in cache
		for(String appId : form.getAppIds()) {
			switch(form.getAction()) {
				case "enable":
					PistonModelCache.apps.get(appId).setEnabled(true);
					break;
				case "disable":
					PistonModelCache.apps.get(appId).setEnabled(false);
					break;
				default:
					throw new FrameworkException(MessageFormat.format("Unsupported action {0}.", form.getAction()));
			}
		}
		
		LOGGER.exitMethod("execute");
//		String nextAction = (String)RequestContextUtil.getRequestAttribute(FrameworkConstants.PISTON_PLUGIN_NEXTACTION);
		return FrameworkConstants.PREFIX_FORWARD + ListAppsPluginAction.ACTION_NAME;
	}

}
