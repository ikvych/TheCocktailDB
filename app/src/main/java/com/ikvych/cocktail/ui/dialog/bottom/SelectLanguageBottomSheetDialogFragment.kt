package com.ikvych.cocktail.ui.dialog.bottom

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.ikvych.cocktail.R
import com.ikvych.cocktail.filter.type.AlcoholDrinkFilter
import com.ikvych.cocktail.ui.dialog.base.*
import com.ikvych.cocktail.util.Language


class SelectLanguageBottomSheetDialogFragment :
    ListBaseBottomSheetDialogFragment<Language?, ListDialogButton, SelectLanguageDialogType>() {

    override val dialogType: SelectLanguageDialogType =
        SelectLanguageDialogType
    override var data: Language? = Language.DEFAULT
    private var selectedLanguage: Language? = Language.DEFAULT
    override var dialogBuilder: SimpleBottomSheetDialogBuilder = SimpleBottomSheetDialogBuilder()

    override val dialogListDataAdapter: DialogListDataAdapter<Language?> =
        object : DialogListDataAdapter<Language?> {
            override fun getName(data: Language?): CharSequence {
                return data?.value ?: ""
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialogBuilder = requireArguments().getParcelable(EXTRA_KEY_BUILDER)!!
        val language = requireArguments().getInt(EXTRA_KEY_SELECTED_LANGUAGE)
        selectedLanguage = Language.values()[language]
        listAdapter = DialogListAdapter(selectedLanguage)
    }

    override var listData: List<Language?> = mutableListOf<Language?>().apply {
        addAll(Language.values())
    }.toList()

    override fun getButtonType(view: View): ListDialogButton {
        return when (view.id) {
            R.id.tv_filter_type_element -> ItemListDialogButton
            else -> throw NotImplementedError("handle another dialog button types")
        }
    }


    override fun obtainDataForView(view: View): Language? {
        return when (getButtonType(view)) {
            is ItemListDialogButton -> view.tag as? Language?
            else -> super.obtainDataForView(view)
        }
    }

    companion object {
        fun newInstance(selectedLanguage: Language? = null): SelectLanguageBottomSheetDialogFragment {
            return SelectLanguageBottomSheetDialogFragment()
                .apply {
                arguments = bundleOf(
                    EXTRA_KEY_BUILDER to SimpleBottomSheetDialogBuilder().apply {
                        titleTextResId = R.string.profile_change_language_dialog_title
                        descriptionTextResId = R.string.profile_change_language_dialog_description
                        isCancelable = true
                    },
                    EXTRA_KEY_SELECTED_LANGUAGE to selectedLanguage?.ordinal
                )
            }
        }

        private const val EXTRA_KEY_BUILDER = "EXTRA_KEY_BUILDER"
        private const val EXTRA_KEY_SELECTED_LANGUAGE = "EXTRA_KEY_SELECTED_LANGUAGE"
    }
}