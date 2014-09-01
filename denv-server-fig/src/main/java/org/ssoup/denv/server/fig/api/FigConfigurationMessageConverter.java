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

package org.ssoup.denv.server.fig.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.ssoup.denv.common.converter.AbstractDenvYamlMessageConverter;
import org.ssoup.denv.server.fig.domain.conf.FigApplicationConfiguration;
import org.ssoup.denv.server.fig.domain.conf.FigServiceConfiguration;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

/**
 * HttpMessageConverter for using denv domain models with Spring WebMVC. Currently supports json and yaml
 */
public class FigConfigurationMessageConverter extends AbstractDenvYamlMessageConverter<FigApplicationConfiguration> {

    private static final Logger logger = LoggerFactory.getLogger(FigConfigurationMessageConverter.class);

    @Override
    protected boolean supports(Class<?> clazz) {
        return FigApplicationConfiguration.class.isAssignableFrom(clazz);
    }

    @Override
    protected FigApplicationConfiguration readInternal(Class<? extends FigApplicationConfiguration> aClass, HttpInputMessage httpInputMessage) throws IOException, HttpMessageNotReadableException {
        Yaml yaml = new Yaml();
        FigApplicationConfiguration figAppConfig = new FigApplicationConfiguration();
        Map<String, Object> figConfigMap = (Map<String, Object>)yaml.load(httpInputMessage.getBody());
        for (String serviceName : figConfigMap.keySet()) {
            String serviceYaml = yaml.dump(figConfigMap.get(serviceName));
            FigServiceConfiguration serviceConfiguration = yaml.loadAs(serviceYaml, FigServiceConfiguration.class);
            figAppConfig.put(serviceName, serviceConfiguration);
        }
        return figAppConfig;
    }

    @Override
    protected void writeInternal(FigApplicationConfiguration figApplicationConfiguration, HttpOutputMessage httpOutputMessage) throws IOException, HttpMessageNotWritableException {
        Writer w = new OutputStreamWriter(httpOutputMessage.getBody());
        getYaml().dump(figApplicationConfiguration, w);
        w.flush();
        httpOutputMessage.getBody().flush();
    }
}
