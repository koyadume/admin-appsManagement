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

import org.koyad.piston.business.model.Plugin;

import in.koyad.piston.app.api.annotation.AnnoPluginAction;
import in.koyad.piston.app.api.model.Request;
import in.koyad.piston.app.api.plugin.BasePluginAction;
import in.koyad.piston.app.appMgmt.forms.PluginDetailsPluginForm;
import in.koyad.piston.app.appMgmt.utils.ModelGenerator;
import in.koyad.piston.cache.store.PortalCache;
import in.koyad.piston.client.api.PortalClient;
import in.koyad.piston.common.basic.exception.FrameworkException;
import in.koyad.piston.common.constants.Messages;
import in.koyad.piston.common.constants.MsgType;
import in.koyad.piston.common.util.LogUtil;
import in.koyad.piston.common.util.Message;
import in.koyad.piston.core.sdk.impl.PortalClientImpl;

@AnnoPluginAction(
	name = SavePluginPluginAction.ACTION_NAME
)
/**
 * This action is used to update permissions for a plugin. 
 */
public class SavePluginPluginAction extends BasePluginAction {
	
	public static final String ACTION_NAME = "savePlugin";
	
	private final PortalClient portalClient = PortalClientImpl.getInstance();

	private static final LogUtil LOGGER = LogUtil.getLogger(SavePluginPluginAction.class);
	
	@Override
	public String execute(Request req) throws FrameworkException {
		LOGGER.enterMethod("execute");
		
		try {
			//update data in db
			PluginDetailsPluginForm form = req.getPluginForm(PluginDetailsPluginForm.class);
			Plugin newData = ModelGenerator.getPlugin(form);
			portalClient.updatePlugin(newData);
			
			//update version in form
			form.setVersion(newData.getVersion());
			
			//update data in cache
			Plugin oldData = PortalCache.plugins.get(newData.getId());
			oldData.refresh(newData);
			
			req.setAttribute(PluginDetailsPluginForm.FORM_NAME, form);
			
			if(null == form.getId()) {
				req.setAttribute("msg", new Message(MsgType.INFO, MessageFormat.format(Messages.RESOURCE_CREATED_SUCCESSFULLY, "Plugin")));
			} else {
				req.setAttribute("msg", new Message(MsgType.INFO, MessageFormat.format(Messages.RESOURCE_UPDATED_SUCCESSFULLY, "Plugin")));
			}
		} catch(FrameworkException ex) {
			LOGGER.logException(ex);
			req.setAttribute("msg", new Message(MsgType.ERROR, "Error occured while updating plugin details."));
		}
		
		LOGGER.exitMethod("execute");
		return "/pages/pluginDetails.xml";
	}

}
