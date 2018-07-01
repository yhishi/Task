package com.yhishi.mytask

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.realm.OrderedRealmCollection
import io.realm.RealmBaseAdapter


class TaskAdapter(data: OrderedRealmCollection<Task>?) : RealmBaseAdapter<Task>(data) {

    // View保持用のクラス。simple_list_item_2 XMLファイルのtext1とtext2を保持
    inner class ViewHolder(cell: View) {
        val date = cell.findViewById<TextView>(android.R.id.text1)
        val title = cell.findViewById<TextView>(android.R.id.text2)
    }

    /**
     * getView
     *
     * リストビューのセルのデータが必要になると呼ばれる
     * position:リストビューのセル位置, convertView:作成済みのセルを表すビュー, parent:親のリストビュー
     * セルはリストの１つを表し、セル用ビューは再利用される。
     * （例）6行のリストビューの場合、convertViewは6回nullでgetViewが呼ばれ、
     *      スクロールで7行目が表示されると、1行目のセルが再利用され、convertViewには1行目のセルがセットされる。
     */
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val viewHolder: ViewHolder

        when (convertView) {
            // セルに設定するビューを新規作成
            null -> {
                // 親のリストビューからLayoutInflaterインスタンス作成
                val inflater = LayoutInflater.from(parent?.context)

                // XMLファイルからビュー生成
                view = inflater.inflate(android.R.layout.simple_list_item_2, parent, false)
                viewHolder = ViewHolder(view)

                // 高速化のためにセル用ビューのタグに保持
                view.tag = viewHolder
            }
            else -> {
                view = convertView
                viewHolder = view.tag as ViewHolder
            }
        }

        // データを取り出して表示
        adapterData?.run {
            // データ取り出し
            val schedule = get(position)

            // 表示
            viewHolder.date.text = DateFormat.format("yyyy/MM/dd", schedule.date)
            viewHolder.title.text = schedule.title
        }
        // 設定を終えたビューを戻り値として返し、リストビューの一行を表示
        return view
    }
}