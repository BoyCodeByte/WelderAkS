package com.boycodebyte.welderaks.ui.instruments.worker

import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.boycodebyte.welderaks.R
import com.boycodebyte.welderaks.data.models.Instrument
import com.boycodebyte.welderaks.data.models.Profile
import com.boycodebyte.welderaks.databinding.ItemInstrumentBinding
import com.boycodebyte.welderaks.databinding.ItemInstrumentWorkerBinding


interface InstrumentActionListeners {

    fun onInstrumentDelete(instrument: Instrument)

    fun onInstrumentDetails(instrument: Instrument)
}

class InstrumentRecyclerAdapter(
    private val profile: Profile,
    private val actionListeners: InstrumentActionListeners
) :
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
        actionListeners.onInstrumentDetails(instrument)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InstrumentHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemInstrumentWorkerBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)

        return InstrumentHolder(binding)
    }

    override fun onBindViewHolder(holder: InstrumentHolder, position: Int) {
        val instrument = instruments[position]
        with(holder.binding) {
            holder.itemView.tag = instrument
            instNameTextView.text = instrument.name
            fixedUser.text = "${profile.name} ${profile.surname}"
            photoImageInstr.setImageResource(R.drawable.ic_instruments)
        }
    }

    override fun getItemCount(): Int = instruments.size

    class InstrumentHolder(val binding: ItemInstrumentWorkerBinding) :
        RecyclerView.ViewHolder(binding.root)
}