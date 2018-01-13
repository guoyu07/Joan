package com.pengji.modules.upms.controller;

import com.jfinal.plugin.activerecord.Page;
import com.pengji.base.BaseProjectController;
import com.pengji.component.annotation.ControllerBind;
import com.pengji.modules.upms.model.SysRole;
import com.pengji.modules.upms.service.RoleService;
import com.pengji.utils.SQLUtils;
import com.pengji.utils.StrUtils;




/**
 * 角色
 * 
 * @author flyfox 2018-01-12
 */
@ControllerBind(controllerKey = "/upms/role")
public class RoleController extends BaseProjectController {

	//private static final Log log = Log.getLog(RoleController.class);
	
	private static RoleService srv = RoleService.me;
	
	private static final String path = "/pages/modules/upms/role/role_";

	public void index() {
		list();
	}
	
	public void list() {
		SysRole model = getModelByAttr(SysRole.class);
		
		SQLUtils sql = new SQLUtils(" from sys_role t where 1=1 ");
		if (model.getAttrValues().length != 0) {
			sql.setAlias("t");
			// 查询条件
			sql.whereEquals("name", model.getName()); 
		}
		// 排序
		String orderBy = getBaseForm().getOrderBy();
		if (StrUtils.isEmpty(orderBy)) {
			sql.append(" order by t.id desc ");
		} else {
			sql.append(" order by ").append(orderBy);
		}
		
		Page<SysRole> page = srv.paginate(getParaToInt("p", 1), 40,"select * ",sql);

		// 下拉框
		setAttr("page", page);
		setAttr("attr", model);
		render(path + "list.html");
	}

	public void add() {
		render(path + "add.html");
	}

	public void view() {
		SysRole model = srv.findById(getParaToInt());
		setAttr("model", model);
		render(path + "view.html");
	}

	public void delete() {
		srv.delete(getParaToInt());
		list();
	}

	public void edit() {
		SysRole model = srv.findById(getParaToInt());
		setAttr("model", model);
		render(path + "edit.html");
	}

	public void save() {
		Integer pid = getParaToInt();
		SysRole model = getModel(SysRole.class);
		if (pid != null && pid > 0) { // 更新
			srv.update(model);
		} else { // 新增
			srv.save(model);
		}
		renderMessage("保存成功");
	}
}
