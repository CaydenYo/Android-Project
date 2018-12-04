package com.tarena.hero1.xml;
//创建DOM文档的工具类

import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class HeroXml {
	private Document doc=null;
	private String encoding="UTF-8";
	private Element root=null;//文档根节点
	public Document getDoc() {
		return doc;
	}
	public void setDoc(Document doc) {
		this.doc = doc;
	}
	public String getEncoding() {
		return encoding;
	}
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	public Element getRoot() {
		return root;
	}
	public void setRoot(Element root) {
		this.root = root;
	}
	
	public HeroXml() throws Exception {
		this.initial();
	}

	//创建一个初始化方法，用来生一个DOM对象
	public void initial() throws Exception{
		//创建一个文档构建器的工厂
		DocumentBuilderFactory factory=
				DocumentBuilderFactory.newInstance();
		DocumentBuilder builder=factory.newDocumentBuilder();
		//创建一个doc的文档
		doc=builder.newDocument();
	}
	//创建并返回文档的根标签(元素)
	public Element createRootElement(String rootTagName){
		if(doc.getDocumentElement()==null){
			//创建一个指定标签名的元素
			root=doc.createElement(rootTagName);
			//把创建的元素追加到文档上
			doc.appendChild(root);
			return root;
		}
		//返回根节点
		return doc.getDocumentElement();
	}
	//创建一个普通的标签
	public Element createElement(String tagName,String value){
		//获得根节点相关的dom文档
		Document doc=root.getOwnerDocument();
		//创建一个元素
		Element child=doc.createElement(tagName);
		//设置元素的文档内容
		child.setTextContent(value);
		//返回子元素
		return child;
	}

	//创建并返回一个空节点
	public Element createElement(String tagName){
		//获得和根节相关的dom对象
		Document doc=root.getOwnerDocument();
		Element child=doc.createElement(tagName);
		return child;
	}

	//返回xml格式的字符串
	public String xmlToString() throws Exception{
		String xmlString=null;
		TransformerFactory factory=TransformerFactory.newInstance();
		Transformer transformer=factory.newTransformer();
		
		DOMSource source=new DOMSource(doc);
		
		StringWriter writer=new StringWriter();
	
		StreamResult result=new StreamResult(writer);
		
		transformer.setOutputProperty("encoding", encoding);
		
		transformer.transform(source, result);
		xmlString=writer.getBuffer().toString();
		return xmlString;
	}
}
