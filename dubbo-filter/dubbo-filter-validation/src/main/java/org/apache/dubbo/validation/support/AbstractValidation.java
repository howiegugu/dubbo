/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.dubbo.validation.support;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.validation.Validation;
import org.apache.dubbo.validation.Validator;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * AbstractValidation is abstract class for Validation interface. It helps low level Validation implementation classes
 * by performing common task e.g. key formation, storing instance of validation class to avoid creation of unnecessary
 * copy of validation instance and faster execution.
 *
 * @see Validation
 * @see Validator
 */
public abstract class AbstractValidation implements Validation {

    private final ConcurrentMap<String, Validator> validators = new ConcurrentHashMap<>();

    @Override
    public Validator getValidator(URL url) {
        String key = url.toFullString();
        Validator validator = validators.get(key);
        if (validator == null) {
            // 若通过 url 从 Map 结构中找不到 value 的话，则直接根据 url 创建一个校验器实现类
// 而且 createValidator 是一个 protected abstract 修饰的
// 说明是一种模板方式，创建校验器实现类，是可被重写重新创建自定义的校验器
            validators.put(key, createValidator(url));
            validator = validators.get(key);
        }
        return validator;
    }

    protected abstract Validator createValidator(URL url);

}
