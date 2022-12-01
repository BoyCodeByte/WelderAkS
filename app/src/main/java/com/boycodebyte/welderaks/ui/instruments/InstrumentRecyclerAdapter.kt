package com.boycodebyte.welderaks.ui.instruments

import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.boycodebyte.welderaks.R
import com.boycodebyte.welderaks.data.models.Instrument
import com.boycodebyte.welderaks.data.models.Profile
import com.boycodebyte.welderaks.databinding.ItemInstrumentBinding


interface InstrumentActionListeners {

    fun onInstrumentDelete(instrument: Instrument)

    fun onInstrumentDetails(instrument: Instrument)
}

class InstrumentRecyclerAdapter(private val actionListeners: InstrumentActionListeners) :
    RecyclerView.Adapter<InstrumentRecyclerAdapter.InstrumentHolder>(),
    View.OnClickListener {


    var instruments = emptyList<Instrument>()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    var users = emptyList<Profile>()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    override fun onClick(v: View) {
        val instrument = v.tag as Instrument
        when (v.id) {
            R.id.deleteImageButton -> {
                showPopupMenu(v)
            }
            else -> {
                actionListeners.onInstrumentDetails(instrument)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InstrumentHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemInstrumentBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)
        binding.deleteImageButton.setOnClickListener(this)

        return InstrumentHolder(binding)
    }

    override fun onBindViewHolder(holder: InstrumentHolder, position: Int) {
        val instrument = instruments[position]
        with(holder.binding) {
            holder.itemView.tag = instrument
            deleteImageButton.tag = instrument
            deleteImageButton.setImageResource(R.drawable.delete)

            instNameTextView.text = instrument.name
            users.forEach {
                if (it.id == instrument.idOfProfile) {
                    fixedUser.text = "${it.name} ${it.surname}"
                }
                println(it)
            }
            photoImageInstr.setImageResource(R.drawable.ic_instruments)
        }
    }

    override fun getItemCount(): Int = instruments.size

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(view.context, view)
        val context = view.context
        val instrument = view.tag as Instrument

        popupMenu.menu.add(0, ID_REMOVE, Menu.NONE, context.getString(R.string.remove))

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                ID_REMOVE -> {
                    actionListeners.onInstrumentDelete(instrument)
                    notifyDataSetChanged()
                }
            }
            return@setOnMenuItemClickListener true
        }
        popupMenu.show()
    }

    companion object {
        private const val ID_REMOVE = 1

    }


    class InstrumentHolder(val binding: ItemInstrumentBinding) :
        RecyclerView.ViewHolder(binding.root)
}