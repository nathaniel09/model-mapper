import com.intellij.codeInsight.generation.GenerateGetterAndSetterHandler
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.JavaDirectoryService
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiField

/**
 * Created by sebastianuskh on 5/22/17.
 */

class MapGenerator(val psiClass: PsiClass, val selectedField: List<PsiField>, val project: Project, val editor: Editor){
    fun generate() {
        val serviceClass = JavaDirectoryService.getInstance().createClass(psiClass.containingFile.containingDirectory, "${psiClass.name}Service")
        for (field in selectedField) {
            serviceClass.add(field)
        }

        GenerateGetterAndSetterHandler().invoke(project, editor, serviceClass.containingFile)

        val mapperClass = JavaDirectoryService.getInstance().createClass(psiClass.containingFile.containingDirectory, "${psiClass.name}ServiceMapper")

    }
}