package com.yhishi.mytask

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_task_edit.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.yesButton
import java.text.SimpleDateFormat
import java.util.Date
import java.text.ParseException
import android.text.format.DateFormat
import android.view.View

class TaskEditActivity : AppCompatActivity() {

    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_edit)
        realm = Realm.getDefaultInstance()

        // インテントに格納したtask_idを取得（取得できなかった場合つまり新規登録の場合は-1）
        val taskId = intent?.getLongExtra("task_id", -1L)

        // 更新の場合
        if (taskId != -1L) {
            // 受け取ったtaskIdに一致するidをrealmから検索
            val task = realm.where<Task>().equalTo("id", taskId).findFirst()

            // 画面に表示
            dateEdit.setText(DateFormat.format("yyyy/MM/dd", task?.date))
            titleEdit.setText(task?.title)
            detailEdit.setText(task?.detail)

            // 削除ボタンを表示
            delete.visibility = View.VISIBLE
        } else {
            // 削除ボタンを非表示
            delete.visibility = View.INVISIBLE
        }

        // 保存処理
        save.setOnClickListener {
            when(taskId) {
                // 新規登録の場合
                -1L -> {
                    // realmトランザクション実行
                    realm.executeTransaction {
                        val maxId = realm.where<Task>().max("id")

                        // maxIdがnot nullならlong変換, nullなら0
                        val nextId = (maxId?.toLong() ?: 0L) + 1

                        // データを一行追加
                        val task = realm.createObject<Task>(nextId)

                        // 入力された日付をDate型に変換後、設定
                        dateEdit.text.toString().toDate("yyyy/MM/dd")?.let {
                            task.date = it
                        }
                        task.title = titleEdit.text.toString()
                        task.detail = detailEdit.text.toString()
                    }
                    alert("追加しました！") {
                        yesButton { finish() }
                    }.show()
                }
                // 更新の場合
                else -> {
                    realm.executeTransaction {
                        val task = realm.where<Task>().equalTo("id", taskId).findFirst()
                        dateEdit.text.toString().toDate("yyyy/MM/dd")?.let {
                            task?.date = it
                        }
                        task?.title = titleEdit.text.toString()
                        task?.detail = detailEdit.text.toString()
                    }
                    alert("修正しました！") {
                        yesButton { finish() }
                    }.show()
                }
            }
        }
        // 削除処理
        delete.setOnClickListener {
            realm.executeTransaction {
                realm.where<Task>().equalTo("id", taskId)?.findFirst()?.deleteFromRealm()
            }
            alert("削除しました！") {
                yesButton { finish() }
            }.show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    fun String.toDate(pattern: String = "yyyy/MM/dd HH:mm"): Date? {
        val sdFormat = try {
            SimpleDateFormat(pattern)
        } catch (e: IllegalArgumentException) {
            null
        }
        val date = sdFormat?.let {
            try {
                it.parse(this)
            } catch (e: ParseException) {
                null
            }
        }
        return date
    }
}
