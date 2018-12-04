package com.tarena.test;

import java.util.List;

import com.tarena.hero1.bean.Hero;
import com.tarena.hero1.xml.XMLManager;

import android.test.AndroidTestCase;
import android.util.Log;

public class TestHero extends AndroidTestCase {
	public void testXml(){
		XMLManager manager=new XMLManager(this.getContext());
		try {
			String strXml=manager.createXML();
			Log.i("TAG", strXml);
			List<Hero> heros=manager.parseXml(strXml);
			Log.i("TAG", heros.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
