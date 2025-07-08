<#-- @ftlvariable name="data" type="io.qameta.allure.attachment.http.HttpRequestAttachment" -->
<h3>Request</h3>
<pre><code>
${data.method} ${data.url}
</code></pre>

<#if data.body??>
<h4>Body</h4>
<pre><code>${data.body}</code></pre>
</#if>

<#if data.headers?? && data.headers?has_content>
<h4>Headers</h4>
<pre><code>
<#list data.headers as name, value>
${name}: ${value}
</#list>
</code></pre>
</#if>