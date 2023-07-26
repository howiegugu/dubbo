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
package org.apache.dubbo.validation.filter;

import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.common.utils.ConfigUtils;
import org.apache.dubbo.rpc.AsyncRpcResult;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.validation.Validation;
import org.apache.dubbo.validation.Validator;

import static org.apache.dubbo.common.constants.CommonConstants.CONSUMER;
import static org.apache.dubbo.common.constants.CommonConstants.PROVIDER;
import static org.apache.dubbo.common.constants.FilterConstants.VALIDATION_KEY;

/**
 * ValidationFilter invoke the validation by finding the right {@link Validator} instance based on the
 * configured <b>validation</b> attribute value of invoker url before the actual method invocation.
 *
 * <pre>
 *     e.g. &lt;dubbo:method name="save" validation="jvalidation" /&gt;
 *     In the above configuration a validation has been configured of type jvalidation. On invocation of method <b>save</b>
 *     dubbo will invoke {@link org.apache.dubbo.validation.support.jvalidation.JValidator}
 * </pre>
 * <p>
 * To add a new type of validation
 * <pre>
 *     e.g. &lt;dubbo:method name="save" validation="special" /&gt;
 *     where "special" is representing a validator for special character.
 * </pre>
 * <p>
 * developer needs to do
 * <br/>
 * 1)Implement a SpecialValidation.java class (package name xxx.yyy.zzz) either by implementing {@link Validation} or extending {@link org.apache.dubbo.validation.support.AbstractValidation} <br/>
 * 2)Implement a SpecialValidator.java class (package name xxx.yyy.zzz) <br/>
 * 3)Add an entry <b>special</b>=<b>xxx.yyy.zzz.SpecialValidation</b> under <b>META-INF folders org.apache.dubbo.validation.Validation file</b>.
 *
 * @see Validation
 * @see Validator
 * @see Filter
 * @see org.apache.dubbo.validation.support.AbstractValidation
 */
@Activate(group = {CONSUMER, PROVIDER}, value = VALIDATION_KEY, order = 10000)
public class ValidationFilter implements Filter {

    private Validation validation;

    /**
     * Sets the validation instance for ValidationFilter
     *
     * @param validation Validation instance injected by dubbo framework based on "validation" attribute value.
     */
    public void setValidation(Validation validation) {
        this.validation = validation;
    }

    /**
     * Perform the validation of before invoking the actual method based on <b>validation</b> attribute value.
     *
     * @param invoker    service
     * @param invocation invocation.
     * @return Method invocation result
     * @throws RpcException Throws RpcException if  validation failed or any other runtime exception occurred.
     */
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        // Validation 接口的代理类被注入成功，且该调用的方法有 validation 属性
        if (validation != null && !invocation.getMethodName().startsWith("$")
            && ConfigUtils.isNotEmpty(invoker.getUrl().getMethodParameter(invocation.getMethodName(), VALIDATION_KEY))) {
            try {
                // 接着通过 url 中 validation 属性值，并且为该方法创建对应的校验实现类
                Validator validator = validation.getValidator(invoker.getUrl());
                if (validator != null) {
                    // 若找到校验实现类的话，则真正开启对方法的参数进行校验
                    validator.validate(invocation.getMethodName(), invocation.getParameterTypes(), invocation.getArguments());
                }
            } catch (RpcException e) {
                throw e;
            } catch (Throwable t) {
                // 非 RpcException 异常的话，则直接封装结果返回
                return AsyncRpcResult.newDefaultAsyncResult(t, invocation);
            }
        }
        // 能来到这里，说明要么没有配置校验过滤器，要么就是校验了但参数都合法
        // 既然没有抛异常的话，那么就直接调用下一个过滤器的逻辑
        return invoker.invoke(invocation);
    }

}
