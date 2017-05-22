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
import java.util.regex.Pattern;

import org.koyad.piston.business.model.App;
import org.koyad.piston.business.model.Members;
import org.koyad.piston.business.model.Plugin;
import org.koyad.piston.business.model.Preference;
import org.koyad.piston.business.model.Resource;
import org.koyad.piston.business.model.SecurityAcl;
import org.koyad.piston.business.model.enums.RoleType;

import in.koyad.piston.app.appMgmt.forms.AppDetailsPluginForm;
import in.koyad.piston.app.appMgmt.forms.AppPluginPrefsPluginForm;
import in.koyad.piston.app.appMgmt.forms.PluginDetailsPluginForm;
import in.koyad.piston.app.appMgmt.forms.ResPluginForm;
import in.koyad.piston.common.basic.exception.FrameworkException;
import in.koyad.piston.common.util.BeanPropertyUtils;

public class ModelGenerator {
	
	public static App getApp(AppDetailsPluginForm form) throws FrameworkException {
		App app = new App();
		populateResource(app, form);
		return app;
	}

	/**
	 * @param form
	 * @return
	 * @throws FrameworkException 
	 */
	public static Plugin getPlugin(PluginDetailsPluginForm form) throws FrameworkException {
		Plugin plugin = new Plugin();
//		plugin.setApp(PortalCache.apps.get(form.getAppId()));
		populateResource(plugin, form);
		return plugin;
	}
	
	private static void populateResource(Resource res, ResPluginForm form) throws FrameworkException {
		BeanPropertyUtils.copyProperties(res, form);
		res.setAcls(getAcls(res, form));
	}
	
	private static List<SecurityAcl> getAcls(Resource res, ResPluginForm form) throws FrameworkException {
		List<SecurityAcl> acls = new ArrayList<>();
		if(null != form.getManager()) {
			acls.add(getAcl(res, RoleType.MANAGER, form.getManager()));
		}
		
		if(null != form.getEditor()) {
			acls.add(getAcl(res, RoleType.EDITOR, form.getEditor()));
		}
		
		if(null != form.getUser()) {
			acls.add(getAcl(res, RoleType.USER, form.getUser()));
		}

		return acls;
	}
	
	private static SecurityAcl getAcl(Resource res, RoleType role, String[] typeAndExternalIds) throws FrameworkException {
		SecurityAcl acl = new SecurityAcl();
		acl.setRole(role);
		List<String> users = new ArrayList<>();
		List<String> groups = new ArrayList<>();
		for(String typeAndExternalId : typeAndExternalIds) {
			if(typeAndExternalId.startsWith("user:")) {
				users.add(typeAndExternalId.split(Pattern.quote(":"))[1]);
			} else if(typeAndExternalId.startsWith("group:")) {
				groups.add(typeAndExternalId.split(Pattern.quote(":"))[1]);
			}
		}
		Members members = new Members(users, groups);
		acl.setMembers(members);
		
		return acl;
	}
	
	public static List<Preference> getPreferences(AppPluginPrefsPluginForm form) throws FrameworkException {
		List<Preference> prefs = new ArrayList<>();
		
		for(int i=0; i < form.getNames().length; i++) {
			prefs.add(new Preference(form.getNames()[i], form.getValues()[i]));
		}
		
		return prefs;
	}

}
