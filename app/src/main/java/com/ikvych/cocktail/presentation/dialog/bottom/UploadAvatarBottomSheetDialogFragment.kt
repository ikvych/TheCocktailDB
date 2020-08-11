package com.ikvych.cocktail.presentation.dialog.bottom

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.ikvych.cocktail.R
import com.ikvych.cocktail.presentation.dialog.base.*
import com.ikvych.cocktail.presentation.dialog.type.ItemListDialogButton
import com.ikvych.cocktail.presentation.dialog.type.ListDialogButton
import com.ikvych.cocktail.presentation.dialog.type.UploadPhotoDialogType
import com.ikvych.cocktail.presentation.enumeration.UploadAvatar


class UploadAvatarBottomSheetDialogFragment :
    ListBaseBottomSheetDialogFragment<UploadAvatar, ListDialogButton, UploadPhotoDialogType>() {

    override val dialogType: UploadPhotoDialogType =
        UploadPhotoDialogType
    override var data: UploadAvatar? = null
    private var selectedLanguage: UploadAvatar? = null
    override var dialogBuilder: SimpleBottomSheetDialogBuilder = SimpleBottomSheetDialogBuilder()

    override val dialogListDataAdapter: DialogListDataAdapter<UploadAvatar> =
        object : DialogListDataAdapter<UploadAvatar> {
            override fun getName(data: UploadAvatar): CharSequence {
                return data.value
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialogBuilder = requireArguments().getParcelable(EXTRA_KEY_BUILDER)!!
        listAdapter = DialogListAdapter()
    }

    override var listData: List<UploadAvatar> = mutableListOf<UploadAvatar>().apply {
        addAll(UploadAvatar.values())
    }.toList()

    override fun getButtonType(view: View): ListDialogButton {
        return when (view.id) {
            R.id.tv_button_item -> ItemListDialogButton
            else -> throw NotImplementedError("handle another dialog button types")
        }
    }


    override fun obtainDataForView(view: View): UploadAvatar? {
        return when (getButtonType(view)) {
            is ItemListDialogButton -> view.tag as? UploadAvatar?
            else -> super.obtainDataForView(view)
        }
    }

    companion object {
        fun newInstance(): UploadAvatarBottomSheetDialogFragment {
            return UploadAvatarBottomSheetDialogFragment()
                .apply {
                arguments = bundleOf(
                    EXTRA_KEY_BUILDER to SimpleBottomSheetDialogBuilder().apply {
                        titleTextResId = R.string.upload_avatar_dialog_title
                        descriptionTextResId = R.string.upload_avatar_dialog_description
                        isCancelable = true
                    }
                )
            }
        }

        private const val EXTRA_KEY_BUILDER = "EXTRA_KEY_BUILDER"
    }
}