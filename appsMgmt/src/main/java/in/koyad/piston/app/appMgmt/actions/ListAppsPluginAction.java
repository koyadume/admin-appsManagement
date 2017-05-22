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

import java.util.List;

import org.koyad.piston.business.model.App;

import in.koyad.piston.app.api.annotation.AnnoPluginAction;
import in.koyad.piston.app.api.model.Request;
import in.koyad.piston.app.api.plugin.BasePluginAction;
import in.koyad.piston.cache.store.PortalCache;
import in.koyad.piston.common.basic.exception.FrameworkException;
import in.koyad.piston.common.util.LogUtil;

@AnnoPluginAction(
	name = ListAppsPluginAction.ACTION_NAME
)
public class ListAppsPluginAction extends BasePluginAction {

	public static final String ACTION_NAME = "listApps";
	
	private static final LogUtil LOGGER = LogUtil.getLogger(ListAppsPluginAction.class);
	
	@Override
	public String execute(Request req) throws FrameworkException {
		LOGGER.enterMethod("execute");
		
		List<App> apps = PortalCache.apps.values();
		req.setAttribute("apps", apps);
		
		LOGGER.exitMethod("execute");
		return "/pages/apps.xml";
	}

}
