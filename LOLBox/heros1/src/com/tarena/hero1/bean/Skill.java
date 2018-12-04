package com.tarena.hero1.bean;

import java.io.Serializable;

public class Skill implements Serializable{
	private String skillName;
	private String skillPath;
	public String getSkillName() {
		return skillName;
	}
	public void setSkillName(String skillName) {
		this.skillName = skillName;
	}
	public String getSkillPath() {
		return skillPath;
	}
	public void setSkillPath(String skillPath) {
		this.skillPath = skillPath;
	}
	public Skill(String skillName, String skillPath) {
		super();
		this.skillName = skillName;
		this.skillPath = skillPath;
	}
	public Skill() {
		super();
	}
	@Override
	public String toString() {
		return "Skill [skillName=" + skillName + ", skillPath=" + skillPath + "]";
	}
}
