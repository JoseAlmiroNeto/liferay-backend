<%@ include file="/init.jsp"%>

<c:if test="${not empty listaEventos}">
    <div class="row m-0 d-flex justify-content-around">
        <c:forEach var="evento" items="${listaEventos}">
            <c:if test="${evento.localidade eq valorLocalidade}">
                <a href="http://localhost:8080/web/back-end/w/${evento.tituloWebContent.toLowerCase().replace(' ', '-')}" class="card-event shadow-sm d-flex flex-column" style="text-decoration: none">
                    <img src="${evento.thumbUrl != null ? evento.thumbUrl : 'https://i.imgur.com/9apdyP4.jpg'}" alt="Imagem da Notícia" class="card-event-img-top w-100">
                    <div class="card-event-body d-flex align-items-center justify-content-center text-center px-3">
                        <p class="card-event-body-description" id="descricao${evento.hashCode()}">${evento.descricao}</p>
                        <script>
                            // Limitar o número de caracteres no lado do cliente após o carregamento
                            var descricaoElement = document.getElementById("descricao${evento.hashCode()}");
                            if (descricaoElement.innerHTML.length > 50) {
                                descricaoElement.innerHTML = descricaoElement.innerHTML.substring(0, 50) + '...';
                            }
                        </script>
                    </div>
                </a>
            </c:if>
        </c:forEach>
    </div>
</c:if>