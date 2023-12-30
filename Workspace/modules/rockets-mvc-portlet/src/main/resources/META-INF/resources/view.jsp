<%@ include file="/init.jsp" %>
<%@ page import="com.liferay.portal.kernel.model.User" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="com.liferay.portal.kernel.service.UserLocalServiceUtil" %>

<portlet:defineObjects />

<div class="all-container w-100">
   <div class="user-container d-flex justify-content-between align-items-center border">
   		<b class="m-0">ID</b>
   	 	<b class="m-0">User Name</b>
  		<b class="m-0">Email</b>
   </div>

   <c:forEach var="user" items="${userList}">
      <div class="user-container d-flex justify-content-between align-items-center border">
   		<p class="m-0">${user.getUserId()}</p>
   	 	<p class="m-0">${user.getFullName()}</p>
  		<p class="m-0">${user.getEmailAddress()}</p>
      </div>
   </c:forEach>
</div>


