package com.whatever.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

@Service
public class EmailService {

	/** We use velocity to create the page registration confirmation sent to new user*/
	@Autowired
	VelocityEngine velocityEngine;
	
	@Autowired
	private JavaMailSender mailSender;	
	
	@Value("${mail.enable}")
	private Boolean enable;
	
	@Value("${site.url}")
	private String url;
	
	private void sendEmail(MimeMessagePreparator preparator){
		
		if (enable)
			mailSender.send(preparator);
	}

	@Async /** We don't wait for server response*/
	public void sendVerificationEmail(final String emailAddress, String token){
		
		/*final StringBuilder sb = new StringBuilder();
		sb.append("<HTML>");
		sb.append("<p>Hello there, this is <strong>HTML, verify your EMAIL</strong></p>");
		sb.append("</HTML>");*/
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("token", token);
		model.put("url", url);
		
		final String contents = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "/com/whatever/velocity/verifyemail.vm", "UTF-8" ,model);
		
		MimeMessagePreparator preparator = new MimeMessagePreparator(){

			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
				
				
				message.setTo(emailAddress);
				message.setFrom(new InternetAddress("dighital@yahoo.com"));
				message.setSubject("Please Verify your email Address");
				message.setSentDate(new Date());
				message.setText(contents, true);
				/*message.setText(sb.toString(), true);*/
			}
			
		};
		
		sendEmail(preparator);
	}
}
