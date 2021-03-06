package cn.slkj.slclgl.module.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.slkj.easyui.util.Tree;
import cn.slkj.slclgl.module.bean.Module;
import cn.slkj.slclgl.module.mapper.ModuleMapper;
import cn.slkj.slclgl.module.service.ModuleService;

@Repository
public class ModuleServiceImpl implements ModuleService {
	@Autowired
	private ModuleMapper mapper;

	@Override
	public List<Module> getAll(String parentId) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		// map.put("parentId",parentId);
		List<Module> list = mapper.getAll(map);
		List<Module> rootList = makeTree(list);
		return rootList;
	}

	private List<Module> makeTree(List<Module> list) {
		List<Module> parent = new ArrayList<Module>();
		// get parentId = null;
		for (Module e : list) {
			if (e.getParent_id().equals("0")) {
				e.setChildren(new ArrayList<Module>(0));
				parent.add(e);
			}
		}
		// 删除parentId = null;
		list.removeAll(parent);
		makeChildren(parent, list);
		return parent;
	}

	private void makeChildren(List<Module> parent, List<Module> children) {
		if (children.isEmpty()) {
			return;
		}
		List<Module> tmp = new ArrayList<Module>();
		for (Module c1 : parent) {
			for (Module c2 : children) {
				c2.setChildren(new ArrayList<Module>(0));
				if (c1.getId().equals(c2.getParent_id())) {
					c1.getChildren().add(c2);
					tmp.add(c2);
				}
			}
		}
		children.removeAll(tmp);
		makeChildren(tmp, children);
	}

	@Override
	public List<Tree> getCombotree(String parentId) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		List<Module> list = mapper.getAll(map);
		return toTree(list, "0");
	}

	private List<Tree> toTree(List<Module> list, String code) {
		List<Tree> trees = new ArrayList<Tree>();
		for (Module m : list) {
			Tree t = new Tree();
			t.setId(m.getId() + "");
			t.setText(m.getName());
			// t.setState();
			// t.setChecked();
			if (code.equals(m.getParent_id())) {
				t.setChildren(toTree(list, m.getId()));
				trees.add(t);
			}

		}
		return trees;
	}

	@Override
	public int insert(Module module) {
		try {
			return mapper.insert(module);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;

	}

	@Override
	public int update(Module module) {
		try {
			return mapper.update(module);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int delete(String id) {
		try {
			return mapper.delete(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;

	}

	@Override
	public Module queryOne(String id) {
		return mapper.queryOne(id);
	}
}
