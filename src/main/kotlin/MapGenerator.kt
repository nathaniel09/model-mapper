import com.intellij.psi.JavaDirectoryService
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiField

/**
 * Created by sebastianuskh on 5/22/17.
 */

class MapGenerator(val psiClass: PsiClass, val selectedField: List<PsiField>){
    fun generate() {
        val serviceClass = JavaDirectoryService.getInstance().createClass(psiClass.containingFile.containingDirectory, "${psiClass.name}Service")
        for (field in selectedField) {
            serviceClass.add(field)
        }

    }
}