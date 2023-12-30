package rockets.portlet.event.portlet;

import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.model.JournalFolder;
import com.liferay.journal.service.JournalArticleLocalServiceUtil;
import com.liferay.journal.service.JournalFolderServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;

import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.DocumentException;
import com.liferay.portal.kernel.xml.Node;
import com.liferay.portal.kernel.xml.SAXReaderUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;

import rockets.portlet.event.constants.RocketsPortletEventPortletKeys;

/**
 * @author Zik
 */
@Component(immediate = true, property = { "com.liferay.portlet.display-category=category.sample",
		"com.liferay.portlet.header-portlet-css=/css/main.css", "com.liferay.portlet.instanceable=true",
		"javax.portlet.display-name=RocketsPortletEvent", "javax.portlet.init-param.template-path=/",
		"javax.portlet.init-param.view-template=/view.jsp",
		"javax.portlet.name=" + RocketsPortletEventPortletKeys.ROCKETSPORTLETEVENT,
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=power-user,user" }, service = Portlet.class)

public class RocketsPortletEventPortlet extends MVCPortlet {

    private static final String FOLDER_NAME = "Eventos";
    private static final String TITULO_STRUCTURE = "titulo";
    private static final String DESCRICAO_STRUCTURE = "descricao";
    private static final String LOCALIDADE_STRUCTURE = "localidade";
    private static final String IMAGEM_EVENTO_STRUCTURE = "imagemEvento";
    private static final double VERSION_EPSILON = 0.000001;

    @Override
    public void doView(RenderRequest renderRequest, RenderResponse renderResponse)
            throws IOException, PortletException {

    	try {
            // Obtém informações do usuário
            ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
            User user = UserLocalServiceUtil.getUser(themeDisplay.getUserId());

            long groupId = PortalUtil.getScopeGroupId(renderRequest);

            // Obtém a localidade do usuário a partir do ExpandoBridge
            ExpandoBridge expandoBridge = user.getExpandoBridge();
            String valorLocalidade = (String) expandoBridge.getAttribute("Localidade");

            // Obtém a lista de pastas do Journal
            List<JournalFolder> folders = getJournalFolders(groupId);

            // Encontra o ID da pasta desejada
            long folderId = findFolderId(folders);

            // Obtém a lista de eventos DTO
            List<EventoDTO> listaEventos = getEventosDTO(groupId, folderId);

            // Define atributos de solicitação para uso na renderização
            renderRequest.setAttribute("user", user);
            renderRequest.setAttribute("listaEventos", listaEventos);
            renderRequest.setAttribute("valorLocalidade", valorLocalidade);

        } catch (PortalException | DocumentException e) {
        	// Tratamento de exceção
        }

        super.doView(renderRequest, renderResponse);
    }

    private List<JournalFolder> getJournalFolders(long groupId) throws PortalException {
        return JournalFolderServiceUtil.getFolders(groupId, 0);
    }

    private long findFolderId(List<JournalFolder> folders) {
        return folders.stream()
                .filter(folder -> folder.getName().equals(FOLDER_NAME))
                .mapToLong(JournalFolder::getFolderId)
                .findFirst()
                .orElse(0);
    }

    private List<EventoDTO> getEventosDTO(long groupId, long folderId)
            throws PortalException, DocumentException {
        List<EventoDTO> listaEventos = new ArrayList<>();

        // Obtém a lista de artigos do Journal na pasta específica
        List<JournalArticle> articles = getJournalArticles(groupId, folderId);

        // Itera sobre os artigos para processar e criar objetos EventoDTO
        for (JournalArticle article : articles) {
            double currentVersion = article.getVersion();

            // Obtém a versão mais recente do artigo
            double latestVersion = JournalArticleLocalServiceUtil.getLatestVersion(groupId, article.getArticleId());

            // Verifica se a versão atual é a mais recente (dentro de uma margem de erro)
            if (Math.abs(currentVersion - latestVersion) < VERSION_EPSILON) {
                String articleId = article.getArticleId();
                String tituloWebContent = article.getTitle();

                // Obtém informações adicionais da estrutura do artigo
                String titulo = getStructureContentByName(articleId, groupId, TITULO_STRUCTURE);
                String descricao = getStructureContentByName(articleId, groupId, DESCRICAO_STRUCTURE);
                String localidade = getStructureContentByName(articleId, groupId, LOCALIDADE_STRUCTURE);
                String thumb = getStructureContentByName(articleId, groupId, IMAGEM_EVENTO_STRUCTURE);

                // Cria um objeto EventoDTO e o preenche com as informações
                EventoDTO eventoDTO = new EventoDTO();
                eventoDTO.setTitulo(titulo);
                eventoDTO.setDescricao(descricao);
                eventoDTO.setLocalidade(localidade);
                eventoDTO.setThumb(thumb);
                eventoDTO.setTituloWebContent(tituloWebContent);

                // Adiciona o URL da imagem ao objeto EventoDTO
                String thumbUrl = eventoDTO.getImageUrl();
                eventoDTO.setThumbUrl(thumbUrl);

                // Adiciona o objeto EventoDTO à lista
                listaEventos.add(eventoDTO);
            }
        }

        // Retorna a lista final de objetos EventoDTO
        return listaEventos;
    }

    private List<JournalArticle> getJournalArticles(long groupId, long folderId) throws PortalException {
        // Utiliza o JournalArticleLocalServiceUtil para obter os artigos do Journal na pasta
        return JournalArticleLocalServiceUtil.getArticles(groupId, folderId);
    }

    public static String getStructureContentByName(String articleId, long groupId, String name)
            throws PortalException, DocumentException {
        final JournalArticle article = JournalArticleLocalServiceUtil.getArticle(groupId, articleId);
        final Document document = SAXReaderUtil.read(article.getContent());
        final Node node = document
                .selectSingleNode("/root/dynamic-element[@field-reference='" + name + "']/dynamic-content");

        return node != null ? node.getText() : null;
    }
}