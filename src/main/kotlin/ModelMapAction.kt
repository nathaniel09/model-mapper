import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.command.WriteCommandAction
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

        if (dialog.isOK) {
            generateModelMapper(psiClass, dialog.getSelectedField())
        }
    }

    private fun generateModelMapper(psiClass: PsiClass, selectedField: List<PsiField>) {
        object : WriteCommandAction.Simple<Unit>(psiClass.project, psiClass.containingFile){
            override fun run() {
                MapGenerator(psiClass, selectedField).generate()
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