import com.intellij.ide.util.DefaultPsiElementCellRenderer
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.LabeledComponent
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiField
import com.intellij.psi.PsiModifier
import com.intellij.ui.CollectionListModel
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBList
import com.intellij.ui.components.panels.VerticalBox
import javax.swing.JComponent
import javax.swing.JPanel

/**
 * @authorsebastianuskh on 5/22/17.
 */
class ShowDialog(val psiClass: PsiClass): DialogWrapper(psiClass.project) {

    private val fieldComponent: LabeledComponent<JPanel>
    private val includeSubclasses: JBCheckBox
    private val fieldColection: CollectionListModel<PsiField>
    private var showCheckbox: Boolean

    init {
        title = "Generate Model Mapper"

        fieldColection = CollectionListModel<PsiField>()
        val fieldList = JBList<PsiField>(fieldColection)
        fieldList.cellRenderer = DefaultPsiElementCellRenderer()
        val panel = ToolbarDecorator.createDecorator(fieldList).disableAddAction().createPanel()
        fieldComponent = LabeledComponent.create(panel, "Field To Be Map")

        includeSubclasses = JBCheckBox("Include fields from base classes")
        includeSubclasses.addActionListener { event -> updateFieldsDisplay(psiClass) }
        showCheckbox = psiClass.fields.size != psiClass.allFields.size

        updateFieldsDisplay(psiClass)
        init()
    }

    private fun updateFieldsDisplay(psiClass: PsiClass) {

        val fields = getClassFields(if (includeSubclasses.isSelected) psiClass.allFields else psiClass.allFields)
        fieldColection.removeAll()
        fieldColection.add(fields)
    }

    private fun getClassFields(allField: Array<out PsiField>): List<PsiField> {
        val list = ArrayList<PsiField>()
        for (field in allField) {
            if (!field.hasModifierProperty(PsiModifier.STATIC) && !field.hasModifierProperty(PsiModifier.TRANSIENT))
                list.add(field)
        }
        return list
    }

    override fun createCenterPanel(): JComponent? = fieldComponent

    override fun createSouthPanel(): JComponent {
        val southPanel = super.createSouthPanel()
        if (showCheckbox && southPanel != null) {
            val combinedView = VerticalBox()
            combinedView.add(includeSubclasses)
            combinedView.add(southPanel)
            return combinedView
        } else {
            return southPanel
        }
    }

    fun getSelectedField() = fieldColection.items
}