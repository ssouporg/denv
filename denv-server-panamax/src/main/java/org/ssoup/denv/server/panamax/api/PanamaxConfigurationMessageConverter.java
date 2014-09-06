/*
 * Copyright 2012  Research Studios Austria Forschungsges.m.b.H.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.ssoup.denv.server.panamax.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.ssoup.denv.common.converter.AbstractDenvYamlMessageConverter;
import org.ssoup.denv.server.panamax.domain.conf.PanamaxApplicationConfiguration;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * HttpMessageConverter for using denv domain models with Spring WebMVC. Currently supports json and yaml
 */
public class PanamaxConfigurationMessageConverter extends AbstractDenvYamlMessageConverter<PanamaxApplicationConfiguration> {

    private static final Logger logger = LoggerFactory.getLogger(PanamaxConfigurationMessageConverter.class);

    @Override
    protected boolean supports(Class<?> clazz) {
        return PanamaxApplicationConfiguration.class.isAssignableFrom(clazz);
    }

    @Override
    protected PanamaxApplicationConfiguration readInternal(Class<? extends PanamaxApplicationConfiguration> aClass, HttpInputMessage httpInputMessage) throws IOException, HttpMessageNotReadableException {
        return getYaml().loadAs(httpInputMessage.getBody(), PanamaxApplicationConfiguration.class);
    }

    @Override
    protected void writeInternal(PanamaxApplicationConfiguration panamaxApplicationConfiguration, HttpOutputMessage httpOutputMessage) throws IOException, HttpMessageNotWritableException {
        Writer w = new OutputStreamWriter(httpOutputMessage.getBody());
        getYaml().dump(panamaxApplicationConfiguration, w);
        w.flush();
        httpOutputMessage.getBody().flush();
    }
}