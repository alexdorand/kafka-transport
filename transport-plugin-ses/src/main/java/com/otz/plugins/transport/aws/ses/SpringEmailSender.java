package com.otz.plugins.transport.aws.ses;

import com.otz.transport.common.EventContent;
import com.otz.transport.common.EventHeader;
import com.otz.transport.common.content.EmailEventContent;
import com.otz.transport.common.plugins.EmailTemplateRepository;
import com.otz.transport.config.EmailConfigParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

/**
 * Copyright 2016 opentoolzone.com - Kafka Transport
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p>
 * Created by alexdorand on 2016-12-03.
 */

@Service
public class SpringEmailSender {

    private Properties props = System.getProperties();
    private Session session;
    private static final String TO = "to";


    @Autowired
    private EmailTemplateRepository emailTemplateRepository;

    @Autowired
    private EmailConfigParameters emailConfigParameters;

    private static final String MAIL_TRANSPORT_PROTOCOL = "mail.transport.protocol";
    private static final String MAIL_SMTP_PORT = "mail.smtp.port";
    private static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
    private static final String MAIL_SMTP_SSL_ENABLED = "mail.smtp.ssl.enable";


    private Transport transport;

    @PostConstruct
    public void init() throws MessagingException {

        props.put(MAIL_TRANSPORT_PROTOCOL, emailConfigParameters.getProtocol());
        props.put(MAIL_SMTP_PORT, emailConfigParameters.getPort());
        props.put(MAIL_SMTP_AUTH, emailConfigParameters.isSmtpAuth());
        props.put(MAIL_SMTP_SSL_ENABLED, emailConfigParameters.isSmtpSslEnable());

        // Create a Session object to represent a mail session with the specified properties.
        session = Session.getDefaultInstance(props);

    }

    public void send(EventHeader emailHeader, EventContent emailRequestContent) {

        EmailEventContent emailEventContent = (EmailEventContent) emailRequestContent;

        try {
            send(emailEventContent.getEmailType(), emailEventContent.getLocale(), emailEventContent.getValues().get(TO), emailEventContent.getValues());
        } catch (MessagingException e) {
            // TODO: re-queue to be sent again
            e.printStackTrace();
        }

    }

    public void send(String templateName, Locale locale, String to, Map<String, String> parameters) throws MessagingException {

        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(emailTemplateRepository.getFrom(templateName, locale)));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        msg.setSubject(emailTemplateRepository.getSubject(templateName, locale));
        msg.setContent(emailTemplateRepository.getContent(templateName, locale, parameters), emailTemplateRepository.getContentType(templateName, locale));


        try {
            // Create a transport.
            transport = session.getTransport();
            transport.connect(emailConfigParameters.getHost(), emailConfigParameters.getAccessKey(), emailConfigParameters.getSecretKey());
            transport.sendMessage(msg, msg.getAllRecipients());

        } catch (Exception e) {

            // TODO deal with failure to send
            // send message to hipchat
            e.printStackTrace();
        }

    }


}
