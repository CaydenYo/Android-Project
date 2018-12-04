package com.tarena.hero1.xml;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.xmlpull.v1.XmlPullParser;

import com.tarena.hero1.bean.Hero;
import com.tarena.hero1.bean.Skill;

import android.content.Context;
import android.util.Xml;

public class XMLManager {
	String[] bigImage;// 存放大图的路径的字符串数组
	String[] smallImage;// 存小图的图径的字符串数组
	String[] strjn;// 存放技能的路径的数组
	Context context;

	public XMLManager(Context context) {
		this.context = context;
	}

	public String createXML() throws Exception {
		// 获得英雄大图的路径保存到数组中
		bigImage = context.getAssets().list("bfile");
		smallImage = context.getAssets().list("sfile");
		String[] tfilelist = context.getAssets().list("tfile");
		strjn = context.getAssets().list("jnfile");
		String herosName[] = getHerosName("name.txt");

		// 创建一个xml的dom
		HeroXml xml = new HeroXml();
		// 创建一个根节点
		Element root = xml.createRootElement("heros");
		// 在根节点上创建hero子节点
		for (int i = 0; i < bigImage.length; i++) {
			// 创建一个英雄节点
			Element hero = xml.createElement("hero");
			// 获得所有英雄的名字
			String heroName = herosName[i];
			String names[] = heroName.split(" ");
			String name = names[0] + names[1];
			String type = names[2];

			// 获得英雄的详细信息
			String bpath = "bfile/" + bigImage[i];// 大图路径
			String spath = "sfile/" + smallImage[i];// 小图路径
			String tpath = "tfile/" + tfilelist[i];// 英雄介绍文档路径

			// 获得英雄技能信息
			String jnpath = "jnfile/" + strjn[i];
			String str1[] = context.getAssets().list(jnpath);

			String jnImagePath = jnpath + "/" + str1[0];
			String jnTxtPath = jnpath + "/" + str1[1];

			String jnImages[] = context.getAssets().list(jnImagePath);
			String jnjieshao[] = context.getAssets().list(jnTxtPath);

			// 创建英雄的子元素
			Element ename = xml.createElement("name", name);
			Element ebpath = xml.createElement("bpath", bpath);
			Element espath = xml.createElement("spath", spath);
			Element heropath = xml.createElement("heropath", tpath);
			Element herotype = xml.createElement("type", type);
			// 创建一人技能节点
			Element skill = xml.createElement("skill");
			// 创建技能的子节点
			for (int j = 0; j < jnImages.length; j++) {
				Element skillname = xml.createElement("skillname", jnTxtPath + "/" + jnjieshao[j]);
				Element skillImage = xml.createElement("skillurl", jnImagePath + "/" + jnImages[j]);

				skill.appendChild(skillname);
				skill.appendChild(skillImage);
			}

			hero.appendChild(ename);
			hero.appendChild(espath);
			hero.appendChild(ebpath);
			hero.appendChild(heropath);
			hero.appendChild(herotype);
			hero.appendChild(skill);

			// 将英雄添加到根节点上
			root.appendChild(hero);
		}
		String xmlString = xml.xmlToString();
		return xmlString;
	}

	private String[] getHerosName(String string) throws Exception {
		InputStream is = context.getAssets().open(string);
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		String line = null;
		line = reader.readLine();
		StringBuffer sb = new StringBuffer();
		while (line != null) {
			sb.append(line);
			// 再读下一行
			line = reader.readLine();
		}
		String names = sb.toString();

		return names.split(",");
	}

	public List<Hero> parseXml(String strXml) throws Exception {
		List<Hero> heros = null;
		InputStream is = new ByteArrayInputStream(strXml.getBytes());
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(is, "UTF-8");

		int event = parser.getEventType();

		Hero hero = null;
		List<Skill> skills = null;
		Skill skill = null;
		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:
				// 创建一个hero集合存放解析出来的hero数据
				heros = new ArrayList<Hero>();
				break;
			case XmlPullParser.START_TAG:
				if ("hero".equals(parser.getName())) {
					// 进行一信hero元素
					hero = new Hero();
				} else if ("name".equals(parser.getName())) {
					hero.setName(parser.nextText());
				} else if ("bpath".equals(parser.getName())) {
					hero.setBpath(parser.nextText());
				} else if ("spath".equals(parser.getName())) {
					hero.setSpath(parser.nextText());
				} else if ("heropath".equals(parser.getName())) {
					hero.setHeroPath(parser.nextText());
				} else if ("type".equals(parser.getName())) {
					hero.setType(parser.nextText());
				} else if ("skill".equals(parser.getName())) {
					skills = new ArrayList<Skill>();
				} else if ("skillname".equals(parser.getName())) {
					// 解析到技能了
					// 创建一个技能对象
					skill = new Skill();
					skill.setSkillName(parser.nextText());
				} else if ("skillurl".equals(parser.getName())) {
					skill.setSkillPath(parser.nextText());
					if (skill.getSkillName() != null && skill.getSkillPath() != null) {
						// 技能和图片都封装完成,把技能加到集合中
						skills.add(skill);
					}
				}
				break;
			case XmlPullParser.END_TAG:
				if ("hero".equals(parser.getName())) {
					// 一个英雄的数据封装完成
					// 添加到集合中
					hero.setSkills(skills);
					heros.add(hero);
				}
				break;
			}
			event = parser.next();
		}
		return heros;
	}
}
