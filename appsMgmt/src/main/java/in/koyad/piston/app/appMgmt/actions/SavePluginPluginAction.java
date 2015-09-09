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

import org.koyad.piston.core.model.Plugin;

import in.koyad.piston.app.appMgmt.forms.PluginDetailsPluginForm;
import in.koyad.piston.app.appMgmt.utils.ModelGenerator;
import in.koyad.piston.common.constants.Messages;
import in.koyad.piston.common.constants.MsgType;
import in.koyad.piston.common.exceptions.FrameworkException;
import in.koyad.piston.common.utils.LogUtil;
import in.koyad.piston.common.utils.Message;
import in.koyad.piston.controller.plugin.PluginAction;
import in.koyad.piston.controller.plugin.annotations.AnnoPluginAction;
import in.koyad.piston.core.sdk.api.PortalService;
import in.koyad.piston.core.sdk.impl.PortalImpl;
import in.koyad.piston.servicedelegate.model.PistonModelCache;
import in.koyad.piston.ui.utils.FormUtils;
import in.koyad.piston.ui.utils.RequestContextUtil;

@AnnoPluginAction(
	name = SavePluginPluginAction.ACTION_NAME
)
/**
 * This action is used to update permissions for a plugin. 
 */
public class SavePluginPluginAction extends PluginAction {
	
	public static final String ACTION_NAME = "savePlugin";
	
	private final PortalService portalService = new PortalImpl();

	private static final LogUtil LOGGER = LogUtil.getLogger(SavePluginPluginAction.class);
	
	@Override
	protected String execute() throws FrameworkException {
		LOGGER.enterMethod("execute");
		
		try {
			//update data in db
			PluginDetailsPluginForm form = FormUtils.createFormWithReqParams(PluginDetailsPluginForm.class);
			Plugin newData = ModelGenerator.getPlugin(form);
			portalService.updatePlugin(newData);
			
			//update version in form
			form.setVersion(newData.getVersion());
			
			//update data in cache
			Plugin oldData = PistonModelCache.plugins.get(newData.getId());
			oldData.refresh(newData);
			
			RequestContextUtil.setRequestAttribute(PluginDetailsPluginForm.FORM_NAME, form);
			
			if(null == form.getId()) {
				RequestContextUtil.setRequestAttribute("msg", new Message(MsgType.INFO, MessageFormat.format(Messages.RESOURCE_CREATED_SUCCESSFULLY, "Plugin")));
			} else {
				RequestContextUtil.setRequestAttribute("msg", new Message(MsgType.INFO, MessageFormat.format(Messages.RESOURCE_UPDATED_SUCCESSFULLY, "Plugin")));
			}
		} catch(FrameworkException ex) {
			LOGGER.logException(ex);
			RequestContextUtil.setRequestAttribute("msg", new Message(MsgType.ERROR, "Error occured while updating plugin details."));
		}
		
		LOGGER.exitMethod("execute");
		return "/pages/pluginDetails.xml";
	}

}
