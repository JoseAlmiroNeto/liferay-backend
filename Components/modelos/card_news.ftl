<#assign AssetCategoryLocalService=serviceLocator.findService("com.liferay.asset.kernel.service.AssetCategoryLocalService") />

<div class="card-all">
 <a class="card-container" href="${friendlyURL}" style="text-decoration: none">
  <#if (imgNews.getData())?? && imgNews.getData() != "">
	  <img class="card-container-img" alt="${imgNews.getAttribute("alt")}" data-fileentryid="${imgNews.getAttribute("fileEntryId")}" src="${imgNews.getData()}" />
  </#if>
	<div class="card-body d-flex flex-column w-100 px-0">
	  <h1 class="card-body-title m-0">
	    <#if (title.getData())??>
	      ${title.getData()}
      </#if>
		</h1>
		<p class="card-body-date m-0">
		  <#assign date_Data = getterUtil.getString(date.getData())>
      <#if validator.isNotNull(date_Data)>
	      <#assign date_DateObj = dateUtil.parseDate("yyyy-MM-dd", date_Data, locale)>
        ${dateUtil.getDate(date_DateObj, "dd MMM yyyy", locale)}
      </#if>
		</p>
		<p class="card-body-description m-0">
		  <#if (description.getData())??>
	       ${description.getData()}
      </#if>
		</p>
	</div>
	</a>
</div>