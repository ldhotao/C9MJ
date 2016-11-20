package com.c9mj.platform.live.ui;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.c9mj.platform.R;
import com.c9mj.platform.user.ui.UserFragment;
import com.c9mj.platform.util.PhotoUtil;
import com.c9mj.platform.util.adapter.FragmentAdapter;
import com.c9mj.platform.widget.fragment.LazyFragment;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.CommonPagerTitleView;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * author: LMJ
 * date: 2016/9/1
 * 直播主页面
 */
public class LiveFragment extends LazyFragment {

    String[] liveTypeIdArray;
    String[] liveTypeNameArray;
    Integer[] logoArrays = new Integer[]{
            R.mipmap.ic_launcher,
            R.drawable.logo_douyu,
            R.drawable.logo_panda,
            R.drawable.logo_zhanqi,
            R.drawable.logo_yy,
            R.drawable.logo_longzhu,
            R.drawable.logo_quanmin,
            R.drawable.logo_cc,
            R.drawable.logo_huomao
    };

    List<Fragment> fragmentList = new ArrayList<>();
    List<String> titleList = new ArrayList<>();

    Context context;

    @BindView(R.id.magic_indicator)
    MagicIndicator indicator;
    CommonNavigatorAdapter navigatorAdapter;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    FragmentAdapter fragmentAdapter;
    @BindView(R.id.live_tv_live_type)
    TextView liveTvLiveType;

    public static LiveFragment newInstance() {
        LiveFragment fragment = new LiveFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_live, container, false);
        ButterKnife.bind(this, view);

        context = view.getContext();

        initData();
        initFragment();
        initViewPager();
        initIndicator();

        return view;
    }

    private void initData() {
        liveTypeIdArray = context.getResources().getStringArray(R.array.live_type_id);
        liveTypeNameArray = context.getResources().getStringArray(R.array.live_type_name);

    }

    @Override
    protected void initLazyView(@Nullable Bundle savedInstanceState) {
    }

    private void initFragment() {
        titleList.add(getString(R.string.live_lol));
        titleList.add(getString(R.string.live_ow));
        titleList.add(getString(R.string.live_dota2));
        titleList.add(getString(R.string.live_hs));
        titleList.add(getString(R.string.live_csgo));
        fragmentList.add(LiveListFragment.newInstance(getString(R.string.game_type_lol)));
        fragmentList.add(LiveListFragment.newInstance(getString(R.string.game_type_ow)));
        fragmentList.add(LiveListFragment.newInstance(getString(R.string.game_type_dota2)));
        fragmentList.add(LiveListFragment.newInstance(getString(R.string.game_type_hs)));
        fragmentList.add(LiveListFragment.newInstance(getString(R.string.game_type_csgo)));
    }

    private void initViewPager() {
        fragmentAdapter = new FragmentAdapter(this.getChildFragmentManager(), fragmentList);
        viewPager.setAdapter(fragmentAdapter);
        viewPager.setOffscreenPageLimit(4);
    }

    private void initIndicator() {

        CommonNavigator navigator = new CommonNavigator(context);
        navigator.setAdjustMode(true);
        navigator.setFollowTouch(true);
        navigatorAdapter = new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return fragmentList == null ? 0 : fragmentList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                CommonPagerTitleView titleView = new CommonPagerTitleView(context);
                titleView.setContentView(R.layout.item_live_tab_indicator_layout);//加载自定义布局作为Tab

                final TextView tab_textview = (TextView) titleView.findViewById(R.id.tab_text);
                tab_textview.setText(titleList.get(index));
                titleView.setOnPagerTitleChangeListener(new CommonPagerTitleView.OnPagerTitleChangeListener() {
                    @Override
                    public void onSelected(int i, int i1) {
                    }

                    @Override
                    public void onDeselected(int i, int i1) {

                    }

                    @Override
                    public void onLeave(int i, int i1, float v, boolean b) {

                    }

                    @Override
                    public void onEnter(int i, int i1, float v, boolean b) {

                    }
                });
                titleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(index);
                    }
                });

                return titleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                indicator.setLineHeight(UIUtil.dip2px(context, 2));
                indicator.setRoundRadius(UIUtil.dip2px(context, 2));
                indicator.setYOffset(UIUtil.dip2px(context, 0.5));
                indicator.setColors(getResources().getColor(R.color.color_icons));
                return indicator;
            }
        };

        navigator.setAdapter(navigatorAdapter);
        indicator.setNavigator(navigator);
        ViewPagerHelper.bind(indicator, viewPager);
    }

    @OnClick(R.id.live_tv_live_type)
    public void onClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View contentView = View.inflate(context, R.layout.layout_live_type_picker, null);
        ImageView iv_logo = (ImageView) contentView.findViewById(R.id.layout_live_type_picker_iv_logo);
        RecyclerView recylcerView = (RecyclerView) contentView.findViewById(R.id.layout_live_type_picker_recyclerview);



        builder.setView(contentView)
                .setPositiveButton(getString(R.string.enter), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.color_primary));
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.color_secondary_text));

    }
}
