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
package in.koyad.piston.app.appMgmt.utils;

import java.util.ArrayList;
import java.util.List;

import org.koyad.piston.business.model.App;
import org.koyad.piston.business.model.Plugin;
import org.koyad.piston.business.model.SecurityAcl;
import org.koyad.piston.business.model.enums.RoleType;

import in.koyad.piston.app.appMgmt.forms.AppDetailsPluginForm;
import in.koyad.piston.app.appMgmt.forms.PluginDetailsPluginForm;
import in.koyad.piston.app.appMgmt.forms.ResPluginForm;
import in.koyad.piston.common.util.BeanPropertyUtils;
import in.koyad.piston.common.util.LogUtil;

public class PopulateFormUtil {

	private static final LogUtil LOGGER = LogUtil.getLogger(PopulateFormUtil.class);
	
	public static void populateAppDetails(AppDetailsPluginForm appDetailsPluginForm, App app) {
		//copy id, name etc.
		BeanPropertyUtils.copyProperties(appDetailsPluginForm, app);
		
		//copy permissions
		copyAcls(app.getAcls(), appDetailsPluginForm);
	}
	
	public static void populatePluginDetails(PluginDetailsPluginForm editPluginForm, Plugin plugin) {
		//copy id, name etc.
		BeanPropertyUtils.copyProperties(editPluginForm, plugin);

		//set appId
//		editPluginForm.setAppId(plugin.getApp().getId());
		
		//copy permissions
		copyAcls(plugin.getAcls(), editPluginForm);
	}
	
	private static void copyAcls(List<SecurityAcl> acls, ResPluginForm form) {
		for(SecurityAcl acl : acls) {
			List<String> principals = new ArrayList<>();
			acl.getMembers().getUsers().forEach(user -> principals.add("user:".concat(user)));
			acl.getMembers().getGroups().forEach(group -> principals.add("group:".concat(group)));
			
//			for(Principal principal :  acl.getMembers()) {
//				String prefix  = "";
//				if(principal instanceof User) {
//					prefix = "user";
//				} else if(principal instanceof Group) {
//					prefix = "group";
//				}
//				principals.add(prefix + ":" + principal.getExternalId()); 
//			}
			RoleType roleType = acl.getRole();
			switch(roleType) {
				case MANAGER:
					form.setManager(principals.toArray(new String[principals.size()]));
					break;
				case EDITOR:
					form.setEditor(principals.toArray(new String[principals.size()]));
					break;
				case USER:
					form.setUser(principals.toArray(new String[principals.size()]));
					break;
				default:
					LOGGER.debug("No match found.");
			}
		}
	}

}
