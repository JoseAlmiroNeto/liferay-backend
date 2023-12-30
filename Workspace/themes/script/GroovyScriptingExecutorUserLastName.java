import com.liferay.portal.kernel.model.User
import com.liferay.portal.kernel.service.UserLocalServiceUtil

try {
    def users = UserLocalServiceUtil.getUsers(0, UserLocalServiceUtil.getUsersCount())

    users.each { user ->

        user.setLastName("GX2")
        
        UserLocalServiceUtil.updateUser(user)

        println("User ID: ${user.userId}")
        println("Nome Completo: ${user.fullName}")
        println("Novo Sobrenome: ${user.lastName}")
    }
} catch (Exception e) {
    println("Error:")
    e.printStackTrace()
    println("Mensagem de exceção: ${e.message}")
    println("Causa raiz: ${e.cause}")
}
