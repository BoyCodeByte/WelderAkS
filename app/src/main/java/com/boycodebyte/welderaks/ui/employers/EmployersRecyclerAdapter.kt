package com.boycodebyte.welderaks.ui.employers

import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.boycodebyte.welderaks.R
import com.boycodebyte.welderaks.data.models.Profile
import com.boycodebyte.welderaks.databinding.EmployersItemBinding


interface ProfileActionListeners{

    fun onProfileDelete(profile:Profile)

    fun onProfileDetails(profile: Profile)
}

class ProfileRecyclerAdapter(private val actionListeners: ProfileActionListeners):
    RecyclerView.Adapter<ProfileRecyclerAdapter.ProfileHolder>(),
    View.OnClickListener{

    var users = emptyList<Profile>()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    override fun onClick(v: View) {
        val profile = v.tag as Profile
        when (v.id) {
            R.id.deleteImageButton -> {
                showPopupMenu(v)
            }
            else -> {
                actionListeners.onProfileDetails(profile)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding =EmployersItemBinding.inflate(inflater,parent,false)

        binding.root.setOnClickListener(this)
        binding.deleteImageButton.setOnClickListener(this)

        return ProfileHolder(binding)
    }

    override fun onBindViewHolder(holder: ProfileHolder, position: Int) {
        val profile = users[position]
        with(holder.binding){
            holder.itemView.tag = profile
            deleteImageButton.tag = profile
            deleteImageButton.setImageResource(R.drawable.delete)
            employerNameTextView.text="${profile.name} ${profile.surname}"
            jobTitle.text="${profile.jobTitle}"
            photoImageEmployer.setImageResource(R.drawable.employer)
        }
    }

    override fun getItemCount(): Int =users.size

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(view.context, view)
        val context = view.context
        val profile = view.tag as Profile

        popupMenu.menu.add(0, ID_REMOVE, Menu.NONE, context.getString(R.string.remove))

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                ID_REMOVE -> {
                    actionListeners.onProfileDelete(profile)
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


        class ProfileHolder(val binding: EmployersItemBinding):
            RecyclerView.ViewHolder(binding.root)
}