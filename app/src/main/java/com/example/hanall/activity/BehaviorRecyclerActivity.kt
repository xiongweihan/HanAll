package com.example.hanall.activity


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.hanall.R
import com.example.hanall.adapter.BasicRecyclerAdapter
import com.example.hanall.utils.ConvertUtil
import com.example.hanall.utils.recycleview.RecycleUtil
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_behavior_recycler_actiuvity.*

/**
 * 可以点击回到原来位置的Recycleview
 */
class BehaviorRecyclerActivity : TitleActivity() {

    companion object {
        var list: ArrayList<String> = arrayListOf()

        @SuppressLint("StaticFieldLeak")
        var adapter: BasicRecyclerAdapter? = null

        @JvmStatic
        fun start(context: Context) {
            val intent = Intent(context, BehaviorRecyclerActivity::class.java)
            context.startActivity(intent)
        }

    }

    override fun getLayoutId2(): Int {
        return R.layout.activity_behavior_recycler_actiuvity
    }

    override fun initView() {
        super.initView()
        setRefreshType(RefreshType.NONE)
//        setBottomSheetBehaviorHeight()//代码动态设置CoordinatorLayout的窥视高度
        for (index in 0..20) {
            list.add("测测试数据")
        }
        adapter = BasicRecyclerAdapter(list, this)
        RecycleUtil.getInstance(this, recycler_view)
                .setVertical()
                .setAdapter(adapter)
        adapter!!.setOnItemClickListener { position, data, view ->
            run {
                recycler_view.scrollToPosition(0)
            }
            val bottomSheetBehavior = BottomSheetBehavior.from(recycler_view)
            //STATE_COLLAPSED: 关闭Bottom Sheets,显示peekHeight的高度，默认是0
            //STATE_DRAGGING: 用户拖拽Bottom Sheets时的状态
            //STATE_SETTLING: 当Bottom Sheets view摆放时的状态。
            //STATE_EXPANDED: 当Bottom Sheets 展开的状态
            //STATE_HIDDEN: 当Bottom Sheets 隐藏的状态
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }

    }


    //可以在XML布局设置，也可以代码动态设置CoordinatorLayout的窥视高度
    private fun setBottomSheetBehaviorHeight() {
        recycler_view.post {
            val layoutManager: RecyclerView.LayoutManager? = recycler_view.layoutManager
            if (layoutManager?.getChildAt(0) != null) {
                val height: Int = layoutManager.getChildAt(0)!!.height
                Log.e("--------->", "height:$height")
                val bottomSheetBehavior: BottomSheetBehavior<*> = BottomSheetBehavior.from<RecyclerView>(recycler_view)
                val dp: Float = ConvertUtil.toPx((height * 3).toFloat())
                bottomSheetBehavior.peekHeight = dp.toInt()
            }
        }
    }
}