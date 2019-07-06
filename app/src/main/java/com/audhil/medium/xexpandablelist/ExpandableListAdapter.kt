package com.audhil.medium.xexpandablelist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat

class ExpandableListAdapter : BaseExpandableListAdapter() {

    var items = mutableListOf<Comment>()
    var selectedGroupPos = -1
    var selectedChildPos = -1

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean = true

    override fun hasStableIds(): Boolean = true

    override fun getChildrenCount(groupPosition: Int): Int = items[groupPosition].replies.size

    override fun getChild(groupPosition: Int, childPosition: Int): Any = items[groupPosition].replies[childPosition]

    override fun getChildId(groupPosition: Int, childPosition: Int): Long = childPosition.toLong()

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup
    ): View? {
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(parent.context).inflate(R.layout.child, null)
        }

        view?.findViewById<TextView>(R.id.child_txt)?.apply {
            text = items[groupPosition].replies[childPosition].replyName

            if (selectedGroupPos == groupPosition && selectedChildPos == childPosition)
                setBackgroundColor(ContextCompat.getColor(parent.context, android.R.color.darker_gray))
            else
                setBackgroundColor(0)
        }
        return view
    }

    override fun getGroupId(groupPosition: Int): Long = groupPosition.toLong()

    override fun getGroupCount(): Int = items.size

    override fun getGroup(groupPosition: Int): Any = items[groupPosition]

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup): View? {
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(parent.context).inflate(R.layout.parent, null)
        }

        view?.findViewById<TextView>(R.id.parent_txt)?.apply {
            text = items[groupPosition].commentName
            if (selectedGroupPos == groupPosition)
                setBackgroundColor(ContextCompat.getColor(parent.context, android.R.color.holo_green_light))
            else
                setBackgroundColor(0)
        }

        return view
    }

    fun changeItemBackGround(selectedGroupPos: Int, selectedChildPos: Int = -1) {
        this.selectedChildPos = selectedChildPos
        this.selectedGroupPos = selectedGroupPos
        notifyDataSetChanged()
    }
}