package com.tarena.hero1.bean;

import java.io.Serializable;
import java.util.List;

public class Hero implements Serializable{
	private String name;
	private String bpath;
	private String spath;
	private String heroPath;
	private String type;
	private List<Skill> skills;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBpath() {
		return bpath;
	}

	public void setBpath(String bpath) {
		this.bpath = bpath;
	}

	public String getSpath() {
		return spath;
	}

	public void setSpath(String spath) {
		this.spath = spath;
	}

	public String getHeroPath() {
		return heroPath;
	}

	public void setHeroPath(String heroPath) {
		this.heroPath = heroPath;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<Skill> getSkills() {
		return skills;
	}

	public void setSkills(List<Skill> skills) {
		this.skills = skills;
	}

	public Hero(String name, String bpath, String spath, String heroPath, String type, List<Skill> skills) {
		super();
		this.name = name;
		this.bpath = bpath;
		this.spath = spath;
		this.heroPath = heroPath;
		this.type = type;
		this.skills = skills;
	}

	public Hero() {
		super();
	}
	@Override
	public String toString() {
		return "Hero [name=" + name + ", bpath=" + bpath + ", spath=" + spath + ", heroPath=" + heroPath + ", type="
				+ type + ", skills=" + skills + "]";
	}
}
