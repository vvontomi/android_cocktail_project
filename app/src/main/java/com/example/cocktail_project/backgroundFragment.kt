package com.example.cocktail_project

import android.content.ClipData
import android.content.ClipDescription
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.DragEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_background.*
import android.os.Handler
import android.os.Looper
import android.widget.Button
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.cocktail_project.databinding.FragmentBackgroundBinding


class backgroundFragment : Fragment() {

    lateinit var alcholFragment: AlcholFragment
    lateinit var drinkFragment: DrinkFragment
    lateinit var garnishFragment: GarnishFragment
    lateinit var result : ArrayList<String>
    lateinit var select_elements : ArrayList<String>
    //힌트 기능 구현
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        result = arguments?.getStringArrayList("recipe2background") as ArrayList<String>

        select_elements = ArrayList()
        select_elements.add(result.get(result.lastIndex)) //firebase 사진 uri
        select_elements.add(result.get(result.lastIndex - 1)) //칵테일 이름

        Log.d("LOG", result.get(result.lastIndex - 2))
        if (result.get(result.lastIndex - 2) == "null"){ //재료가 3개인 칵테일의 경우 마지막 null 값 제거
            Log.d("LOG", "the cocktail elements has null")
            result.removeAt(result.lastIndex - 2)
            Log.d("LOG", "after null clean $result")
        }

//        Handler(Looper.getMainLooper()).postDelayed({
//            recipe.visibility = View.INVISIBLE
//            recipea.visibility = View.INVISIBLE
//            recipeb.visibility = View.INVISIBLE
//            recipec.visibility = View.INVISIBLE
//            reciped.visibility = View.INVISIBLE
//        },0L) // 처음에는 안뜨게 하기
//
//        Handler(Looper.getMainLooper()).postDelayed({
//            recipe.visibility = View.VISIBLE
//            recipea.visibility = View.VISIBLE
//            recipeb.visibility = View.VISIBLE
//            recipec.visibility = View.VISIBLE
//            reciped.visibility = View.VISIBLE
//            Handler(Looper.getMainLooper()).postDelayed({
//                recipe.visibility = View.INVISIBLE
//                recipea.visibility = View.INVISIBLE
//                recipeb.visibility = View.INVISIBLE
//                recipec.visibility = View.INVISIBLE
//                reciped.visibility = View.INVISIBLE
//            },2500L) // 텍스트뷰 잠시 뜨는 시간
//        },10000L) // 지연시간
//
//        Handler(Looper.getMainLooper()).postDelayed({
//            timerTxt.visibility = View.INVISIBLE
//        },10000L)

//        val countDown = object : CountDownTimer(10000, 1000) {
//            override fun onTick(millisUntilFinished: Long) {
//                timerTxt.setText("힌트 등장 " +millisUntilFinished / 1000 + "초 전" ).toString()
//            }
//            override fun onFinish() {
//                timerTxt.setText("힌트 없음").toString()
//                Handler(Looper.getMainLooper()).postDelayed({
//                    timerTxt.visibility = View.VISIBLE
//                },2500L)
//            }
//        }.start()

        alcholFragment = AlcholFragment()
        drinkFragment = DrinkFragment()
        garnishFragment = GarnishFragment()
    }

    //탭 레이아웃 구현
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var root_view = inflater.inflate(R.layout.fragment_background, container, false)
        var tab_layout = root_view.findViewById<TabLayout>(R.id.tab_layout)
        parentFragmentManager.beginTransaction().replace(R.id.frame_layout, alcholFragment).commit() //첫 Fragment
        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        replaceView(alcholFragment)
                    }
                    1 -> {
                        replaceView(drinkFragment)
                    }
                    else -> {
                        replaceView(garnishFragment)
                    }
                }
            }

            private fun replaceView(tab: Fragment) {
//                var selectedFragment: Fragment? = null
//                selectedFragment = tab
//                selectedFragment?.let {
                parentFragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_layout, tab)
                    .commit()

                //}
            }
        })

        //드래그 앤 드롭 기능
        var target_glass = root_view.findViewById<ImageView>(R.id.glass1)

        target_glass.setOnDragListener { v, e ->
            when (e.action) {
                DragEvent.ACTION_DRAG_STARTED -> { //드래그 시작 (이미지를 끌었을 때)
                    // 드래그된 데이터를 accept 할 수 있는지 결정
                    if (e.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                        //드래그 되었을 때 확인하기 위해서 넣은 배경 색
                        //(v as? ImageView)?.setColorFilter(Color.BLUE)
                        Toast.makeText(this.context, "ACTION STARTED", Toast.LENGTH_LONG)
                        // 다시 그리기 , 화면 갱신
                        v.invalidate()

                        //드래그 된 데이터 받았을 때 True
                        true
                    } else {
                        //끄는중 false, END가 될 때까지 이벤트 다시 수신 X
                        false
                    }
                }
                DragEvent.ACTION_DRAG_ENTERED -> {
//                    (v as? ImageView)?.setColorFilter(Color.GREEN)
                    (v as? ImageView)?.alpha = 0.5F

                    Toast.makeText(this.context, "Drag Entered", Toast.LENGTH_LONG)

                    // 다시 그리기
                    v.invalidate()

                    // Returns true; the value is ignored.
                    true
                }

                DragEvent.ACTION_DRAG_LOCATION ->
                    //이벤트 무시
                    true

                DragEvent.ACTION_DRAG_EXITED -> {
                    //파란색으로 재설정
                    //(v as? ImageView)?.setColorFilter(Color.BLUE)
                    (v as? ImageView)?.alpha = 1.0F

                    Toast.makeText(this.context, "Drag Exit", Toast.LENGTH_LONG)

                    //뷰 리셋
                    v.invalidate()

                    true
                }
                DragEvent.ACTION_DROP -> { //드랍했을 때
                    //드래그 된 데이터가..포함한? 항목을 가져옴
                    val item: ClipData.Item = e.clipData.getItemAt(0)

                    //텍스트 데이터 갖고오기
                    val dragData = item.text

                    // 드래그한 데이터가 포함된 메시지를 표시
                    select_elements.add(dragData.toString())
                    for (test_string in result) {
                        Log.d("LOG", test_string)
                        var drag_data = dragData.toString()
                        if (drag_data == test_string) {
                            Log.d("LOG", "$test_string is same to $dragData")
                        }
                    }
                    Log.d("LOG", "selected elements : $select_elements")
                    Log.d("LOG", "cocktail elements : $result")


//                    Toast.makeText(this.context, "Dragged data $dragData", Toast.LENGTH_LONG).show()
//                    Toast.makeText(this.context, "Dragged data $test_string", Toast.LENGTH_LONG).show()


                    if (select_elements.size == 3)
                        (v as? ImageView)?.setImageResource(R.drawable.second_glass)
                    if (select_elements.size == 4)
                        (v as? ImageView)?.setImageResource(R.drawable.third_glass)
                    if (select_elements.size == 5)
                        (v as? ImageView)?.setImageResource(R.drawable.fourth_glass)
                    if (select_elements.size == 6)
                        (v as? ImageView)?.setImageResource(R.drawable.fifth_glass)

                    (v as? ImageView)?.alpha = 1.0F
                    v.invalidate()

                    //DragEvent.getResult()는 true를 반환
                    true
                }

                DragEvent.ACTION_DRAG_ENDED -> {
                    //색조 clear
                    (v as? ImageView)?.clearColorFilter()

                    //리셋
                    v.invalidate()

                    //getResult() 수행, 발생한 상황 표시
//                    when(e.result) {
//                        true ->
//                            Toast.makeText(this.context, "The drop was handled.", Toast.LENGTH_LONG)
//                        else ->
//                            Toast.makeText(this.context, "The drop didn't work.", Toast.LENGTH_LONG)
//                    }.show()

                //true 반환
                    true
                }
                else -> {
                    Log.e("DragDrop Example", "Unknown action type received by View.OnDragListener.")
                    false
                }
            }
        }

        val shakebutton = root_view.findViewById<Button>(R.id.btn_mix)
        shakebutton.setOnClickListener {
            var check = true
            Log.d("LOG", "click mix button, check elements.")
            Log.d("LOG", "selected elements : $select_elements")
            Log.d("LOG", "cocktail elements : $result")
            if (select_elements.size != result.size){ //선택의 개수가 다른 경우
                check = false
            }
            else { //선택의 개수가 같은경우 제대로 된 재료를 선택 했는지 확인
                for (element in result) {
                    var check2 = false
                    for (select_element in select_elements) {
                        if (element == select_element) check2 = true
                    }
                    if (check2 == false) {
                        check = false
                        break
                    }
                }
            }

            var bundle_result = ArrayList<String>()
            if (check == true){
                Log.d("LOG", "Right Element Select !! good job")
                bundle_result.add("True")
            }
            else {
                Log.d("LOG", "Wrong Element select ..")
                bundle_result.add("False")
            }
            bundle_result.add(result.get(result.lastIndex - 1)) //선택했던 칵태일 이름
            bundle_result.add(result.get(result.lastIndex)) //선택했던 칵테일 파베 주소
            val bundle = bundleOf("background2shake" to bundle_result)

            findNavController().navigate(R.id.action_backgroundFragment_to_shakeFragment, bundle)

        }
        //프래그먼트 레이아웃 확장
        return root_view
    }

    val binding : FragmentBackgroundBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.btnHome?.setOnClickListener {
            findNavController().navigate(R.id.action_backgroundFragment_to_startFragment)
        }
        binding?.btnBack?.setOnClickListener {
            findNavController().navigate(R.id.action_backgroundFragment_to_selectFragment)
        }
    }
}
