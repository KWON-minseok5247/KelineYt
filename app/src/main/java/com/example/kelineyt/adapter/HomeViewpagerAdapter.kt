package com.example.kelineyt.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
// https://heechokim.tistory.com/23
// https://notepad96.tistory.com/182
// FragmentStateAdapter는 각 페이지를 프래그먼트로 구성하고 싶을 때 사용한다.
// 내부적으로 모든 페이지의 프래그먼트 인스턴스가 아닌 필요한 만큼의 인스턴스만 가지고 있고
// 나머지 페이지에 대해서는 상태값만 들고 있는 상태다.
class HomeViewpagerAdapter(
    private val fragments: List<Fragment>,
    fm: FragmentManager,
    lifecycle: Lifecycle
): FragmentStateAdapter(fm, lifecycle) {

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}

//1 -> fragement와 layout을 만든다. viewpager와 tablayout을 만들어둔다
//2. 뷰페이저 어뎁터를 만든다. 여기서 탭 레이아웃을 여러개 사용하니 homeviewpager(
//리스트<프래그먼트>, fm, lifecycle)로 하고
//사이즈, createfragmentr를 만들어둔다. 컨트롤 i하면 자동으로 등록된다.
//그러면 getItemCount는 fragments.size를 리턴하고 createFragment는 아마도 return fragments[position]하면 되겠지
//3. homefragment에서 먼저 fragmentlist가 필요하다. -> val list = arraylistof(chair, furniture....)로 해둔다.
//binding.viewpager = homeviewpager(list, childfragmentmanager, lifecycle)
//그 다음 tabLayoutMediator(binding.tablayout, binding.viewpager) { tab, position ->
//when (position) {
//0 -> tab.text = Main....
//}하고 마지막에 attach()를 붙이면 탭레이아웃이 완성된다.