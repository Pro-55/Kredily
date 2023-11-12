package com.example.kredily.ui.passcode.set_passcode

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import com.example.kredily.R
import com.example.kredily.databinding.FragmentOfficeLocationsSheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class OfficeLocationsSheetFragment : BottomSheetDialogFragment() {

    companion object {
        private val KEY_OFFICE_LOCATIONS = "key_office_locations"
        private val KEY_SELECTED_LOCATION = "key_selected_locations"

        @JvmStatic
        private fun newInstance(
            officeLocations: List<String>,
            selectedLocation: String?,
            listener: Listener?
        ) = OfficeLocationsSheetFragment().apply {
            arguments = Bundle().apply {
                putStringArray(KEY_OFFICE_LOCATIONS, officeLocations.toTypedArray())
                putString(KEY_SELECTED_LOCATION, selectedLocation)
            }
            this.listener = listener
        }

        @JvmStatic
        fun showDialog(
            officeLocations: List<String>,
            selectedLocation: String?,
            listener: Listener?,
            fm: FragmentManager,
            tag: String
        ) {
            if (!fm.isStateSaved) {
                newInstance(officeLocations, selectedLocation, listener).apply {
                    isCancelable = false
                    show(fm, tag)
                }
            }
        }
    }

    // Global
    private val TAG = OfficeLocationsSheetFragment::class.java.simpleName
    private lateinit var binding: FragmentOfficeLocationsSheetBinding
    private val officeLocations = mutableListOf<String>()
    private var selectedLocation: String? = null
    private var listener: Listener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setStyle(STYLE_NORMAL, R.style.RoundedBottomSheetDialogStyle)
        super.onCreate(savedInstanceState)
        arguments?.let { args ->
            officeLocations.clear()
            officeLocations.addAll(args.getStringArray(KEY_OFFICE_LOCATIONS)?.toList()!!)
            selectedLocation = args.getString(KEY_SELECTED_LOCATION) ?: officeLocations[0]
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return (super.onCreateDialog(savedInstanceState) as BottomSheetDialog).apply {
            setOnShowListener {
                (it as? BottomSheetDialog)?.behavior?.apply {
                    state = BottomSheetBehavior.STATE_EXPANDED
                    skipCollapsed = true
                    isDraggable = false
                    peekHeight = 0
                    setCanceledOnTouchOutside(true)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_office_locations_sheet,
            container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.recyclerOfficeLocations.adapter = OfficeLocationsAdapter(
            data = officeLocations,
            selectedLocation = selectedLocation!!,
            listener = object : OfficeLocationsAdapter.Listener {
                override fun onLocationSelected(selectedLocation: String) {
                    listener?.onLocationSelected(selectedLocation)
                    dismiss()
                }
            }
        )
    }

    interface Listener {
        fun onLocationSelected(selectedLocation: String)
    }
}