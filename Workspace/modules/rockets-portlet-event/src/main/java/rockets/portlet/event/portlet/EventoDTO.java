package rockets.portlet.event.portlet;

import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;

public class EventoDTO {
	
	private String titulo;
    private String descricao;
    private String thumb;
    private String localidade;
    private String thumbUrl;
    private String tituloWebContent;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

	public String getLocalidade() {
		return localidade;
	}

	public void setLocalidade(String localidade) {
		this.localidade = localidade;
	}
	
	public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }
	
	public String getImageUrl() {
	    try {
	        JSONObject jsonObject = JSONFactoryUtil.createJSONObject(this.thumb);
	        if (jsonObject.has("url")) {
	            return jsonObject.getString("url");
	        }
	    } catch (JSONException e) {
	    	e.printStackTrace();
	    }
	    return null;
	}

	public String getTituloWebContent() {
		return tituloWebContent;
	}

	public void setTituloWebContent(String tituloWebContent) {
		this.tituloWebContent = tituloWebContent;
	}

}
