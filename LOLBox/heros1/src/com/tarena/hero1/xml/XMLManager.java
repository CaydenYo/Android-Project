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
	String[] bigImage;// ��Ŵ�ͼ��·�����ַ�������
	String[] smallImage;// ��Сͼ��ͼ�����ַ�������
	String[] strjn;// ��ż��ܵ�·��������
	Context context;

	public XMLManager(Context context) {
		this.context = context;
	}

	public String createXML() throws Exception {
		// ���Ӣ�۴�ͼ��·�����浽������
		bigImage = context.getAssets().list("bfile");
		smallImage = context.getAssets().list("sfile");
		String[] tfilelist = context.getAssets().list("tfile");
		strjn = context.getAssets().list("jnfile");
		String herosName[] = getHerosName("name.txt");

		// ����һ��xml��dom
		HeroXml xml = new HeroXml();
		// ����һ�����ڵ�
		Element root = xml.createRootElement("heros");
		// �ڸ��ڵ��ϴ���hero�ӽڵ�
		for (int i = 0; i < bigImage.length; i++) {
			// ����һ��Ӣ�۽ڵ�
			Element hero = xml.createElement("hero");
			// �������Ӣ�۵�����
			String heroName = herosName[i];
			String names[] = heroName.split(" ");
			String name = names[0] + names[1];
			String type = names[2];

			// ���Ӣ�۵���ϸ��Ϣ
			String bpath = "bfile/" + bigImage[i];// ��ͼ·��
			String spath = "sfile/" + smallImage[i];// Сͼ·��
			String tpath = "tfile/" + tfilelist[i];// Ӣ�۽����ĵ�·��

			// ���Ӣ�ۼ�����Ϣ
			String jnpath = "jnfile/" + strjn[i];
			String str1[] = context.getAssets().list(jnpath);

			String jnImagePath = jnpath + "/" + str1[0];
			String jnTxtPath = jnpath + "/" + str1[1];

			String jnImages[] = context.getAssets().list(jnImagePath);
			String jnjieshao[] = context.getAssets().list(jnTxtPath);

			// ����Ӣ�۵���Ԫ��
			Element ename = xml.createElement("name", name);
			Element ebpath = xml.createElement("bpath", bpath);
			Element espath = xml.createElement("spath", spath);
			Element heropath = xml.createElement("heropath", tpath);
			Element herotype = xml.createElement("type", type);
			// ����һ�˼��ܽڵ�
			Element skill = xml.createElement("skill");
			// �������ܵ��ӽڵ�
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

			// ��Ӣ����ӵ����ڵ���
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
			// �ٶ���һ��
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
				// ����һ��hero���ϴ�Ž���������hero����
				heros = new ArrayList<Hero>();
				break;
			case XmlPullParser.START_TAG:
				if ("hero".equals(parser.getName())) {
					// ����һ��heroԪ��
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
					// ������������
					// ����һ�����ܶ���
					skill = new Skill();
					skill.setSkillName(parser.nextText());
				} else if ("skillurl".equals(parser.getName())) {
					skill.setSkillPath(parser.nextText());
					if (skill.getSkillName() != null && skill.getSkillPath() != null) {
						// ���ܺ�ͼƬ����װ���,�Ѽ��ܼӵ�������
						skills.add(skill);
					}
				}
				break;
			case XmlPullParser.END_TAG:
				if ("hero".equals(parser.getName())) {
					// һ��Ӣ�۵����ݷ�װ���
					// ��ӵ�������
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
