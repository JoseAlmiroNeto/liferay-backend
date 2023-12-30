<#if entries?has_content>
    <div class="container-cards d-flex w-100">
        <#list entries as curEntry>
            <#assign article=curEntry.getAssetRenderer().getArticle() />
            <@liferay_journal["journal-article"]
                articleId=article.getArticleId()
                groupId=article.getGroupId()
                ddmTemplateKey="42806" />
        </#list>
    </div>
</#if>