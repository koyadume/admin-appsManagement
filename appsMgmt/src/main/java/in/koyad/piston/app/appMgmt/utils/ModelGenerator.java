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
package in.koyad.piston.app.appMgmt.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.koyad.piston.core.model.App;
import org.koyad.piston.core.model.Plugin;
import org.koyad.piston.core.model.Preference;
import org.koyad.piston.core.model.Resource;
import org.koyad.piston.core.model.SecurityAcl;
import org.koyad.piston.core.model.enums.Role;

import in.koyad.piston.app.appMgmt.forms.AppDetailsPluginForm;
import in.koyad.piston.app.appMgmt.forms.AppPluginPrefsPluginForm;
import in.koyad.piston.app.appMgmt.forms.PluginDetailsPluginForm;
import in.koyad.piston.app.appMgmt.forms.ResPluginForm;
import in.koyad.piston.common.exceptions.FrameworkException;
import in.koyad.piston.common.utils.BeanPropertyUtils;
import in.koyad.piston.common.utils.ServiceManager;
import in.koyad.piston.core.sdk.api.PortalUserService;
import in.koyad.piston.servicedelegate.model.PistonModelCache;

public class ModelGenerator {
	
	private static final PortalUserService portalUserService = ServiceManager.getService(PortalUserService.class);

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
		plugin.setApp(PistonModelCache.apps.get(form.getAppId()));
		populateResource(plugin, form);
		return plugin;
	}
	
	private static void populateResource(Resource res, ResPluginForm form) throws FrameworkException {
		BeanPropertyUtils.copyProperties(res, form);
		res.setAcls(getAcls(res, form));
	}
	
	private static Set<SecurityAcl> getAcls(Resource res, ResPluginForm form) throws FrameworkException {
		Set<SecurityAcl> acls = new HashSet<>();
		if(null != form.getManager()) {
			acls.add(getAcl(res, Role.MANAGER, form.getManager()));
		}
		
		if(null != form.getEditor()) {
			acls.add(getAcl(res, Role.EDITOR, form.getEditor()));
		}
		
		if(null != form.getUser()) {
			acls.add(getAcl(res, Role.USER, form.getUser()));
		}

		return acls;
	}
	
	private static SecurityAcl getAcl(Resource res, Role role, String[] typeAndExternalIds) throws FrameworkException {
		SecurityAcl acl = new SecurityAcl();
		acl.setResource(res);
		acl.setRole(role);
		acl.setMembers(portalUserService.getPrincipals(typeAndExternalIds));
		
		return acl;
	}
	
	public static List<Preference> getPreferences(AppPluginPrefsPluginForm form) {
		List<Preference> prefs = new ArrayList<>();
		
		for(int i=0; i < form.getIds().length; i++) {
			Preference pref = new Preference();
			prefs.add(pref);
			
			pref.setId(form.getIds()[i]);
			pref.setName(form.getNames()[i]);
			pref.setValue(form.getValues()[i]);
			pref.setAppId(form.getAppIds()[i]);
			pref.setPluginId(form.getPluginIds()[i]);
		}
		
		return prefs;
	}

}
