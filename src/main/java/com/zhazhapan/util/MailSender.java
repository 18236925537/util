/**
 * 
 */
package com.zhazhapan.util;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.sun.mail.util.MailSSLSocketFactory;

/**
 * 发送邮件需要邮箱账号开启POP3/SMTP服务
 * 
 * @author pantao
 *
 */
public class MailSender {

	public enum MailHost {
		/**
		 * QQ邮件服务器
		 */
		QQ,
		/**
		 * 网易163邮件服务器
		 */
		NE163,
		/**
		 * 谷歌邮件服务器
		 */
		GMAIL,
		/**
		 * 新浪邮件服务器
		 */
		SINA,
		/**
		 * OutLook邮件服务器
		 */
		OUTLOOK
	}

	/**
	 * 邮件服务器，默认使用QQ服务器
	 */
	private static String host = "smtp.qq.com";

	/**
	 * 个人名称
	 */
	private static String personal = "personal";

	/**
	 * 收件箱
	 */
	private static String from = "";

	/**
	 * 邮箱密码
	 */
	private static String key = "";

	/**
	 * 配置邮箱
	 * 
	 * @param mailHost
	 *            邮件服务器
	 * @param personal
	 *            个人名称
	 * @param from
	 *            发件箱
	 * @param key
	 *            密码
	 */
	public static void config(MailHost mailHost, String personal, String from, String key) {
		setHost(mailHost);
		setPersonal(personal);
		setFrom(from);
		setKey(key);
	}

	/**
	 * 配置邮箱
	 * 
	 * @param host
	 *            邮件服务器
	 * @param personal
	 *            个人名称
	 * @param from
	 *            发件箱
	 * @param key
	 *            密码
	 */
	public static void config(String host, String personal, String from, String key) {
		setHost(host);
		setPersonal(personal);
		setFrom(from);
		setKey(key);
	}

	/**
	 * 发送邮件
	 * 
	 * @param mailHost
	 *            邮件服务器
	 * @param personal
	 *            个人名称
	 * @param to
	 *            收件箱
	 * @param title
	 *            标题
	 * @param content
	 *            内容
	 * @param from
	 *            收件箱
	 * @param key
	 *            密码
	 * @throws UnsupportedEncodingException
	 *             异常
	 * @throws GeneralSecurityException
	 *             异常
	 * @throws MessagingException
	 *             异常
	 */
	public static void sendMail(MailHost mailHost, String personal, String to, String title, String content,
			final String from, final String key)
			throws UnsupportedEncodingException, GeneralSecurityException, MessagingException {
		setHost(mailHost);
		setPersonal(personal);
		sendMail(to, title, content, from, key);
	}

	/**
	 * 发送邮件
	 * 
	 * @param host
	 *            邮件服务器
	 * @param personal
	 *            个人名称
	 * @param to
	 *            收件箱
	 * @param title
	 *            标题
	 * @param content
	 *            内容
	 * @param from
	 *            收件箱
	 * @param key
	 *            密码
	 * @throws UnsupportedEncodingException
	 *             异常
	 * @throws GeneralSecurityException
	 *             异常
	 * @throws MessagingException
	 *             异常
	 */
	public static void sendMail(String host, String personal, String to, String title, String content,
			final String from, final String key)
			throws UnsupportedEncodingException, GeneralSecurityException, MessagingException {
		setHost(host);
		setPersonal(personal);
		sendMail(to, title, content, from, key);
	}

	/**
	 * 发送邮件，调用此方法前请先检查邮件服务器是否已经设置，如果没有设置，请先设置{@link MailSender#setHost(String)}，
	 * 如不设置将使用默认的QQ邮件服务器
	 * 
	 * @param to
	 *            收件箱
	 * @param title
	 *            标题
	 * @param content
	 *            内容
	 * @param from
	 *            发件箱
	 * @param key
	 *            密码
	 * @throws GeneralSecurityException
	 *             异常
	 * @throws UnsupportedEncodingException
	 *             异常
	 * @throws MessagingException
	 *             异常
	 */
	public static void sendMail(String to, String title, String content, String from, String key)
			throws GeneralSecurityException, UnsupportedEncodingException, MessagingException {
		setFrom(from);
		setKey(key);
		sendMail(to, title, content);
	}

	/**
	 * 发送邮件，调用此方法前请先检查邮件服务器是否已经设置，如果没有设置，请先设置{@link MailSender#setHost(String)}，
	 * 如不设置将使用默认的QQ邮件服务器
	 * 
	 * @param to
	 *            收件箱
	 * @param title
	 *            标题
	 * @param content
	 *            内容
	 * @throws GeneralSecurityException
	 *             异常
	 * @throws UnsupportedEncodingException
	 *             异常
	 * @throws MessagingException
	 *             异常
	 */
	public static void sendMail(String to, String title, String content)
			throws GeneralSecurityException, UnsupportedEncodingException, MessagingException {
		// 获取系统属性
		Properties properties = System.getProperties();
		// 设置邮件服务器
		properties.setProperty("mail.smtp.host", host);
		properties.put("mail.smtp.auth", "true");
		MailSSLSocketFactory sf;
		sf = new MailSSLSocketFactory();
		sf.setTrustAllHosts(true);
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.ssl.socketFactory", sf);
		// 获取默认session对象
		Session session = Session.getDefaultInstance(properties, new Authenticator() {
			@Override
			public PasswordAuthentication getPasswordAuthentication() {
				// 发件人邮件用户名、密码
				return new PasswordAuthentication(from, key);
			}
		});
		// 创建默认的MimeMessage对象
		MimeMessage message = new MimeMessage(session);
		// Set From:头部头字段
		message.setFrom(new InternetAddress(from, personal, "UTF-8"));
		// Set To:头部头字段
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
		// Set Subject:头部头字段
		message.setSubject(title, "UTF-8");
		// 设置消息体
		message.setContent(content, "text/html;charset=UTF-8");
		message.setSentDate(new Date());
		// 发送消息
		Transport.send(message);
	}

	/**
	 * 获取当前邮件服务器
	 * 
	 * @return {@link String}
	 */
	public static String getHost() {
		return host;
	}

	/**
	 * 设置邮件服务器
	 * 
	 * @param host
	 *            {@link String}
	 */
	public static void setHost(String host) {
		MailSender.host = host;
	}

	/**
	 * 设置邮件服务器
	 * 
	 * @param mailHost
	 *            {@link MailHost}
	 */
	public static void setHost(MailHost mailHost) {
		switch (mailHost) {
		case NE163:
			host = "smtp.163.com";
			break;
		case GMAIL:
			host = "smtp.gmail.com";
			break;
		case SINA:
			host = "smtp.sina.com";
			break;
		case OUTLOOK:
			host = "smtp-mail.outlook.com";
			break;
		default:
			host = "smtp.qq.com";
			break;
		}
	}

	/**
	 * 获取个人名称
	 * 
	 * @return {@link String}
	 */
	public static String getPersonal() {
		return personal;
	}

	/**
	 * 设置个人名称
	 * 
	 * @param personal
	 *            {@link String}
	 */
	public static void setPersonal(String personal) {
		MailSender.personal = personal;
	}

	/**
	 * 获取发件箱
	 * 
	 * @return 发件箱
	 */
	public static String getFrom() {
		return from;
	}

	/**
	 * 设置发件箱
	 * 
	 * @param from
	 *            发件箱
	 */
	public static void setFrom(String from) {
		MailSender.from = from;
	}

	/**
	 * 获取邮箱密码
	 * 
	 * @return 密码
	 */
	public static String getKey() {
		return key;
	}

	/**
	 * 设置邮箱密码
	 * 
	 * @param key
	 *            密码
	 */
	public static void setKey(String key) {
		MailSender.key = key;
	}
}