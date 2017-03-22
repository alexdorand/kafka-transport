package com.sample.application.email.srv;

import com.otz.transport.common.plugins.EmailTemplateRepository;
import com.sample.application.email.domain.EmailTemplates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Locale;
import java.util.Map;

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
@Service("EmailTemplates")
public class EmailTemplateRepositoryImp implements EmailTemplateRepository {

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private EmailTemplates emailTemplates;


    @Override
    public String getContent(String template, Locale locale, Map<String, String> parameters) {

        String templateName = template + ((locale == null ? "" : "-" + locale.getLanguage()));

        Context ctx = new Context();
        ctx.setVariables(parameters);

        return this.templateEngine.process(emailTemplates.getTemplateMap().get(templateName).getContentFileName(), ctx);
    }

    @Override
    public String getSubject(String template, Locale locale) {

        String templateName = template + ((locale == null ? "" : "-" + locale.getLanguage()));
        return emailTemplates.getTemplateMap().get(templateName).getSubject();
    }

    @Override
    public String getFrom(String template, Locale locale) {
        String templateName = template + ((locale == null ? "" : "-" + locale.getLanguage()));
        return emailTemplates.getTemplateMap().get(templateName).getFrom();
    }

    @Override
    public String getContentType(String template, Locale locale) {
        // "text/html"
        String templateName = template + ((locale == null ? "" : "-" + locale.getLanguage()));
        return emailTemplates.getTemplateMap().get(templateName).getContentType();
    }
}
