package com.tarena.hero1.xml;
//����DOM�ĵ��Ĺ�����

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
	private Element root=null;//�ĵ����ڵ�
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

	//����һ����ʼ��������������һ��DOM����
	public void initial() throws Exception{
		//����һ���ĵ��������Ĺ���
		DocumentBuilderFactory factory=
				DocumentBuilderFactory.newInstance();
		DocumentBuilder builder=factory.newDocumentBuilder();
		//����һ��doc���ĵ�
		doc=builder.newDocument();
	}
	//�����������ĵ��ĸ���ǩ(Ԫ��)
	public Element createRootElement(String rootTagName){
		if(doc.getDocumentElement()==null){
			//����һ��ָ����ǩ����Ԫ��
			root=doc.createElement(rootTagName);
			//�Ѵ�����Ԫ��׷�ӵ��ĵ���
			doc.appendChild(root);
			return root;
		}
		//���ظ��ڵ�
		return doc.getDocumentElement();
	}
	//����һ����ͨ�ı�ǩ
	public Element createElement(String tagName,String value){
		//��ø��ڵ���ص�dom�ĵ�
		Document doc=root.getOwnerDocument();
		//����һ��Ԫ��
		Element child=doc.createElement(tagName);
		//����Ԫ�ص��ĵ�����
		child.setTextContent(value);
		//������Ԫ��
		return child;
	}

	//����������һ���սڵ�
	public Element createElement(String tagName){
		//��ú͸�����ص�dom����
		Document doc=root.getOwnerDocument();
		Element child=doc.createElement(tagName);
		return child;
	}

	//����xml��ʽ���ַ���
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
