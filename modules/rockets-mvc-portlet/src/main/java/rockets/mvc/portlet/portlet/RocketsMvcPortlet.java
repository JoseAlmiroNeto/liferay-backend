package rockets.mvc.portlet.portlet;

import rockets.mvc.portlet.constants.RocketsMvcPortletKeys; 

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;

import javax.portlet.Portlet;

import org.osgi.service.component.annotations.Component;

import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;

import java.io.IOException;
import java.util.List;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * @author Zik
 */
@Component(
	immediate = true,
	property = {
		"com.liferay.portlet.display-category=category.sample",
		"com.liferay.portlet.header-portlet-css=/css/main.css",
		"com.liferay.portlet.instanceable=true",
		"javax.portlet.display-name=RocketsMvc",
		"javax.portlet.init-param.template-path=/",
		"javax.portlet.init-param.view-template=/view.jsp",
		"javax.portlet.name=" + RocketsMvcPortletKeys.ROCKETSMVC,
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=power-user,user"
	},
	service = Portlet.class
)
public class RocketsMvcPortlet extends MVCPortlet {
	
	@Override
	   public void doView(
	      RenderRequest renderRequest, RenderResponse renderResponse)
	      throws IOException, PortletException {

	      List<User> userList = UserLocalServiceUtil.getUsers(-1, -1);
	      renderRequest.setAttribute("userList", userList);

	      super.doView(renderRequest, renderResponse);
	}
}