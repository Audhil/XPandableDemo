package com.audhil.medium.xexpandablelist

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.text.TextUtils
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    private val adapter: ExpandableListAdapter by lazy {
        ExpandableListAdapter()
    }

    private val inputMethodManager by lazy {
        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    var selectedGroupPos = -1
    var selectedChildPos = -1

    private var items = mutableListOf<Comment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null || !savedInstanceState.containsKey("DataList"))
            for (i in 0..100) {
                val replies = mutableListOf<Reply>()
                //  comment this loop for no pre filled replies
                for (j in 0..1)
                    replies.add(Reply("reply ".plus(j + 1)))
                items.add(Comment("comment ".plus(i + 1), replies))
            }
        else {
            items = savedInstanceState.getParcelableArrayList<Comment>("DataList") as MutableList<Comment>
            adapter.changeItemBackGround(
                savedInstanceState.getInt("selectedGroupPos", -1),
                savedInstanceState.getInt("selectedChildPos", -1)
            )
        }

        adapter.items = items
        adapter.notifyDataSetChanged()
        list_view.setAdapter(adapter)

        //  parent
        list_view.setOnGroupClickListener { parent, v, groupPosition, id ->
            selectedGroupPos = groupPosition
            adapter.changeItemBackGround(selectedGroupPos)
            showKeyPad()
            false
        }

        //  child
        list_view.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
            selectedGroupPos = groupPosition
            selectedChildPos = childPosition
            adapter.changeItemBackGround(selectedGroupPos, selectedChildPos)
            showKeyPad()
            false
        }

        submit_btn.setOnClickListener {
            if (TextUtils.isEmpty(reply_etext.text)) {
                Toast.makeText(applicationContext, getString(R.string.empty_reply), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (selectedGroupPos < 0) {
                Toast.makeText(applicationContext, getString(R.string.select_proper_pos), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //  if child is clicked
            if (selectedGroupPos > 0 && selectedChildPos >= 0) {
                val oldReplies = mutableListOf<Reply>()
                oldReplies.addAll(adapter.items[selectedGroupPos].replies)
                //  adding reply to last item
                oldReplies.add(Reply(replyName = reply_etext.text.toString()))
                //  get the last item
                val temp = oldReplies[oldReplies.size - 1]
                for (i in oldReplies.size - 1 downTo selectedChildPos + 1) {
                    oldReplies[i] = oldReplies[i - 1]
                }
                oldReplies[selectedChildPos + 1] = temp
                adapter.items[selectedGroupPos].replies.clear()
                adapter.items[selectedGroupPos].replies.addAll(oldReplies)
                adapter.notifyDataSetChanged()
            }
            //  if only parent is clicked
            else
                if (selectedGroupPos > 0) {
                    adapter.items[selectedGroupPos].replies.add(Reply(replyName = reply_etext.text.toString()))
                    adapter.notifyDataSetChanged()
                }

            reply_etext.setText("")
            inputMethodManager.hideSoftInputFromWindow(reply_etext.windowToken, 0)
            selectedChildPos = -1
        }
    }

    private fun showKeyPad() {
        reply_etext.requestFocus()
        inputMethodManager.toggleSoftInput(
            InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY
        )
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putInt("selectedGroupPos", adapter.selectedGroupPos)
        outState?.putInt("selectedChildPos", adapter.selectedChildPos)
        outState?.putParcelableArrayList("DataList", adapter.items as ArrayList<out Parcelable>)
    }
}