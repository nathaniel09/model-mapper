import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiField
import com.intellij.psi.util.PsiTreeUtil

/**
 * @author sebastianuskh on 5/19/17.
 */


class ModelMapAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val psiClass = getPsiClassFromContext(e) ?: return
        val dialog = ShowDialog(psiClass)
        dialog.show()

        val project = e.project ?: return
        val editor = CommonDataKeys.EDITOR.getData(e.dataContext) ?: return

        if (dialog.isOK) {
            generateModelMapper(psiClass, dialog.getSelectedField(), project, editor)
        }
    }

    private fun generateModelMapper(psiClass: PsiClass, selectedField: List<PsiField>, project: Project, editor: Editor) {
        object : WriteCommandAction.Simple<Unit>(psiClass.project, psiClass.containingFile){
            override fun run() {
                MapGenerator(psiClass, selectedField, project, editor).generate()
            }
        }.execute()
    }

    private fun getPsiClassFromContext(e: AnActionEvent?): PsiClass? {
        val psiFile = e?.getData(LangDataKeys.PSI_FILE)
        val editor = e?.getData(PlatformDataKeys.EDITOR)
        if (psiFile == null || editor == null) return null
        val offset = editor.getCaretModel().getOffset()
        val element = psiFile.findElementAt(offset)
        return PsiTreeUtil.getParentOfType(element, PsiClass::class.java)
    }

}