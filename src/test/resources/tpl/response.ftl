<#-- @ftlvariable name="data" type="io.qameta.allure.attachment.http.HttpResponseAttachment" -->
<h3>Response</h3>
<pre><code>Status: Status: ${data.responseCode}</code></pre>

<#if data.body??>
    <h4>Body</h4>
    <pre><code>${data.body}</code></pre>
</#if>

<#if data.headers?has_content>
    <h4>Headers</h4>
    <#list data.headers as name, value>
        <pre><code><b>${name}</b>: ${value}</code></pre>
    </#list>
</#if>
